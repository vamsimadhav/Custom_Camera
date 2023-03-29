package com.example.custom_camera.Networking;

import android.util.Log;

import com.example.custom_camera.Networking.Models.User;
import com.example.custom_camera.Networking.Models.UserTokens;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIHelpers {
    public static void authenticateApp(NetworkCallback networkCallback) {
        APICaller apiCaller = RetrofitClient.getInstance().create(APICaller.class);
        Call<User> call = apiCaller.getSignInInfo("amit_4@test.com","12345678");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                UserTokens userTokens = new UserTokens(
                        response.headers().get("Content-Type"),
                        response.headers().get("access-token"),
                        response.headers().get("uid"),
                        response.headers().get("client")
                );
                networkCallback.authenticateTokens(userTokens);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public static void sendImageToServer(UserTokens userTokens,String currentDate,String savedImage) {
        APICaller apiCaller = RetrofitClient.getInstance().create(APICaller.class);

        // Prepare Body

        Call<ResponseBody> call =null;
        try {
            RequestBody doneDate = RequestBody.create(MediaType.parse("text/plain"), currentDate);
            RequestBody batchQrCode = RequestBody.create(MediaType.parse("text/plain"), "AAO");
            RequestBody reason = RequestBody.create(MediaType.parse("text/plain"), "NA");
            RequestBody failure = RequestBody.create(MediaType.parse("text/plain"), "false");
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("test[images_attributes][][pic]", savedImage);
            call = apiCaller.sendImageData(userTokens.getContentType(),userTokens.getAccessToken(),userTokens.getUid(),userTokens.getClient(),doneDate,imagePart,batchQrCode,reason,failure);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (call != null) {
            call .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.e("IMAGE SENT",response.body() + "");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }


    }
}
