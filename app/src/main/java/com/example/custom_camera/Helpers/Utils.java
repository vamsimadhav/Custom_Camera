package com.example.custom_camera.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;

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

    public static Bitmap getBitmapFromPath(String imagePath) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
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
}
