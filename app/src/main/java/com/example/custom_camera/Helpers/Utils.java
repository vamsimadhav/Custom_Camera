package com.example.custom_camera.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Patterns;

import com.example.custom_camera.Camera.Configurations.CameraFacing;
import com.example.custom_camera.Camera.Configurations.CameraFocus;
import com.example.custom_camera.Camera.Configurations.CameraImageFormat;
import com.example.custom_camera.Camera.Configurations.CameraResolution;
import com.example.custom_camera.Camera.Configurations.CameraRotation;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class Utils {
    public static void isNameValid(TextInputLayout nameInput, String name) {
        if (name.isEmpty()) {
            nameInput.setError("Name cannot be empty");
        } else if (name.length() < 2) {
            nameInput.setError("Name must be at least 2 characters long");
        } else {
            nameInput.setError(null);
        }
    }

    public static void isEmailValid(TextInputLayout emailInput, String email) {
        if (email.isEmpty()) {
            emailInput.setError("Email cannot be empty");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Please enter a valid email address");
        } else {
            emailInput.setError(null);
        }
    }

    public static Bitmap getBitmapFromPath(Context context, ArrayList<CameraCharacteristics> cameraCharacteristics) {
//        String path = cameraCharacteristics.get(0).getImageFile().getAbsolutePath();
//        File storageDir = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES);
//        String fileName = storageDir.getAbsolutePath() + path.substring(path.lastIndexOf('/') + 1);
//        File file = new File(fileName);
//        if (file.exists()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//            return bitmap;
//        }
//        return null;
        String root = cameraCharacteristics.get(13).getImageFile().getAbsolutePath();
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File storageDir = new File(path + "/HiddenCamera");
        String fileName = storageDir.getAbsolutePath() + "/" + root.substring(root.lastIndexOf('/') + 1);
        File file = new File(fileName);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            return bitmap;
        }
        return null;

    }

    public static String convertBitMapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64String;
    }

    public static ArrayList<CameraCharacteristics> buildParameters(Context context) {
        ArrayList<CameraCharacteristics> list = new ArrayList<>();

        for (int i= -12; i<= 12; i++) {
            CameraCharacteristics characteristics = new CameraCharacteristics()
                    .getBuilder(context)
                    .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .setImageRotation(CameraRotation.ROTATION_90)
                    .setCameraFocus(CameraFocus.AUTO)
                    .setCameraExposure(i)
                    .build();

            list.add(characteristics);
        }
        return list;
    }
}
