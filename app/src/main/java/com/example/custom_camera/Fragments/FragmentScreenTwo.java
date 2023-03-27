package com.example.custom_camera.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.custom_camera.Activitys.CameraActivityTwo;
import com.example.custom_camera.R;
public class FragmentScreenTwo extends Fragment {
    public FragmentScreenTwo() {
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
        View rootView =  inflater.inflate(R.layout.fragment_screen_two, container, false);
        Button gotoCamera = rootView.findViewById(R.id.gotoCameraBtn);
        gotoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.cameraActivityTwo);
            }
        });
        return rootView;
    }
}