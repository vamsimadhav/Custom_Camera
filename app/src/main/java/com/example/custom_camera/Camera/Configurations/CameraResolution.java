package com.example.custom_camera.Camera.Configurations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CameraResolution {
    public static final int HIGH_RESOLUTION = 4567;
    public static final int MEDIUM_RESOLUTION = 5678;
    public static final int LOW_RESOLUTION = 6789;

    private CameraResolution() {
        throw new RuntimeException("Cannot initiate CameraResolution.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HIGH_RESOLUTION, MEDIUM_RESOLUTION, LOW_RESOLUTION})
    public @interface SupportedResolution {
    }
}
