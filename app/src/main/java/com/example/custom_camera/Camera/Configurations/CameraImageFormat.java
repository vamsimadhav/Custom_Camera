package com.example.custom_camera.Camera.Configurations;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CameraImageFormat {

    public static final int FORMAT_JPEG = 123;
    public static final int FORMAT_PNG = 234;
    public static final int FORMAT_WEBP = 345;

    private CameraImageFormat() {
        throw new RuntimeException("Cannot initialize CameraImageFormat.");
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FORMAT_JPEG, FORMAT_PNG, FORMAT_WEBP})
    public @interface SupportedImageFormat {
    }
}
