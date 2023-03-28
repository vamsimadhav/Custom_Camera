package com.example.custom_camera.Networking.Models;

public class User {
    private int id;
    private String email;
    private boolean onboarded;

    public User(int id, String email, boolean onboarded) {
        this.id = id;
        this.email = email;
        this.onboarded = onboarded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnboarded() {
        return onboarded;
    }

    public void setOnboarded(boolean onboarded) {
        this.onboarded = onboarded;
    }
}
