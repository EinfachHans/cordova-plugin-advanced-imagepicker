declare module 'cordova-plugin-advanced-imagepicker' {

  interface PresentOptions {
    /**
     * Which Media Types are allowed to be selected
     * default: "IMAGE"
     */
    mediaType?: 'IMAGE' | 'VIDEO' | 'ALL';
    /**
     * Show possibility to take via Camera
     * default: true
     */
    showCameraTile?: boolean;
    /**
     * On which Screen the Picker should be started (iOS only)
     * default: "LIBRARY"
     */
    startOnScreen?: 'LIBRARY' | 'IMAGE' | 'VIDEO';
    /**
     * Date format of the Scroll Indicator (Android only)
     * default: "YYYY.MM"
     */
    scrollIndicatorDateFormat?: string;
    /**
     * Show Title (Android only)
     * default: true
     */
    showTitle?: boolean;
    /**
     * Customize the Title (Android only)
     * default: "Select Image"
     */
    title?: string;
    /**
     * Show the zoomIndicator at the Images (Android only)
     * default: true
     */
    zoomIndicator?: boolean;
    /**
     * Min Count of files to be selected
     * default: 0 (android), 1 (iOS)
     */
    min?: number;
    /**
     * Message to be shown if min Count not reached (Android only)
     * default: "You need to select a minimum of ... pictures")"
     */
    minCountMessage?: string;
    /**
     * Max Count of files can selected
     * default: 0 (android), 1 (iOS)
     */
    max?: number;
    /**
     * Message to be shown if max Count is reached
     * default: "You can select a maximum of ... pictures"
     */
    maxCountMessage?: string;
    /**
     * Change Done Button Text
     */
    buttonText?: string;
    /**
     * Show Library as Dropdown (Android only)
     * default: false
     */
    asDropdown?: boolean;
    /**
     * Return the Result as base64
     * default: false
     */
    asBase64?: boolean;
    /**
     * Return the Image as JPEG
     * default: false
     */
    asJpeg?: boolean;
    videoCompression?: string;
  }

}
