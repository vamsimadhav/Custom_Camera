package com.example.custom_camera.Networking;

import android.util.Log;

import com.example.custom_camera.Networking.Models.UserTokens;

import java.io.File;
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

    public static void sendImageToServer(UserTokens userTokens, String currentDate, File savedImageFile) {
        APICaller apiCaller = RetrofitClient.getInstance().create(APICaller.class);

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData(
                "test[images_attributes][][pic]",
                "saved_image.jpg",
                RequestBody.create(MediaType.parse("image/jpeg"), savedImageFile)
        );

        RequestBody doneDatePart = RequestBody.create(MediaType.parse("text/plain"), currentDate);
        RequestBody batchQrCodePart = RequestBody.create(MediaType.parse("text/plain"), "AAO");
        RequestBody reasonPart = RequestBody.create(MediaType.parse("text/plain"), "NA");
        RequestBody failurePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(false));

        Call<ResponseBody> call = apiCaller.sendImageData(
                "application/json",
                userTokens.getAccessToken(),
                userTokens.getUid(),
                userTokens.getClient(),
                doneDatePart,
                imagePart,
                batchQrCodePart,
                reasonPart,
                failurePart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Log.d("IMAGE UPLOAD SUCCESS",response.body().toString());
                } else {
                    // Handle unsuccessful response
                    Log.d("IMAGE UPLD NOT SUCCESS",response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
