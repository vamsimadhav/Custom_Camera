package com.example.custom_camera.Fragments;

import android.hardware.Camera;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.custom_camera.DatabaseHelper;
import com.example.custom_camera.Helpers.Utils;
import com.example.custom_camera.R;
import com.example.custom_camera.UserData;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class FragmentScreenOne extends Fragment {

    private Button testButton;
    private TextInputLayout nameInput;
    private TextInputLayout emailInput;

    public FragmentScreenOne() {
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
        View rootView =  inflater.inflate(R.layout.fragment_screen_one, container, false);
        DatabaseHelper databaseHelper = DatabaseHelper.getDB(getContext());

        nameInput = rootView.findViewById(R.id.userNameTextLayout);
        emailInput = rootView.findViewById(R.id.userEmailTextLayout);
        testButton = rootView.findViewById(R.id.testButton);

        EditText nameEdit = nameInput.getEditText();
        assert nameEdit != null;
        EditText emailEdit = emailInput.getEditText();
        assert emailEdit != null;

        ArrayList<UserData> userArrayData = (ArrayList<UserData>) databaseHelper.userDataDAO().getUserData();
        if (userArrayData.size() > 0) {
            String name = userArrayData.get(0).getName();
            String email = userArrayData.get(0).getEmail();
            emailEdit.setText(email);
            nameEdit.setText(name);
        }

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String name = charSequence.toString();
                Utils.isNameValid(nameInput,name);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = charSequence.toString();
                Utils.isEmailValid(emailInput, email);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<UserData> userArrayData = (ArrayList<UserData>) databaseHelper.userDataDAO().getUserData();
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();

                if (userArrayData.isEmpty()) {
                    UserData userData = new UserData(name,email);
                    databaseHelper.userDataDAO().addUserData(userData);
                } else {
                    boolean alreadyExists = false;
                    for (int i=0; i<userArrayData.size(); i++) {
                        if (email.equals(userArrayData.get(i).getEmail().toString())) {
                            alreadyExists = true;
                        }
                    }
                    if (!alreadyExists) {
                        UserData userData = new UserData(name,email);
                        databaseHelper.userDataDAO().addUserData(userData);
                    }
                }
            }
        });

        return rootView;
    }
}