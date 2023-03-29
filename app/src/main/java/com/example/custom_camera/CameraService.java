package com.example.custom_camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.example.custom_camera.Camera.Helper.HiddenCameraUtils;
import com.example.custom_camera.Camera.HiddenCameraService;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.Camera.Model.CameraError;

import java.io.File;

public class CameraService extends HiddenCameraService {

    private String defaultImagePath;
    private boolean checkData;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
//                CameraCharacteristics cameraConfig = intent.getParcelableExtra("cameraConfig");
//                checkData = intent.getBooleanExtra("uploadImage",false);
                int exposure = intent.getIntExtra("exposure",0);
                int iso = intent.getIntExtra("iso",100);
                int facing = intent.getIntExtra("facing",0);
                int focus = intent.getIntExtra("focus", 0);
                int imageFormat = intent.getIntExtra("imageFormat",123);
                int resolution = intent.getIntExtra("resolution",5678);
                int rotation = intent.getIntExtra("rotation",0);
                CameraCharacteristics cameraConfig = new CameraCharacteristics()
                        .getBuilder(this)
                        .setCameraFacing(facing)
                        .setCameraResolution(resolution)
                        .setImageFormat(imageFormat)
                        .setImageRotation(rotation)
                        .setCameraFocus(focus)
                        .setCameraExposure(exposure)
                        .setCameraIso(iso)
                        .build();
                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Capturing image.", Toast.LENGTH_SHORT).show();

                        takePicture();
                    }
                }, 2000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {

    }

    @Override
    public void onCameraError(int errorCodes) {
        switch (errorCodes) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
    }

    @Override
    public void onSaveCompletion(boolean isSaved) {
        if (isSaved) {
            Intent intent = new Intent("picture_saved");
            sendBroadcast(intent);
        }
        stopSelf();
    }

    @Override
    public void allImageSaved(boolean b) {
        if (b) {
            Intent a = new Intent("all_saved");
            a.putExtra("imagePath",defaultImagePath);
            sendBroadcast(a);
        }

    }
}
