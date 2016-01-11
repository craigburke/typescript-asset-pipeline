module.exports = (function() {

    var TypeScriptProcessor = Java.type('com.craigburke.asset.TypeScriptProcessor');

    return {
        args: [],
        newLine: '\n',
        useCaseSensitiveFileNames: false,
        write: function(s) {
            java.lang.System.out.println(s);
        },
        readFile: function(fileName) {
            return TypeScriptProcessor.readFile(fileName);
        },
        writeFile: function(fileName, data) {
            TypeScriptProcessor.writeFile(fileName, data);
        },
        resolvePath: function(path) { return path; },
        fileExists: function(path) { return true; },
        directoryExists: function(path) { return true; },
        createDirectory: function(directoryName) { },
        getExecutingFilePath: function() { return ''; },
        getCurrentDirectory: function() { return ''; },
        readDirectory: function() { },
        exit: function(exitCode) { }
    }
})();
