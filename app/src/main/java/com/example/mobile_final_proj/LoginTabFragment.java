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

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_tab, container, false);

        // Initialize UI elements
        emailEditText = view.findViewById(R.id.login_email);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_button);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Set a click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        return view;
    }

    private void loginUser() {
        // Retrieve user input from the EditText fields
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Check if any field is empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(), "Both email and password are required", Toast.LENGTH_SHORT).show();
        } else {
            // Check if the user with the provided email and password exists
            if (databaseHelper.checkEmailPassword(email, password)) {
                // User exists, set the current user's email in SessionManager
                SessionManager.getInstance().setCurrentUserEmail(email);

                // Redirect to the main activity or wherever needed
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
