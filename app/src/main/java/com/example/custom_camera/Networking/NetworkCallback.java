package com.example.custom_camera.Networking;

import com.example.custom_camera.Networking.Models.UserTokens;
import org.json.JSONObject;

public interface NetworkCallback {
    void authenticateTokens(UserTokens userTokens);
}
