var exec = require('cordova/exec');

pluginName = 'AdvancedImagePicker';

exports.ErrorCodes = {
  UnsupportedAction: 1,
  WrongJsonObject: 2,
  PickerCanceled: 3,
  UnknownError: 10
};

exports.present = function (options, success, error) {
  exec(success, error, pluginName, 'present', [options]);
};

exports.cleanup = function (success, error) {
  exec(success, error, pluginName, 'cleanup', [])
}
