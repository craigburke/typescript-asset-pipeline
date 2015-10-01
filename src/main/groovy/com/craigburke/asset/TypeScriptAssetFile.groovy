package com.craigburke.asset

import asset.pipeline.AbstractAssetFile

class TypeScriptAssetFile extends AbstractAssetFile {
    static final String contentType = 'application/javascript'
    static extensions = ['ts']
    static final String compiledExtension = 'js'

    static processors = [TypeScriptProcessor]
}