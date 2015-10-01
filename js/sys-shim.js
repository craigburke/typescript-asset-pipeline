module.exports = (function() {
    return {
        args: [],
        newLine: '\n',
        useCaseSensitiveFileNames: false,
        write: function(s) {
            java.lang.System.out.println(s);
        },
        readFile: function(fileName) {
            return String(Packages.com.craigburke.asset.TypeScriptProcessor.getFileContents(fileName));
        },
        writeFile: function(fileName, data) {
            Packages.com.craigburke.asset.TypeScriptProcessor.setResult(data);
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
