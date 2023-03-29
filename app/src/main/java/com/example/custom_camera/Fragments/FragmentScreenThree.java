package com.example.custom_camera.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.custom_camera.Camera.Model.CameraCharacteristics;
import com.example.custom_camera.CameraServiceTwo;
import com.example.custom_camera.Helpers.Utils;
import com.example.custom_camera.R;

import java.util.ArrayList;

public class FragmentScreenThree extends Fragment {

    private ArrayList<CameraCharacteristics> mCameraCharacteristicsList = new ArrayList<>();
    private TextView exposureText;
    private String defaultImagePath;
    private int mCurrentIndex = 0;
    private static final String text = "Capturing Image for Exposure: ";

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
                for( int i=0; i<mCameraCharacteristicsList.size(); i++) {
                    boolean sendData = false;
                    if (mCameraCharacteristicsList.get(i).getCameraExposure() == 0) {
                        sendData = true;
                    }
                    startServiceWithParams(sendData,mCameraCharacteristicsList.get(i));
                }
//                requireActivity().startService(new Intent(requireActivity(), CameraServiceTwo.class));
            }
        });
        return rootView;
    }
    private void startServiceWithParams(boolean param1, CameraCharacteristics param2) {
        Intent intent = new Intent(getActivity(), CameraServiceTwo.class);
        intent.putExtra("uploadImage", param1);
//        intent.putExtra("cameraConfig", param2);
        requireActivity().startService(intent);
    }
}