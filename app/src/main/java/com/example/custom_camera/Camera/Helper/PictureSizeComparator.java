package com.example.custom_camera.Camera.Helper;

import java.util.Comparator;
import android.hardware.Camera;

public class PictureSizeComparator implements Comparator<Camera.Size> {

    public int compare(Camera.Size a, Camera.Size b) {
        return (b.height * b.width) - (a.height * a.width);
    }
}