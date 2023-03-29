package com.example.custom_camera.Camera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.Manifest;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import com.example.custom_camera.Camera.Configurations.CameraFacing;
import com.example.custom_camera.Camera.Helper.HiddenCameraUtils;
import com.example.custom_camera.Camera.Interface.CameraUtilCallback;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.Camera.Model.CameraError;

public abstract class HiddenCameraService extends Service implements CameraUtilCallback {

    private WindowManager mWindowManager;
    private CameraPreview mCameraPreview;

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    @RequiresPermission(allOf = {Manifest.permission.CAMERA, Manifest.permission.SYSTEM_ALERT_WINDOW})
    protected void startCamera(CameraCharacteristics cameraConfig) {

        if (!HiddenCameraUtils.canOverDrawOtherApps(this)) {    //Check if the draw over other app permission is available.

            onCameraError(CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION);
        } else if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) { //check if the camera permission is available

            //Throw error if the camera permission not available
            onCameraError(CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE);
        } else if (cameraConfig.getFacing() == CameraFacing.FRONT_FACING_CAMERA
                && !HiddenCameraUtils.isFrontCameraAvailable(this)) {   //Check if for the front camera

            onCameraError(CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA);
        } else {

            //Add the camera preview surface to the root of the activity view.
            if (mCameraPreview == null) mCameraPreview = addPreView();
            mCameraPreview.startCameraInternal(cameraConfig);
        }
    }

    protected void takePicture() {
        if (mCameraPreview != null) {
            if (mCameraPreview.isSafeToTakePictureInternal()) {
                mCameraPreview.takePictureInternal();
            }
        } else {
            throw new RuntimeException("Background camera not initialized. Call startCamera() to initialize the camera.");
        }
    }

    protected void stopCamera() {
        if (mCameraPreview != null) {
            mWindowManager.removeView(mCameraPreview);
            mCameraPreview.stopPreviewAndFreeCamera();
        }
    }

    private CameraPreview addPreView() {
        //create fake camera view
        CameraPreview cameraSourceCameraPreview = new CameraPreview(this, this);
        cameraSourceCameraPreview.setLayoutParams(new ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(1, 1,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY :
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        mWindowManager.addView(cameraSourceCameraPreview, params);
        return cameraSourceCameraPreview;
    }
}
