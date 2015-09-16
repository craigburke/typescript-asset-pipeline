package com.craigburke.asset

import asset.pipeline.GenericAssetFile
import spock.lang.Specification

class TypeScriptProcessorSpec extends Specification {

    def "compile a basic typescript file"() {
        when:
        String compiledJs = processor.process(input, new GenericAssetFile())

        then:
        compiledJs.contains('var Greeter')

        where:
        processor = new TypeScriptProcessor()

        input = """\
        class Greeter {
            greeting: string;
            constructor(message: string) {
                this.greeting = message;
            }
            greet() {
                return "Hello, " + this.greeting;
            }
        }"""
    }

}