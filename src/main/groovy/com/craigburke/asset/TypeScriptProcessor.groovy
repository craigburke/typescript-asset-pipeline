package com.craigburke.asset

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import asset.pipeline.AssetHelper
import asset.pipeline.CacheManager
import asset.pipeline.AssetPipelineConfigHolder
import groovy.transform.Synchronized
import org.mozilla.javascript.NativeArray
import org.mozilla.javascript.NativeString

class TypeScriptProcessor extends JavaScriptProcessor {
    static ThreadLocal<TypeScriptCompileState> localCompileState = new ThreadLocal<TypeScriptCompileState>()

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
        localCompileState.set(new TypeScriptCompileState(baseAsset: assetFile))

        javaScript {
            tsFile = assetFile.name
            eval 'ts.compile(tsFile);'
        }

        result
    }

    static String getFileContents(String filePath) {
        TypeScriptCompileState compileState = getLocalCompileState().get()

        if (filePath == 'lib.d.ts') {
            TypeScriptProcessor.classLoader.getResource('lib.d.ts').text
        }
        else if (filePath == compileState.baseAsset.name) {
            compileState.baseAsset.inputStream.text
        }
        else {
            String assetPath = AssetHelper.normalizePath("${compileState.baseAsset.parentPath}/${filePath}")
            AssetFile referenceFile = AssetHelper.fileForFullName(assetPath)

            if (referenceFile) {
                CacheManager.addCacheDependency(compileState.baseAsset.path, referenceFile)
            }

            referenceFile?.inputStream?.text ?: ''
        }
    }

    static void setResult(String result) {
        localCompileState.get().result = result
    }

    static String getResult() {
        localCompileState.get().result
    }

    static NativeArray getCompileOptions(String name) {
        List options = []
        Map configOptions = AssetPipelineConfigHolder.config?.typeScript ?: [:]
        configOptions.each { String key, value ->
            String configKey = "--${key}"
            if (value.getClass() != Boolean) {
                options += [configKey, value]
            }
            else if (value) {
                options += configKey
            }
        }
        options += name
    }

}