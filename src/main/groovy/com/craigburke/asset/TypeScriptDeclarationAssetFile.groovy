package com.craigburke.asset

import asset.pipeline.AbstractAssetFile

class TypeScriptDeclarationAssetFile extends AbstractAssetFile {
    static final String contentType = 'text/plain'
    static extensions = ['d.ts']
    static final String compiledExtension = 'd.ts'

    static processors = []

    static String directiveForLine(String line) {
        return null
    }
}