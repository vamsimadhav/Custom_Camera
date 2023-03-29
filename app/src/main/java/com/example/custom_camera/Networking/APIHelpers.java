package com.example.custom_camera.Networking;

import android.util.Log;

import com.example.custom_camera.Networking.Models.User;
import com.example.custom_camera.Networking.Models.UserTokens;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        Call<ResponseBody> call = apiCaller.getSignInInfo("amit_4@test.com","12345678");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() == null) {
                        if (response.errorBody() != null) {
                            Log.d("AUTHENTICATE API", response.errorBody().string());
                        }
                        return;
                    }

//                    String responseText = response.body().toString();
//                    JSONObject jsonResponse = new JSONObject(responseText);
                    UserTokens userTokens = new UserTokens(
                            response.headers().get("Content-Type"),
                            response.headers().get("access-token"),
                            response.headers().get("uid"),
                            response.headers().get("client"));

                    networkCallback.authenticateTokens(userTokens);

                } catch (IOException e) {
                    Log.d("AUTHENTICATE API ERROR", e.toString());
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
                    try {
                        if (response.body() == null) {
                            if (response.errorBody() != null) {
                                Log.d("UPLOAD API", response.errorBody().string());
                            }
                            Log.d("UPLOAD RESPONSE", response.body().toString());
                        }

                    } catch (IOException e) {
                        Log.d("UPLOAD API ERROR", e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }


    }
}
