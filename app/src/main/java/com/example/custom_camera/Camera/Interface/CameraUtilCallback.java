package com.example.custom_camera.Camera.Interface;

import java.io.File;
import androidx.annotation.NonNull;
import com.example.custom_camera.Camera.Model.CameraError;

public interface CameraUtilCallback {
    void onImageCapture(@NonNull File imageFile);
    void onCameraError(@CameraError.CameraErrorCodes int errorCodes);
    void onSaveCompletion(boolean isSaved);
    void allImageSaved(boolean b);
}
