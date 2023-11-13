package com.example.mobile_final_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SignUpTabFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_tab, container, false);

        // Initialize UI elements
        emailEditText = view.findViewById(R.id.signup_email); // Replace with the actual ID from your layout XML
        passwordEditText = view.findViewById(R.id.signup_password); // Replace with the actual ID
        confirmPasswordEditText = view.findViewById(R.id.signup_confirm);
        registerButton = view.findViewById(R.id.signup_button); // Replace with the actual ID

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Set a click listener for the register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        return view;
    }

    private void registerUser() {
        // Retrieve user input from the EditText fields
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Check if any field is empty
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(confirmPassword)) {
                // Check if the user with the same email exists
                if (!databaseHelper.checkEmail(email)) {
                    // User doesn't exist, perform registration
                    boolean registrationSuccessful = databaseHelper.insertData(email, password);

                    if (registrationSuccessful) {
                        Toast.makeText(getActivity(), "Signup Successfully!", Toast.LENGTH_SHORT).show();

                        // Redirect to the login activity
                        Intent intent = new Intent(getActivity(), RegisterActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Signup Failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "User already exists! Please login", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
