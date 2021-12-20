package de.einfachhans.AdvancedImagePicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class AdvancedImagePicker extends CordovaPlugin {

    private CallbackContext _callbackContext;

    private int galleryImageCount = 0;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this._callbackContext = callbackContext;

        try {
            if (action.equals("present")) {
                this.presentFullScreen(args);
                return true;
            } else {
                returnError(AdvancedImagePickerErrorCodes.UnsupportedAction);
                return false;
            }
        } catch (JSONException exception) {
            returnError(AdvancedImagePickerErrorCodes.WrongJsonObject);
        } catch (Exception exception) {
            returnError(AdvancedImagePickerErrorCodes.UnknownError, exception.getMessage());
        }

        return true;
    }

    private void presentFullScreen(JSONArray args) throws JSONException {
        JSONObject options = args.getJSONObject(0);
        String mediaType = options.optString("mediaType", "IMAGE");
        boolean showCameraTile = options.optBoolean("showCameraTile", true);
        String scrollIndicatorDateFormat = options.optString("scrollIndicatorDateFormat");
        boolean showTitle = options.optBoolean("showTitle", true);
        String title = options.optString("title");
        boolean zoomIndicator = options.optBoolean("zoomIndicator", true);
        int min = options.optInt("min");
        String defaultMinCountMessage = "You need to select a minimum of " + (min == 1 ? "one picture" : min + " pictures");
        String minCountMessage = options.optString("minCountMessage", defaultMinCountMessage);
        int max = options.optInt("max");
        String defaultMaxCountMessage = "You can select a maximum of " + max + " pictures";
        String maxCountMessage = options.optString("maxCountMessage", defaultMaxCountMessage);
        String buttonText = options.optString("buttonText");
        boolean asDropdown = options.optBoolean("asDropdown");
        boolean asBase64 = options.optBoolean("asBase64");
        boolean asJpeg = options.optBoolean("asJpeg");
        int width = options.optInt("width", 1024);
        int height = options.optInt("height", 1024);

        if (min < 0 || max < 0) {
            this.returnError(AdvancedImagePickerErrorCodes.WrongJsonObject, "Min and Max can not be less then zero.");
            return;
        }

        if (max != 0 && max < min) {
            this.returnError(AdvancedImagePickerErrorCodes.WrongJsonObject, "Max can not be smaller than Min.");
            return;
        }

        TedImagePicker.Builder builder = TedImagePicker.with(this.cordova.getContext())
                .showCameraTile(showCameraTile)
                .showTitle(showTitle)
                .zoomIndicator(zoomIndicator)
                .errorListener(error -> {
                    this.returnError(AdvancedImagePickerErrorCodes.UnknownError, error.getMessage());
                });

        if (!scrollIndicatorDateFormat.equals("")) {
            builder.scrollIndicatorDateFormat(scrollIndicatorDateFormat);
        }
        if (!title.equals("")) {
            builder.title(title);
        }
        if (!buttonText.equals("")) {
            builder.buttonText(buttonText);
        }
        if (asDropdown) {
            builder.dropDownAlbum();
        }
        String type = "image";
        if (mediaType.equals("VIDEO")) {
            builder.video();
            type = "video";
        }

        if (max == 1) {
            String finalType = type;
            builder.start(result -> {
                this.handleResult(result, asBase64, finalType, asJpeg, width, height);
            });
        } else {
            if (min > 0) {
                builder.min(min, minCountMessage);
            }
            if (max > 0) {
                builder.max(max, maxCountMessage);
            }

            String finalType1 = type;
            builder.startMultiImage(result -> {
                this.handleResult(result, asBase64, finalType1, asJpeg, width, height);
            });
        }
    }

    private void handleResult(Uri uri, boolean asBase64, String type, boolean asJpeg, int width, int height) {
        List<Uri> list = new ArrayList<>();
        list.add(uri);
        this.handleResult(list, asBase64, type, asJpeg, width, height);
    }

    private void handleResult(List<? extends Uri> uris, boolean asBase64, String type, boolean asJpeg, int width, int height) {

        CallbackContext cb = this._callbackContext;

        Executors.newSingleThreadExecutor().execute(
                () -> {
                    JSONArray result = new JSONArray();
                    PluginResult processingResult = new PluginResult(
                        PluginResult.Status.OK,
                        "processing"
                    );
                    processingResult.setKeepCallback(true);
                    cb.sendPluginResult(processingResult);

                    for (Uri uri : uris) {
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("type", type);
                        resultMap.put("isBase64", asBase64);
                        if (asBase64) {
                            try {
                                resultMap.put("src", type.equals("video") ? this.encodeVideo(uri) : this.encodeImage(uri, asJpeg, width, height));
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.returnError(AdvancedImagePickerErrorCodes.UnknownError, e.getMessage());
                                return;
                            }
                        } else {
                            //Path tempFile = Files.createTempFile(null, null);
                            try {
                                resultMap.put("src", encodeImageTempFile(uri, asJpeg, width, height));
                            } catch (IOException e) {
                                e.printStackTrace();
                                cb.error(e.getMessage());
                                return;
                            }
                        }
                        result.put(new JSONObject(resultMap));
                    }
                    cb.success(result);
                }
        );

    }

    private String encodeVideo(Uri uri) throws IOException {
        final InputStream videoStream = this.cordova.getContext().getContentResolver().openInputStream(uri);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = videoStream.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        bytes = output.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private String encodeImage(Uri uri, boolean asJpeg, int width, int height) throws FileNotFoundException {
        final InputStream imageStream = this.cordova.getContext().getContentResolver().openInputStream(uri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        return encodeImage(selectedImage, asJpeg, width, height);
    }

    private String encodeImageTempFile(Uri uri, boolean asJpeg, int width, int height) throws FileNotFoundException, IOException {
        final InputStream imageStream = this.cordova.getContext().getContentResolver().openInputStream(uri);
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        int finalWidth = width;
        int finalHeight = height;
        float widthRatio = (float)width / (float)selectedImage.getWidth();
        float heightRatio = (float)height / (float)selectedImage.getHeight();

        if(widthRatio > heightRatio) {
            finalWidth = (int)(selectedImage.getWidth() * heightRatio);
            finalHeight = (int)(selectedImage.getHeight() * heightRatio);
        } else {
            finalWidth = (int)(selectedImage.getWidth() * widthRatio);
            finalHeight = (int)(selectedImage.getHeight() * widthRatio);
        }


        selectedImage = Bitmap.createScaledBitmap(
            selectedImage,
            finalWidth,
            finalHeight,
            true
        );
        galleryImageCount++;
        File file = new File(
                this.cordova.getContext().getCacheDir(),
                String.format(
                        "gallery-%d.%s",
                        galleryImageCount,
                        asJpeg ? "jpg" : "png"
                )
            );
        if(file.exists()) file.delete();

        FileOutputStream outStream = new FileOutputStream(file);
        if (asJpeg) {
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
        } else {
            selectedImage.compress(Bitmap.CompressFormat.PNG, 80, outStream);
        }

        outStream.flush();
        outStream.close();

        return Uri.fromFile(file).toString();
    }

    private String encodeImage(Bitmap bm, boolean asJpeg, int width, int height) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        /*
let size = image.size

        let widthRatio  = targetSize.width  / size.width
        let heightRatio = targetSize.height / size.height

        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio, height: size.height * widthRatio)
        }         */

        int finalWidth = width;
        int finalHeight = height;
        float widthRatio = (float)width / (float)bm.getWidth();
        float heightRatio = (float)height / (float)bm.getHeight();

        if(widthRatio > heightRatio) {
            finalWidth = (int)(bm.getWidth() * heightRatio);
            finalHeight = (int)(bm.getHeight() * heightRatio);
        } else {
            finalWidth = (int)(bm.getWidth() * widthRatio);
            finalHeight = (int)(bm.getHeight() * widthRatio);
        }


        bm = Bitmap.createScaledBitmap(
                bm,
                finalWidth,
                finalHeight,
                true
        );
        if (asJpeg) {
            bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        } else {
            bm.compress(Bitmap.CompressFormat.PNG, 80, baos);
        }
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private void returnError(AdvancedImagePickerErrorCodes errorCode) {
        returnError(errorCode, null);
    }

    private void returnError(AdvancedImagePickerErrorCodes errorCode, String message) {
        if (_callbackContext != null) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("code", errorCode.value);
            resultMap.put("message", message == null ? "" : message);
            _callbackContext.error(new JSONObject(resultMap));
            _callbackContext = null;
        }
    }
}