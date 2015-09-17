package com.craigburke.asset

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import asset.pipeline.AssetHelper
import asset.pipeline.AssetPipelineConfigHolder
import groovy.transform.Synchronized
import org.mozilla.javascript.Context

class TypeScriptProcessor extends JavaScriptProcessor {

    TypeScriptProcessor(AssetCompiler precompiler) {
        super(precompiler)
    }

    static JavaScriptEngine typeScriptEngine
    @Synchronized
    JavaScriptEngine getEngine() {
        typeScriptEngine = typeScriptEngine ?: new JavaScriptEngine('tsc.js', true)
        typeScriptEngine
    }

    String process(String input, AssetFile assetFile) {
        List options = AssetPipelineConfigHolder.config?.typeScript?.compileOptions ?: []
        String optionsValue = "[${options.collect {"'${it}'"}.join(',')}]"

        javaScript {
            getReferenceFiles(assetFile).each { String name, String data ->
                eval "ts.addReferenceFile('${name}', '${data}');"
            }
            eval "ts.compile('${assetFile.name}', '${formatJavascriptAsString(input)}', ${optionsValue});"
        }.replace('\\n', '\n')
    }

    static String formatJavascriptAsString(String value) {
        value.replace("'", "\\'").replaceAll("(\r)?\n", " \\\\n")
    }

    static Map getReferenceFiles(AssetFile assetFile, AssetFile baseFile = null) {
        Map references = [:]
        baseFile = baseFile ?: assetFile

        assetFile.inputStream.text.findAll(~/reference path=\s*"(.+?)"/) { String fullMatch, String value ->
            String path = AssetHelper.normalizePath("${assetFile.parentPath ?: ''}/${value}") - baseFile.path
            path = path.startsWith('/') ? path.substring(1) : path

            AssetFile referenceFile = AssetHelper.fileForFullName(path)
            if (referenceFile) {
                references[path] = formatJavascriptAsString(referenceFile.inputStream.text)
                references << getReferenceFiles(referenceFile, baseFile)
            }
        }

        references
    }

}