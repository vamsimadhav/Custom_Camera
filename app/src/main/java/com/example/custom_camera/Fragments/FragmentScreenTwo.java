package com.example.custom_camera.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;

import com.example.custom_camera.Camera.Configurations.*;
import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.CameraService;
import com.example.custom_camera.R;

import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class FragmentScreenTwo extends Fragment {
    public FragmentScreenTwo() {
        // Required empty public constructor
    }

    boolean showTimer = false;
    ProgressBar barTimer;
    TextView textTimer, statusText;
    CountDownTimer countDownTimer;
    private NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_screen_two, container, false);
        statusText = rootView.findViewById(R.id.currentStatus);
        Button gotoCamera = rootView.findViewById(R.id.gotoCameraBtn);
        barTimer = rootView.findViewById(R.id.progressBarCircle);
        textTimer = rootView.findViewById(R.id.textViewTime);
        barTimer.setVisibility(View.GONE);
        textTimer.setVisibility(View.GONE);
        CameraCharacteristics cameraConfig = new CameraCharacteristics()
                        .getBuilder(getContext())
                        .setCameraFacing(CameraFacing.REAR_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setImageRotation(CameraRotation.ROTATION_90)
                        .setCameraFocus(CameraFocus.AUTO)
                        .setCameraIso(100)
                        .build();
        gotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimer = true;
                navController = Navigation.findNavController(view);
                startServiceWithParams(false,cameraConfig);
            }
        });

        return rootView;
    }

    private void startServiceWithParams(boolean param1, CameraCharacteristics param2) {
        Intent intent = new Intent(getActivity(), CameraService.class);
        intent.putExtra("uploadImage", param1);
//        intent.putExtra("cameraConfig", param2);
        requireActivity().startService(intent);
    }

    private BroadcastReceiver pictureSavedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("picture_saved".equals(intent.getAction())) {
                // Handle the picture saved event
                statusText.setText("Image Captured");
                if (showTimer) {
                    barTimer.setVisibility(View.VISIBLE);
                    textTimer.setVisibility(View.VISIBLE);
                    startTimer(6);
                    showTimer = false;
                }
            }
        }
    };

    private void startTimer(final int minuti) {

        int timeCounter = minuti * 60 *1000;
        barTimer.setMax(timeCounter);

        countDownTimer = new CountDownTimer(60 * minuti * 1000, 5000) {
            // 500 means, onTick function will be called at every 500 milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                barTimer.setProgress((int)seconds);
                textTimer.setText(String.format("%02d", seconds/60) + ":" + String.format("%02d", seconds%60));
                // format the textview to show the easily readable format

            }
            @Override
            public void onFinish() {
                navController.navigate(R.id.fragmentScreenThree);
            }
        }.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("picture_saved");
        getContext().registerReceiver(pictureSavedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(pictureSavedReceiver);
    }
}