package com.example.custom_camera.Fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import com.example.custom_camera.R;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class FragmentScreenTwo extends Fragment {
    public FragmentScreenTwo() {
        // Required empty public constructor
    }

    boolean isNextScreen = true;


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
               if (isNextScreen) {
                   Navigation.findNavController(view).navigate(R.id.cameraActivityThree);
               } else {
                   Navigation.findNavController(view).navigate(R.id.cameraActivityTwo);
               }
            }
        });

        return rootView;
    }
}