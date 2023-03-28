package com.example.custom_camera.Camera.Configurations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CameraExposure {
    public static final int MIN_EXPOSURE = -12;
    public static final int MAX_EXPOSURE = 12;
    public static final int DEFAULT_EXPOSURE = 0;

    private CameraExposure() {
        throw new RuntimeException("Cannot initialize this class.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MIN_EXPOSURE, MAX_EXPOSURE, DEFAULT_EXPOSURE,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,1,2,3,4,5,6,7,8,9,10,11})
    public @interface SupportedExposure {
    }
}
