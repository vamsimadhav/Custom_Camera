package com.example.custom_camera.Networking;

import com.example.custom_camera.Networking.Models.User;
import com.example.custom_camera.Networking.Models.UserUpdate;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APICaller {

    @FormUrlEncoded
    @POST("api/v1/auth/sign_in")
    Call<ResponseBody> getSignInInfo(@Field("email") String email, @Field("password") String password);

    @POST("api/v1/tests")
    Call<ResponseBody> sendImageData(
            @Header("Content-Type") String contentType,
            @Header("access-token") String accessToken,
            @Header("uid") String uid,
            @Header("client") String client,
            @Part("test[done_date]") RequestBody doneDate,
            @Part MultipartBody.Part image,
            @Part("test[batch_qr_code]") RequestBody batchQrCode,
            @Part("test[reason]") RequestBody reason,
            @Part("test[failure]") RequestBody failure
    );
}
