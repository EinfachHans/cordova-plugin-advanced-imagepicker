# Advanced ImagePicker Cordova Plugin
![Maintenance](https://img.shields.io/maintenance/yes/2021)
[![npm version](https://badge.fury.io/js/cordova-plugin-advanced-imagepicker.svg)](https://badge.fury.io/js/cordova-plugin-advanced-imagepicker)

This [Cordova](https://cordova.apache.org) Plugin is for a better (multiple) ImagePicker with more options.

It currently uses [Yummypets/YPImagePicker](https://github.com/Yummypets/YPImagePicker) (Version `4.5.0`) on iOS and 
[ParkSangGwon/TedImagePicker](https://github.com/ParkSangGwon/TedImagePicker) (Default-Version `1.2.2`) on Android. 

**This Plugin is in active development!**

<!-- DONATE -->
[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG_global.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=LMX5TSQVMNMU6&source=url)

This and other Open-Source Cordova Plugins are developed in my free time.
To help ensure this plugin is kept updated, new features are added and bugfixes are implemented quickly, please donate a couple of dollars (or a little more if you can stretch) as this will help me to afford to dedicate time to its maintenance.
Please consider donating if you're using this plugin in an app that makes you money, if you're being paid to make the app, if you're asking for new features or priority bug fixes.
<!-- END DONATE -->

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Content**

- [Install](#install)
  - [Requirements](#requirements)
  - [Android](#android)
  - [iOS](#ios)
- [Environment Variables](#environment-variables)
  - [Android](#android-1)
  - [iOS](#ios-1)
- [Usage](#usage)
  - [Failure Callbacks](#failure-callbacks)
  - [Error Codes](#error-codes)
- [Api](#api)
  - [All platforms](#all-platforms)
    - [present](#present)
  - [iOS](#ios-2)
    - [cleanup](#cleanup)
- [Quirks](#quirks)
  - [Android](#android-2)
- [Changelog](#changelog)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# Install

## Requirements

- **cordova** `>= 9.0.0`
- **cordova-android** `>= 9.0.0`

## Android
Because the used Framework uses AndroidX and is developed in Kotlin, make sure to enable `AndroidXEnabled` and `GradlePluginKotlinEnabled`: 
```xml
<preference name="AndroidXEnabled" value="true"/>
<preference name="GradlePluginKotlinEnabled" value="true"/>
```

## iOS

This Plugin is developed in Swift and automaticaly adds the Plugin to [Support Swift](https://github.com/akofman/cordova-plugin-add-swift-support).

I developed it, testing with **cordova-ios@6.1.0**.

# Environment Variables

## Android

- ANDROID_IMAGE_PICKER_VERSION - Version of `gun0912.ted:tedimagepicker` / default to `1.1.4` 

## iOS

The iOS platform defines:
- NSCameraUsageDescription: **This app requires access to the camera to take pictures and videos.**
- NSPhotoLibraryUsageDescription: **This app requires access to the photo library to select pictures and videos.**
- NSMicrophoneUsageDescription: **This app requires access to the microphone to record videos.**

You can easily change them, by configure your **config.xml** by:
```xml
<edit-config file="*-Info.plist" mode="merge" target="NSCameraUsageDescription">
    <string>your text</string>
</edit-config>
```

# Usage

The plugin is available via a global variable named `window.AdvancedImagePicker`.
A TypeScript definition is included out of the Box. You can import it like this:
```ts
import AdvancedImagePicker from 'cordova-plugin-advanced-imagepicker';
```

## Failure Callbacks

If an Error appeared this Plugin returns an Object in the failureCallback, that always has the following Structure:

```json
{
  "code": 0,
  "message": "Some additional Info"
}
```

The `code` is one of the [Error Codes](#error-codes) and always present, while the `message` can be empty.
This is mostly something like an Exception Message.

## Error Codes

The following Error Codes can be fired by this Plugin:
- UnsupportedAction
- WrongJsonObject
- PickerCanceled  
- UnknownError

They can be accessed over for Example `window.AdvancedImagePicker.ErrorCodes.UnsupportedAction` and are present in the TypeScript definition too of course. 

# Api

The list of available methods for this plugin is described below.

## All platforms

### present

Open the ImagePicker

#### Parameters:

- options (object) - a JSON-Object containing the following Elements (all optional):
    - mediaType (string) **default: "IMAGE"**
    - showCameraTile (boolean) **default: true**
    - startOnScreen (string) **default: "LIBRARY"**
    - scrollIndicatorDateFormat (string) **Android only, default: "YYYY.MM"**
    - showTitle (boolean) **Android only, default: true**
    - title (string) **Android only, default: "Select Image"**
    - zoomIndicator (boolean) **Android only, default: true**
    - min (number) **default: 0 (android), 1 (iOS)**
    - minCountMessage (string) **Android only, default: "You need to select a minimum of ... files"**
    - max (number) **default: 0 (android), 1 (iOS)**
    - maxCountMessage (string) **default: "You need to select a minimum of ... files"**
    - buttonText (string)
    - asDropdown (boolean) **default: false**
    - asBase64 (boolean) **default: false**
    - asJpeg (boolean) **default: false**
    - videoCompression (string) ([Available Options](https://github.com/Yummypets/YPImagePicker/blob/23158e138bd649b40762bf2e4aa4beb0d463a121/Source/Configuration/YPImagePickerConfiguration.swift#L226-L240)) **default: AVAssetExportPresetHighestQuality**

```js
window.AdvancedImagePicker.present({
  // config here
}, function(success) {
  console.log(success);
}, function (error) {
  console.error(error);
});
```
#### SuccessType:

This Methode returns an Array of Objects with the following fields:

- isBase64 (boolean) - is file base64 encoded
- type (`'image'` | `'video'`) - type of selected file
- src (string) - Result as file-uri or base64 encoded string

## iOS

### cleanup

Cleans the files that were stored in the tmp file directory

```js
window.AdvancedImagePicker.cleanup(function() {
  console.log('success');
}, function (error) {
  console.error(error);
});
```

# Quirks

## Android

- Currently, the Android Library is not able to select Images and Videos at the same Time. See reported Issue [here](https://github.com/ParkSangGwon/TedImagePicker/issues/40).
- The `PickerCanceled` ErrorCode is currently not supported in Android

# Changelog

The full Changelog is available [here](CHANGELOG.md)
