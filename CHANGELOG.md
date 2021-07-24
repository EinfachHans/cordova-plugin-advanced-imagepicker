# Changelog

## 1.4.0
- Upgrade iOS dependency ([See changes](https://github.com/Yummypets/YPImagePicker/releases/tag/4.5.0))
- Upgrade default android dependency ([See Changes](https://github.com/ParkSangGwon/TedImagePicker/releases/tag/1.2.2))
- iOS: `PickerCanceled` ErrorCode added

### Breaking
The Change of the android dependency also includes a dependency-name change. This **could** cause old versions to not work anymore. So make sure to update `ANDROID_IMAGE_PICKER_VERSION` in the `package.json` to `1.2.2`

## 1.3.2
- Change obsolete `android.dataBinding.enabled` to `android.buildFeatures.dataBinding`

## 1.3.1
- fix error with new android default version

## 1.3.0
- use image.pngData() instead of UIImagePNGRepresentation
- iOS: Video Compression added
- `asJpeg added`
- correctly compress Videos on Android

## 1.2.0
- return Unsupported Error on android
- fix: append correct screens by mediaType on iOS [#7](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/issues/7)
- `startOnScreen` config added for iOS

## 1.1.0
- Update Android default Version to 1.1.4 --> Enabled SDK 30 support
- Update iOS Pod to 4.4.0 --> [Changelog](https://github.com/Yummypets/YPImagePicker/releases)
- **BREAKING**: iOS know works with a file url and only returns isBase64 if `asBase64: true` was set
- Function `cleanup` added for ios


## 1.0.1
- I changed my GitHub Username, so i need to adjust stuff
- Typing fixed (closes [#4](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/issues/4))

## 1.0.0
- Initial Release
