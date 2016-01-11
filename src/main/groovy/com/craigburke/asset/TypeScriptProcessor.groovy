package com.craigburke.asset

import asset.pipeline.AbstractProcessor
import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
import asset.pipeline.AssetHelper
import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.CacheManager
import groovy.transform.Synchronized

import javax.script.Invocable
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class TypeScriptProcessor extends AbstractProcessor {

    static ThreadLocal<TypeScriptCompileState> localCompileState = new ThreadLocal<TypeScriptCompileState>()
    static Invocable engine
    static String libDLibrary

    TypeScriptProcessor(AssetCompiler precompiler) {
        super(precompiler)
        initialize()
    }

    @Synchronized
    static initialize() {
        if (!engine) {
            ScriptEngineManager engineManager =  new ScriptEngineManager()
            ScriptEngine jsEngine = engineManager.getEngineByName("nashorn")
            URL tsc = TypeScriptProcessor.classLoader.getResource('tsc.js')
            jsEngine.eval(tsc.text)
            engine = (Invocable)jsEngine
            libDLibrary = TypeScriptProcessor.classLoader.getResource('lib.d.ts').text
        }
    }

    String process(String input, AssetFile assetFile) {
        localCompileState.set(new TypeScriptCompileState(baseAsset: assetFile))
        List<String> options = getOptions(assetFile.name)
        engine.invokeFunction('tsc', options)
        localCompileState.get().result
    }

    static List<String> getOptions(String name) {
        List<String> options = []
        Map<String, Object> configOptions = AssetPipelineConfigHolder.config?.typeScript ?: [:]
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
        options
    }

    static String readFile(String filePath) {
        TypeScriptCompileState compileState = getLocalCompileState().get()

        if (filePath == 'lib.d.ts') {
            libDLibrary
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

    static String writeFile(String name, String content) {
        localCompileState.get().result = content
    }

}