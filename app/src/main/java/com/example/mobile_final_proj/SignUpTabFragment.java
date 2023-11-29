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
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_password);
        confirmPasswordEditText = view.findViewById(R.id.signup_confirm);
        registerButton = view.findViewById(R.id.signup_button);

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
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(confirmPassword)) {
                User user = new User(email, password);

                if (databaseHelper.insertUser(user)) {
                    Toast.makeText(getActivity(), "Signup Successfully!", Toast.LENGTH_SHORT).show();

                    // Redirect to the login activity
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Signup Failed!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
