package com.craigburke.asset

import asset.pipeline.GenericAssetFile
import spock.lang.Specification

class TypeScriptProcessorSpec extends Specification {

    static String fileContent = """\
        class Greeter {
            greeting: string;
            constructor(message: string) {
                this.greeting = message;
            }
            greet() {
                return "Hello, " + this.greeting;
            }
        }"""


    def "compile a basic typescript file"() {
        when:
        String compiledJs = processor.process(fileContent, mockAssetFile)

        then:
        compiledJs.contains('var Greeter')

        where:
        processor = new TypeScriptProcessor()

        mockAssetFile = [ getName: {'foo.ts'}, getInputStream: { new ByteArrayInputStream(fileContent.bytes) } ] as GenericAssetFile
    }

}
