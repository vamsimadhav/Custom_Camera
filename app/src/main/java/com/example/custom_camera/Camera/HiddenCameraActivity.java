package com.example.custom_camera.Camera;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.widget.RelativeLayout;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import com.example.custom_camera.Camera.Model.CameraError;
import com.example.custom_camera.Camera.Helper.HiddenCameraUtils;
import com.example.custom_camera.Camera.Configurations.CameraFacing;
import com.example.custom_camera.Camera.Interface.CameraUtilCallback;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;

public abstract class HiddenCameraActivity extends AppCompatActivity implements CameraUtilCallback {

    private CameraPreview mCameraPreview;
    private CameraCharacteristics mCachedCameraCharacteristics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Add the camera preview surface to the root of the activity view.
        mCameraPreview = addPreView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCamera();
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    protected void startCamera(CameraCharacteristics cameraCharacteristics) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) { //check if the camera permission is available

            onCameraError(CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE);
        } else if (cameraCharacteristics.getFacing() == CameraFacing.FRONT_FACING_CAMERA
                && !HiddenCameraUtils.isFrontCameraAvailable(this)) {   //Check if for the front camera

            onCameraError(CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA);
        } else {
            mCachedCameraCharacteristics = cameraCharacteristics;
            mCameraPreview.startCameraInternal(cameraCharacteristics);
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

    /**
     * Stop and release the camera forcefully.
     */
    protected void stopCamera() {
        mCachedCameraCharacteristics = null;    //Remove config.
        if (mCameraPreview != null) mCameraPreview.stopPreviewAndFreeCamera();
    }
    private CameraPreview addPreView() {
        //create fake camera view
        CameraPreview cameraSourceCameraPreview = new CameraPreview(this, this);
        cameraSourceCameraPreview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View view = ((ViewGroup) getWindow().getDecorView().getRootView()).getChildAt(0);

        if (view instanceof LinearLayout) {
            LinearLayout linearLayout = (LinearLayout) view;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, 1);
            linearLayout.addView(cameraSourceCameraPreview, params);
        } else if (view instanceof RelativeLayout) {
            RelativeLayout relativeLayout = (RelativeLayout) view;

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(1, 1);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            relativeLayout.addView(cameraSourceCameraPreview, params);
        } else if (view instanceof FrameLayout) {
            FrameLayout frameLayout = (FrameLayout) view;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 1);
            frameLayout.addView(cameraSourceCameraPreview, params);
        } else {
            throw new RuntimeException("Root view of the activity/fragment cannot be other than Linear/Relative/Frame layout");
        }

        return cameraSourceCameraPreview;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mCachedCameraCharacteristics != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            startCamera(mCachedCameraCharacteristics);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraPreview != null) mCameraPreview.stopPreviewAndFreeCamera();
    }
}
