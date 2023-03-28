package com.example.custom_camera.Networking;

import com.example.custom_camera.Networking.Models.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APICaller {

    @FormUrlEncoded
    @POST("api/v1/auth/sign_in")
    Call<User> getSignInInfo(@Field("email") String email, @Field("password") String password);
}
