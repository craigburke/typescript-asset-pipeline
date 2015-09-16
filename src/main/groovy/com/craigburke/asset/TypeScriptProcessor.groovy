package com.craigburke.asset

import asset.pipeline.AssetCompiler
import asset.pipeline.AssetFile
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
        String formattedInput = input.replace("'", "\\'").replaceAll("(\r)?\n", " \\\\n")

        javaScript {
            eval "files['foo.ts'] = '${formattedInput}';"
            eval "compile('foo');"
        }
    }

}