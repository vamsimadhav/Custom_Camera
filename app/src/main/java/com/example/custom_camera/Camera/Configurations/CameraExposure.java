package com.example.custom_camera.Camera.Configurations;

public final class CameraExposure {
    public static final int MIN_EXPOSURE = -12;
    public static final int MAX_EXPOSURE = 12;
    public static final int DEFAULT_EXPOSURE = 0;

    private CameraExposure() {
        throw new RuntimeException("Cannot initialize this class.");
    }
}
