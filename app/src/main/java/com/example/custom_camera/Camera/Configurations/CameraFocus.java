package com.example.custom_camera.Camera.Configurations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CameraFocus {

    public static final int AUTO = 0;
    public static final int CONTINUOUS_PICTURE = 1;
    public static final int NO_FOCUS = 2;

    private CameraFocus() {
        throw new RuntimeException("Cannot initialize this class.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({AUTO, CONTINUOUS_PICTURE, NO_FOCUS})
    public @interface SupportedCameraFocus {
    }
}
