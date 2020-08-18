declare module 'cordova-plugin-advanced-imagepicker' {

  /**
   * Used for every Plugin Error Callback
   */
  interface AdvancedImagepickerError {
    /**
     * One of the AdvancedImagepickerErrorCodes
     */
    code: number;

    /**
     * If available some more info (mostly exception message)
     */
    message: string;
  }

}
