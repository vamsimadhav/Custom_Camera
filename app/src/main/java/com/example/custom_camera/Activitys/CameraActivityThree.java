package com.example.custom_camera.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
import com.example.custom_camera.Camera.Configurations.*;
import com.example.custom_camera.Camera.Helper.HiddenCameraUtils;
import com.example.custom_camera.Camera.HiddenCameraActivity;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.Camera.Model.CameraError;
import com.example.custom_camera.Helpers.Utils;
import com.example.custom_camera.Networking.APICaller;
import com.example.custom_camera.Networking.APIHelpers;
import com.example.custom_camera.Networking.Models.User;
import com.example.custom_camera.Networking.Models.UserTokens;
import com.example.custom_camera.Networking.NetworkCallback;
import com.example.custom_camera.Networking.RetrofitClient;
import com.example.custom_camera.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraActivityThree extends HiddenCameraActivity {

    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    private ArrayList<CameraCharacteristics> mCameraCharacteristicsList = new ArrayList<>();
    private int mCurrentIndex = 0;
    private static final String text = "Capturing Image for Exposure: ";
    private TextView exposureText;
    private String defaultImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_three);

//        mCameraCharacteristicsList = Utils.buildParameters();

        exposureText = findViewById(R.id.exposureText);

        //        /Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera(mCameraCharacteristicsList.get(mCurrentIndex));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQ_CODE_CAMERA_PERMISSION);
        }

//        Take a picture
        findViewById(R.id.capture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take picture using the camera without preview.
                takePicture();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_CAMERA_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(mCameraCharacteristicsList.get(mCurrentIndex));
            } else {
                Toast.makeText(this, R.string.error_camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {

    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onSaveCompletion(boolean isSaved) {
        exposureText.setText(text + mCameraCharacteristicsList.get(mCurrentIndex).getCameraExposure());
        mCurrentIndex++;
        if (mCurrentIndex < mCameraCharacteristicsList.size()) {
            try {
                startCamera(mCameraCharacteristicsList.get(mCurrentIndex));
                takePicture();
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendDataToAPI(boolean sendData) {

        if (sendData) {
            APIHelpers.authenticateApp(new NetworkCallback() {
                @Override
                public void authenticateTokens(UserTokens userTokens) {
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


    }

    @Override
    public void saveDefaultImagePath(String path) {
        defaultImagePath = path;
    }




}