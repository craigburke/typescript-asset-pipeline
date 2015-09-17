package com.craigburke.asset

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import asset.pipeline.AssetHelper
import groovy.transform.Synchronized

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
        if (assetFile.name.endsWith('d.ts')) {
            return input
        }

        javaScript {
            getReferenceFiles(assetFile).each { String name, String data ->
                eval "ts.addReferenceFile('${name}', '${data}');"
            }
            eval "ts.compile('${assetFile.name}', '${formatJavascriptAsString(input)}');"
        }.replace('\\n', '\n')
    }

    static String formatJavascriptAsString(String value) {
        value.replace("'", "\\'").replaceAll("(\r)?\n", " \\\\n")
    }

    static Map getReferenceFiles(AssetFile assetFile, String basePath = '') {
        Map references = [:]

        assetFile.inputStream.text.findAll(~/reference path=\s*"(.+?)"/) { String fullMatch, String value ->
            String path = AssetHelper.normalizePath("${assetFile.parentPath ?: ''}/${value}") - basePath
            AssetFile referenceFile = AssetHelper.fileForFullName(path)
            if (referenceFile) {
                references[path] = formatJavascriptAsString(referenceFile.inputStream.text)
                references << getReferenceFiles(referenceFile, basePath ?: assetFile.path)
            }
        }

        references
    }


}