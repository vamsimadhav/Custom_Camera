package com.example.custom_camera.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.custom_camera.Helpers.Utils;
import com.example.custom_camera.Networking.APIHelpers;
import com.example.custom_camera.Networking.Models.UserTokens;
import com.example.custom_camera.Networking.NetworkCallback;
import com.example.custom_camera.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUpload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        Button btnUpload = findViewById(R.id.btnUpload);
        ImageView imageView = findViewById(R.id.imageView);
        String defaultImagePath = getIntent().getStringExtra("defaultImage");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APIHelpers.authenticateApp(new NetworkCallback() {
                    @Override
                    public void authenticateTokens(UserTokens userTokens) {
                        String msg = userTokens.getClient() +":" +userTokens.getAccessToken()+":" +userTokens.getUid();
                        Log.d("API",msg);
                        String savedImage = "";
                        Date currentDate = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = dateFormat.format(currentDate);
                        Bitmap bitmap = Utils.getBitmapFromPath(defaultImagePath);
                        if (bitmap != null) {
                            savedImage = Utils.convertBitMapToBase64(bitmap);
                        }
//                   APIHelpers.sendImageToServer(userTokens,dateString,savedImage);
                    }
                });
            }
        });
    }
}