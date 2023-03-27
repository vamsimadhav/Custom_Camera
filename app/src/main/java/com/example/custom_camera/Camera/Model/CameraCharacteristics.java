package com.example.custom_camera.Camera.Model;

import android.os.Environment;
import android.content.Context;
import android.hardware.Camera;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.custom_camera.Camera.Configurations.*;

import java.io.File;

public class CameraCharacteristics {
    private Context mContext;

    @CameraResolution.SupportedResolution
    private int mResolution = CameraResolution.MEDIUM_RESOLUTION;


    @CameraFacing.SupportedCameraFacing
    private int mFacing = CameraFacing.REAR_FACING_CAMERA;

    @CameraImageFormat.SupportedImageFormat
    private int mImageFormat = CameraImageFormat.FORMAT_JPEG;

    @CameraRotation.SupportedRotation
    private int mImageRotation = CameraRotation.ROTATION_0;

    @CameraFocus.SupportedCameraFocus
    private int mCameraFocus = CameraFocus.AUTO;

//    @CameraExposure.SupportedExposure
    private int mCameraExposure = CameraExposure.DEFAULT_EXPOSURE;

    private int mCameraIso = CameraIso.DEFAULT_ISO;

    private File mImageFile;

    public CameraCharacteristics() {
        // Do nothing
    }

    public Builder getBuilder(Context context) {
        mContext = context;
        return new Builder();
    }

    @CameraResolution.SupportedResolution
    public int getResolution() {
        return mResolution;
    }

    @Nullable
    public String getFocusMode() {
        switch (mCameraFocus) {
            case CameraFocus.AUTO:
                return Camera.Parameters.FOCUS_MODE_AUTO;
            case CameraFocus.CONTINUOUS_PICTURE:
                return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
            case CameraFocus.NO_FOCUS:
                return null;
            default:
                throw new RuntimeException("Invalid camera focus mode.");
        }
    }

    @CameraFacing.SupportedCameraFacing
    public int getFacing() {
        return mFacing;
    }

    @CameraImageFormat.SupportedImageFormat
    public int getImageFormat() {
        return mImageFormat;
    }

//    @CameraExposure.SupportedExposure
public int getCameraExposure() { return mCameraExposure;}

    public int getCameraIso() { return mCameraIso; }

    public File getImageFile() {
        return mImageFile;
    }

    @CameraRotation.SupportedRotation
    public int getImageRotation() {
        return mImageRotation;
    }

    public class Builder {

        public Builder setCameraResolution(@CameraResolution.SupportedResolution int resolution) {

            //Validate input
            if (resolution != CameraResolution.HIGH_RESOLUTION &&
                    resolution != CameraResolution.MEDIUM_RESOLUTION &&
                    resolution != CameraResolution.LOW_RESOLUTION) {
                throw new RuntimeException("Invalid camera resolution.");
            }

            mResolution = resolution;
            return this;
        }

        public Builder setCameraExposure( int exposure) {
            //Validate Input
            if (exposure > CameraExposure.MAX_EXPOSURE || exposure < CameraExposure.MIN_EXPOSURE) {
                throw  new RuntimeException("Invalid Camera Exposure");
            }
            mCameraExposure = exposure;
            return this;
        }

        public Builder setCameraIso(int iso) {
            mCameraIso = iso;
            return this;
        }

        public Builder setCameraFacing(@CameraFacing.SupportedCameraFacing int cameraFacing) {
            //Validate input
            if (cameraFacing != CameraFacing.REAR_FACING_CAMERA &&
                    cameraFacing != CameraFacing.FRONT_FACING_CAMERA) {
                throw new RuntimeException("Invalid camera facing value.");
            }

            mFacing = cameraFacing;
            return this;
        }

        public Builder setCameraFocus(@CameraFocus.SupportedCameraFocus int focusMode) {
            //Validate input
            if (focusMode != CameraFocus.AUTO &&
                    focusMode != CameraFocus.CONTINUOUS_PICTURE &&
                    focusMode != CameraFocus.NO_FOCUS) {
                throw new RuntimeException("Invalid camera focus mode.");
            }

            mCameraFocus = focusMode;
            return this;
        }

        public Builder setImageFormat(@CameraImageFormat.SupportedImageFormat int imageFormat) {
            //Validate input
            if (imageFormat != CameraImageFormat.FORMAT_JPEG &&
                    imageFormat != CameraImageFormat.FORMAT_PNG) {
                throw new RuntimeException("Invalid output image format.");
            }

            mImageFormat = imageFormat;
            return this;
        }

        public Builder setImageRotation(@CameraRotation.SupportedRotation int rotation) {
            //Validate input
            if (rotation != CameraRotation.ROTATION_0
                    && rotation != CameraRotation.ROTATION_90
                    && rotation != CameraRotation.ROTATION_180
                    && rotation != CameraRotation.ROTATION_270) {
                throw new RuntimeException("Invalid image rotation.");
            }

            mImageRotation = rotation;
            return this;
        }

        public Builder setImageFile(File imageFile) {
            mImageFile = imageFile;
            return this;
        }

        public CameraCharacteristics build() {
            if (mImageFile == null) mImageFile = getDefaultStorageFile();
            return CameraCharacteristics.this;
        }

//        @NonNull
//        private File getDefaultStorageFile() {
//            return new File(HiddenCameraUtils.getCacheDir(mContext).getAbsolutePath()
//                    + File.separator
//                    + "IMG_" + System.currentTimeMillis()   //IMG_214515184113123.png
//                    + (mImageFormat == CameraImageFormat.FORMAT_JPEG ? ".jpeg" : ".png"));
//        }

        @NonNull
        private File getDefaultStorageFile() {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/HiddenCamera");
            myDir.mkdirs();
            String fname = "IMG_" + System.currentTimeMillis() + (mImageFormat == CameraImageFormat.FORMAT_JPEG ? ".jpeg" : ".png");
            return new File(myDir, fname);
        }
    }
}
