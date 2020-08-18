declare module 'cordova-plugin-advanced-imagepicker' {

  interface Result {
    type: 'image' | 'video';
    isBase64: boolean;
    src: string;
  }

}
