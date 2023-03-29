package com.example.custom_camera.Networking;

import com.example.custom_camera.Networking.Models.UserTokens;

public interface NetworkCallback {
    void authenticateTokens(UserTokens userTokens);
}
