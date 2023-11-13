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

public class LoginTabFragment extends Fragment {

    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        // Initialize UI elements
        loginEmailEditText = view.findViewById(R.id.login_email); // Replace with the actual ID from your layout XML
        loginPasswordEditText = view.findViewById(R.id.login_password); // Replace with the actual ID
        loginButton = view.findViewById(R.id.login_button); // Replace with the actual ID

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Set a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        return view;
    }

    private void userLogin() {
        // Retrieve user input from the EditText fields
        String email = loginEmailEditText.getText().toString();
        String password = loginPasswordEditText.getText().toString();

        // Check if any field is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the user with the provided email and password exists
            boolean loginSuccessful = databaseHelper.checkEmailPassword(email, password);

            if (loginSuccessful) {
                Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_SHORT).show();

                // Redirect to the main activity or dashboard
                // Replace MainActivity.class with the actual activity you want to navigate to
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Login Failed! Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
