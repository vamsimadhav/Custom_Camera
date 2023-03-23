package com.example.custom_camera.Helpers;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

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
}
