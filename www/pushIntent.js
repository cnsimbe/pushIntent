var exec = require('cordova/exec');

exports.getPushIntent = function(arg0, success, error) {
    exec(success, error, "pushIntent", "getPushIntent", []);
};
