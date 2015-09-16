module.exports = (function() {
    var files = {};
    // Add Files
    return {
        args: [],
        newLine: '\\n',
        useCaseSensitiveFileNames: false,
        write: function(s) {
            java.lang.System.out.println(s);
        },
        readFile: function(fileName) {
            return files[fileName];
        },
        writeFile: function(fileName, data) {
            files[fileName] = data;
        },
        resolvePath: function(path) {
            return '/foo';
        },
        fileExists: function(path) { return false; },
        directoryExists: function(path) { return false; },
        createDirectory: function(directoryName) { },
        getExecutingFilePath: function() {
            return 'foo';
        },
        getCurrentDirectory: function() {
            return '';
        },
        readDirectory: function() { },
        exit: function(exitCode) { }
    }
})();
