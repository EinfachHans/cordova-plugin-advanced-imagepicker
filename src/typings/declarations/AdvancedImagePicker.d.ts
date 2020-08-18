/// <reference path="./interfaces/error.d.ts" />
/// <reference path="./interfaces/options.d.ts" />
/// <reference path="./interfaces/result.d.ts" />

declare module 'cordova-plugin-advanced-imagepicker' {

  export default class AdvancedImagePicker {

    /**
     * Available Error Codes
     */
    static ErrorCodes: {
      UnsupportedAction,
      WrongJsonObject,
      UnknownError
    };

    /**
     * Present the ImagePicker
     *
     * @param options Configure the Selection
     * @param success Success Callback
     * @param error Error Callback
     */
    static present(options: PresentOptions, success: (result: Result[]) => void, error: (error: AdvancedImagepickerError) => void): void;

  }

}
