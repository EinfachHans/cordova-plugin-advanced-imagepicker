package de.einfachhans.AdvancedImagePicker;

public enum AdvancedImagePickerErrorCodes {
    UnsupportedAction(1),
    WrongJsonObject(2),
    PickerCanceled(3),
    UnknownError(10);

    public final int value;

    AdvancedImagePickerErrorCodes(int value) {
        this.value = value;
    }
}
