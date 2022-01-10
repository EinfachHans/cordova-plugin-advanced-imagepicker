### [1.5.5](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.5.4...V1.5.5) (2022-01-07)


### Bug Fixes

* **android:** don't wrap base64 output ([#33](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/issues/33)) ([b03620b](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/b03620b429551c68c939ef484e692a665f3b29ca))

### [1.5.4](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.5.3...V1.5.4) (2021-10-18)


### Bug Fixes

* **ios:** use correct key for error code ([57180f3](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/57180f356ee2034feedad22a6f0eca21eadd34b1))

### [1.5.3](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.5.2...V1.5.3) (2021-10-18)


### Bug Fixes

* **ios:** use double instead of NSFloat ([8bf948f](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/8bf948f4af4ed6c686921eb4b1430d403ee2d4a4))

### [1.5.2](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.5.1...V1.5.2) (2021-10-18)


### Bug Fixes

* **ios:** fix ioa15 styling [See](https://github.com/Yummypets/YPImagePicker/issues/690) ([1c02839](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/1c02839d2e6d9b46a61a532d056f67a5bf2f9561))
* **ios:** remove line breaks from generated base64 ([dfb472b](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/dfb472b327696302b8e730b1832b93b78966bcf6))
* **ios:** remove line breaks from generated video base64 ([1f60c2d](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/1f60c2ddec3051125c69aa9b3ec3b1fe8b00f081))

### [1.5.1](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.5.0...V1.5.1) (2021-10-12)


### Bug Fixes

* **ios:** use double instead of NSFloat ([#26](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/issues/26)) ([91a0c2a](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/91a0c2af8f7bf38ebec6148720633186e921482d))

## [1.5.0](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/compare/V1.4.0...V1.5.0) (2021-10-10)


### Features

* **ios:** add more video config options ([#24](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/issues/24)) ([2833f38](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/2833f38b35c63afe6eabe5b80ae53c849e4b072d))


### Code Refactoring

* add semantic release ([cdf0006](https://github.com/EinfachHans/cordova-plugin-advanced-imagepicker/commit/cdf0006aea589d9444716b6184f60b8816689ac3))

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
