package com.craigburke.asset

import asset.pipeline.AssetPipelineConfigHolder
import asset.pipeline.GenericAssetFile
import asset.pipeline.fs.FileSystemAssetResolver
import spock.lang.Shared
import spock.lang.Specification

class TypeScriptProcessorSpec extends Specification {

    @Shared FileSystemAssetResolver resolver = new FileSystemAssetResolver('test', 'src/test/resources', false)

    void setup() {
        AssetPipelineConfigHolder.resolvers << resolver
    }

    def "compile a basic typescript file"() {
        when:
        String compiledJs = processor.process(assetFile.inputStream.text, assetFile)

        then:
        compiledJs.contains('var Greeter')

        where:
        processor = new TypeScriptProcessor()
        assetFile = getAssetFile('simple.ts')
    }

    def "references"() {
        when:
        processor.process(assetFile.inputStream.text, assetFile)

        then:
        notThrown(Exception)

        where:
        processor = new TypeScriptProcessor()
        assetFile = getAssetFile('references/references.ts')
    }

    GenericAssetFile getAssetFile(String path) {
        resolver.getAsset(path) as GenericAssetFile
    }

}
