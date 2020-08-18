var plugin = function () {
  return window.AdvancedImagePicker || {};
};
var AdvancedImagePicker = /** @class */ (function () {
  function AdvancedImagePicker() {
  }

  AdvancedImagePicker.ErrorCodes = plugin().ErrorCodes;

  AdvancedImagePicker.present = function (options, success, failure) {
    var plu = plugin();
    return plu.present.apply(plu, arguments);
  };

  return AdvancedImagePicker;
}());
export default AdvancedImagePicker;
