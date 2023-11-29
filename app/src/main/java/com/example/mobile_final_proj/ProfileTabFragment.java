package com.example.mobile_final_proj;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileTabFragment extends Fragment {

    private TextView userEmailTextView;
    private TextView passwordTextView;
    private Button showHidePasswordButton;
    private ImageView profileImageView;
    private Button uploadImageButton;
    private Button captureImageButton;
    private boolean isPasswordVisible = false;
    private DatabaseHelper databaseHelper;

    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        // Initialize UI elements
        userEmailTextView = view.findViewById(R.id.user_email_text_view);
        passwordTextView = view.findViewById(R.id.password_text_view);
        showHidePasswordButton = view.findViewById(R.id.show_hide_password_button);
        profileImageView = view.findViewById(R.id.profile_image_view);
        uploadImageButton = view.findViewById(R.id.upload_image_button);
        captureImageButton = view.findViewById(R.id.capture_image_button);

        // Initialize the DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Retrieve and display the current user's email
        User currentUser = retrieveCurrentUser();
        if (currentUser != null) {
            userEmailTextView.setText("User Email: " + currentUser.getEmail());
        }

        // Retrieve and display the current user's password
        String currentUserEmail = SessionManager.getInstance().getCurrentUserEmail();
        if (currentUserEmail != null) {
            User user = databaseHelper.getUserByEmail(currentUserEmail);
            if (user != null) {
                passwordTextView.setText("Current Password: " + (isPasswordVisible ? user.getPassword() : "********"));
            }
        }

        // Set click listener for the show/hide password button
        showHidePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Set click listener for the upload image button
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set click listener for the capture image button
        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        return view;
    }

    private User retrieveCurrentUser() {
        String currentUserEmail = SessionManager.getInstance().getCurrentUserEmail();
        return databaseHelper.getUserByEmail(currentUserEmail);
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        String currentUserEmail = SessionManager.getInstance().getCurrentUserEmail();
        if (currentUserEmail != null) {
            User user = databaseHelper.getUserByEmail(currentUserEmail);
            if (user != null) {
                passwordTextView.setText("Current Password: " + (isPasswordVisible ? user.getPassword() : "********"));
            }
        }
    }

    private void openImagePicker() {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQ_CODE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                profileImageView.setImageURI(selectedImageUri);
            }
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(imageBitmap);
        }
    }
}
