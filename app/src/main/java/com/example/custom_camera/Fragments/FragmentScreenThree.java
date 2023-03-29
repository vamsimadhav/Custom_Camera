package com.example.custom_camera.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.CameraService;
import com.example.custom_camera.Helpers.Utils;
import com.example.custom_camera.Networking.APIHelpers;
import com.example.custom_camera.Networking.Models.User;
import com.example.custom_camera.Networking.Models.UserTokens;
import com.example.custom_camera.Networking.NetworkCallback;
import com.example.custom_camera.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FragmentScreenThree extends Fragment {

    private ArrayList<CameraCharacteristics> mCameraCharacteristicsList = new ArrayList<>();
    private TextView exposureText;
    private int mCurrentIndex = 0;
    private static final String text = "Captured Image for Exposure: ";

    private UserTokens mUserTokens;

    public FragmentScreenThree() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_screen_three, container, false);

        mCameraCharacteristicsList = Utils.buildParameters(getContext());

        Button btnCapture = rootView.findViewById(R.id.gotoCameraBtn);
        exposureText = rootView.findViewById(R.id.currentStatus);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startServiceWithParams(mCameraCharacteristicsList.get(mCurrentIndex));
//                requireActivity().startService(new Intent(requireActivity(), CameraServiceTwo.class));
            }
        });
        return rootView;
    }
    private void startServiceWithParams(CameraCharacteristics param2) {
        Intent intent = new Intent(getActivity(), CameraService.class);
        intent.putExtra("exposure",param2.getCameraExposure());
        intent.putExtra("iso",param2.getCameraIso());
        intent.putExtra("facing",param2.getFacing());
        intent.putExtra("focus",param2.getFocusMode());
        intent.putExtra("imageFormat",param2.getImageFormat());
        intent.putExtra("resolution",param2.getResolution());
        intent.putExtra("rotation",param2.getImageRotation());
        requireActivity().startService(intent);
    }

    private BroadcastReceiver pictureSavedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("picture_saved".equals(intent.getAction())) {
                // Handle the picture saved event
                exposureText.setText(text + mCameraCharacteristicsList.get(mCurrentIndex).getCameraExposure());
                if (mCurrentIndex < mCameraCharacteristicsList.size() - 1) {
                    mCurrentIndex++;
                    startServiceWithParams(mCameraCharacteristicsList.get(mCurrentIndex));
                }
            }
            else if ("all_saved".equals(intent.getAction())) {
                apiCall();
            }
        }
    };

    private void apiCall() {

        Toast toast = Toast.makeText(getContext(), "API call in progress...", Toast.LENGTH_SHORT);
        toast.show();
       APIHelpers.authenticateApp(new NetworkCallback() {
           @Override
           public void authenticateTokens(UserTokens userTokens) {
               mUserTokens = userTokens;
               if (mUserTokens != null) {
                   String savedImage = "";
                   Date currentDate = new Date();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                   String dateString = dateFormat.format(currentDate);
                   Bitmap bitmap = Utils.getBitmapFromPath(getContext(),mCameraCharacteristicsList);
                   if (bitmap != null) {
                       savedImage = Utils.convertBitMapToBase64(bitmap);
                   }
                   APIHelpers.sendImageToServer(mUserTokens,savedImage,dateString);
               }
               toast.cancel();
           }
       });


    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("picture_saved");
        filter.addAction("all_saved");
        getContext().registerReceiver(pictureSavedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(pictureSavedReceiver);
    }
}