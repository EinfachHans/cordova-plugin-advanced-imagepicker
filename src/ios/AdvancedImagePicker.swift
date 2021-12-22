import YPImagePicker

@objc(AdvancedImagePicker) class AdvancedImagePicker : CDVPlugin  {

    var _callbackId: String?
    var OWN_PREFIX: String?

    @objc(pluginInitialize)
    override func pluginInitialize() {
        super.pluginInitialize()
        self.OWN_PREFIX = "advanced_image_picker_";
    }

    @objc(present:)
    func present(command: CDVInvokedUrlCommand) {
        _callbackId = command.callbackId;
        let options = command.argument(at: 0) as? NSDictionary;
        if(options == nil) {
            self.returnError(error: ErrorCodes.WrongJsonObject, message: "The first Argument must be the Configuration");
            return;
        }

        let mediaType = options?.value(forKey: "mediaType") as? String ?? "IMAGE";
        let startOnScreen = options?.value(forKey: "startOnScreen") as? String ?? "LIBRARY";
        let showCameraTile = options?.value(forKey: "showCameraTile") as? Bool ?? true;
        let min = options?.value(forKey: "min") as? NSInteger ?? 1;
        let max = options?.value(forKey: "max") as? NSInteger ?? 1;
        let defaultMaxCountMessage = "You can select a maximum of " + String(max) + " files";
        let maxCountMessage = options?.value(forKey: "maxCountMessage") as? String ?? defaultMaxCountMessage;
        let buttonText = options?.value(forKey: "buttonText") as? String ?? "";
        let asBase64 = options?.value(forKey: "asBase64") as? Bool ?? false;
        let videoCompression = options?.value(forKey: "videoCompression") as? String ?? "AVAssetExportPresetHighestQuality";
        let asJpeg = options?.value(forKey: "asJpeg") as? Bool ?? true;
        let width = options?.value(forKey: "width") as? Int ?? 1024;
        let height = options?.value(forKey: "height") as? Int ?? 1024;
        
        let recordingTimeLimit = options?.value(forKey: "recordingTimeLimit") as? Double ?? 60.0;
        let libraryTimeLimit = options?.value(forKey: "libraryTimeLimit") as? Double ?? 60.0;
        let minimumTimeLimit = options?.value(forKey: "minimumTimeLimit") as? Double ?? 3.0;

        if(max < 0 || min < 0) {
            self.returnError(error: ErrorCodes.WrongJsonObject, message: "Min and Max can not be less then zero.");
            return;
        }

        if(max < min) {
            self.returnError(error: ErrorCodes.WrongJsonObject, message: "Max can not be smaller than Min.");
            return;
        }

        var config = YPImagePickerConfiguration();
        config.onlySquareImagesFromCamera = false;
        config.showsPhotoFilters = false;
        config.showsVideoTrimmer = false;
        config.shouldSaveNewPicturesToAlbum = false;
        config.albumName = Bundle.main.infoDictionary![kCFBundleNameKey as String] as! String;
        config.library.isSquareByDefault = false;
        config.library.itemOverlayType = .none;
        config.library.skipSelectionsGallery = true;
        config.library.preSelectItemOnMultipleSelection = false;
        config.video.compression = videoCompression;
        config.video.recordingTimeLimit = recordingTimeLimit;
        config.video.libraryTimeLimit = libraryTimeLimit;
        config.video.minimumTimeLimit = minimumTimeLimit;

        if(startOnScreen == "IMAGE") {
            config.startOnScreen = .photo;
        } else if(startOnScreen == "VIDEO") {
            config.startOnScreen = .video;
        } else {
            config.startOnScreen = .library;
        }

        var screens: [YPPickerScreen] = [.library];
        if(showCameraTile) {
            if(mediaType != "VIDEO") {
                screens.append(.photo);
            }
            if(mediaType != "IMAGE") {
                screens.append(.video);
            }
        }
        config.screens = screens;
        config.library.defaultMultipleSelection = (max > 1);
        if(mediaType == "IMAGE") {
            config.library.mediaType = YPlibraryMediaType.photo
        } else if(mediaType == "VIDEO") {
            config.library.mediaType = YPlibraryMediaType.video
        } else {
            config.library.mediaType = YPlibraryMediaType.photoAndVideo
        }
        config.library.minNumberOfItems = min;
        config.library.maxNumberOfItems = max;
        
        config.wordings.warningMaxItemsLimit = maxCountMessage;
        if(buttonText != "") {
            config.wordings.next = buttonText;
        }

        let picker = YPImagePicker(configuration: config);

        if #available(iOS 15.0, *) {
            let navBarAppearance = UINavigationBarAppearance()
            navBarAppearance.configureWithOpaqueBackground()
            picker.navigationBar.scrollEdgeAppearance = navBarAppearance
        }

        picker.didFinishPicking {items, cancelled in
            if(cancelled) {
                self.returnError(error: ErrorCodes.PickerCanceled)
            } else if(items.count > 0) {
                self.handleResult(items: items, asBase64: asBase64, asJpeg: asJpeg, width: width, height: height);
            }
            picker.dismiss(animated: true, completion: nil);
        }

        self.viewController.present(picker, animated: true, completion: nil);
    }

    func handleResult(items: [YPMediaItem], asBase64: Bool, asJpeg: Bool, width: Int, height: Int) {
        
        let resultCBID = self._callbackId;
        let dispatchQueue = DispatchQueue(label: "PhotoProcessing", qos: .default);
        
        dispatchQueue.async {
            
            var result:CDVPluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "processing");
            result.setKeepCallbackAs(true);
            self.commandDelegate.send(result, callbackId: resultCBID);
            
            var array = [] as Array;
            for item in items {

                switch item {
                case .photo(let photo):
                    //
                    let encodedImage = self.encodeImage(image: photo.image, asBase64: asBase64, asJpeg: asJpeg, width:width, height:height);
                    array.append([
                        "type": "image",
                        "isBase64": asBase64,
                        "src": encodedImage
                    ]);
                    break;
                case .video(let video):
                    var resultSrc:String;
                    if(asBase64) {
                        resultSrc = self.encodeVideo(url: video.url);
                        if(resultSrc == "") {
                            self.returnError(error: ErrorCodes.UnknownError, message: "Failed to encode Video")
                            return;
                        }
                    } else {
                        resultSrc = video.url.absoluteString;
                    }
                    array.append([
                        "type": "video",
                        "isBase64": asBase64,
                        "src": resultSrc
                    ]);
                    break;
                }

            }
            
            result = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: array);
            self.commandDelegate.send(result, callbackId: resultCBID);
            
        }
    }
    
    func resizeImage(image: UIImage, targetSize: CGSize) -> UIImage? {
        let size = image.size
        
        let widthRatio  = targetSize.width  / size.width
        let heightRatio = targetSize.height / size.height
        
        // Figure out what our orientation is, and use that to form the rectangle
        var newSize: CGSize
        if(widthRatio > heightRatio) {
            newSize = CGSize(width: size.width * heightRatio, height: size.height * heightRatio)
        } else {
            newSize = CGSize(width: size.width * widthRatio, height: size.height * widthRatio)
        }
        
        // This is the rect that we've calculated out and this is what is actually used below
        let rect = CGRect(origin: .zero, size: newSize)
        
        // Actually do the resizing to the rect using the ImageContext stuff
        UIGraphicsBeginImageContextWithOptions(newSize, false, 1.0)
        image.draw(in: rect)
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage
    }

    func encodeImage(image: UIImage, asBase64: Bool, asJpeg: Bool, width: Int, height: Int) -> String {
        let imageData: NSData;
        let _image = self.resizeImage(image: image, targetSize: CGSize(width: width, height: height))!;
        
        if(asJpeg) {
            imageData = UIImageJPEGRepresentation(
                _image,
                0.8
            )! as NSData;
        } else {
            imageData = UIImagePNGRepresentation(_image)! as NSData;
        }
        
        if(asBase64) {
            return imageData.base64EncodedString();
        } else {
            let filePath = self.tempFilePath(ext: asJpeg ? "jpg": "png");
            do {
            
                try imageData.write(to: filePath, options: .atomic);
                return filePath.absoluteString;
            } catch {
                return error.localizedDescription;
            }
        }
    }

    func tempFilePath(ext: String = "png") -> URL {
        let filename: String = self.OWN_PREFIX! + UUID().uuidString;
        var contentUrl = URL(fileURLWithPath: NSTemporaryDirectory());
        contentUrl.appendPathComponent(filename);
        contentUrl.appendPathExtension(ext);
        return contentUrl;
    }

    func encodeVideo(url: URL) -> String {
        do {
            let fileData = try Data.init(contentsOf: url)
            return fileData.base64EncodedString();
        } catch {
            return "";
        }
    }

    func returnError(callbackId: String?, error: ErrorCodes, message: String = "") {
        if(callbackId != nil) {
            let result:CDVPluginResult = CDVPluginResult(
                status: CDVCommandStatus_ERROR, messageAs: [
                    "code": error.rawValue,
                    "message": message
            ]);
            self.commandDelegate.send(result, callbackId: callbackId)
        }
    }

    func returnError(error: ErrorCodes, message: String = "") {
        self.returnError(callbackId: _callbackId, error: error, message: message)
        _callbackId = nil;
    }

    @objc(cleanup:)
    func cleanup(command: CDVInvokedUrlCommand) {
        do {
            let tmpFiles: [String] = try FileManager().contentsOfDirectory(atPath: NSTemporaryDirectory());
            for tmpFile in tmpFiles {
                // only delete files from this plugin:
                if(tmpFile.hasPrefix(self.OWN_PREFIX!)) {
                    try FileManager().removeItem(atPath: NSTemporaryDirectory() + tmpFile)
                }
            }
        } catch {
            returnError(callbackId: command.callbackId, error: ErrorCodes.UnknownError, message: error.localizedDescription);
            return;
        }
        let result:CDVPluginResult = CDVPluginResult(status: CDVCommandStatus_OK);
        self.commandDelegate.send(result, callbackId: command.callbackId);
    }
}

enum ErrorCodes:NSNumber {
    case UnsupportedAction = 1
    case WrongJsonObject = 2
    case PickerCanceled = 3
    case UnknownError = 10
}
