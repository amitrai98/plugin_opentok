cordova.define("cordova_evontech_tokbox.TokBoxPhonegapPlugin", function(require, exports, module) {
var exec = require('cordova/exec');

exports.coolMethod = function(arg0, success, error) {
    exec(success, error, "TokBoxPhonegapPlugin", "coolMethod", [arg0]);
};

exports.connectToSession = function(arg0, success, error) {
	exec(success, error, "TokBoxPhonegapPlugin", "connectToSession", [arg0]);
};

exports.sendTextMessage = function(arg0, success, error) {
    exec(success, error, "TokBoxPhonegapPlugin", "sendTextMessage", [arg0]);
};

exports.initializeVideoCall = function(arg0, success, error) {
    exec(success, error, "TokBoxPhonegapPlugin", "initializeVideoCall", [arg0]);
};


});
