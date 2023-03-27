package com.example.custom_camera.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.custom_camera.Camera.Configurations.CameraFacing;
import com.example.custom_camera.Camera.Configurations.CameraFocus;
import com.example.custom_camera.Camera.Configurations.CameraImageFormat;
import com.example.custom_camera.Camera.Configurations.CameraResolution;
import com.example.custom_camera.Camera.Configurations.CameraRotation;
import com.example.custom_camera.Camera.Helper.HiddenCameraUtils;
import com.example.custom_camera.Camera.HiddenCameraActivity;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.Camera.Model.CameraError;
import com.example.custom_camera.R;

import java.io.File;

public class CameraActivityTwo extends HiddenCameraActivity {

    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    private CameraCharacteristics mCameraCharacteristics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_two);

        mCameraCharacteristics = new CameraCharacteristics()
                .getBuilder(this)
                .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .setImageRotation(CameraRotation.ROTATION_90)
                .setCameraFocus(CameraFocus.AUTO)
                .setCameraIso(100)
                .build();

//        /Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera(mCameraCharacteristics);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQ_CODE_CAMERA_PERMISSION);
        }

//        Take a picture
        findViewById(R.id.capture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take picture using the camera without preview.
                takePicture();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_CAMERA_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(mCameraCharacteristics);
            } else {
                Toast.makeText(this, R.string.error_camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
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
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {

    }

    @Override
    public void onSaveCompletion(boolean isSaved) {
        finish();
    }
}