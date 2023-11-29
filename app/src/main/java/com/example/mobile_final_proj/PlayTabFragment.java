package com.example.mobile_final_proj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayTabFragment extends Fragment {

    private WebView webView;
    private Spinner videoSpinner;
    private Button changeVideoButton;

    private String[] videoTitles =  {"UFC", "FIFA", "NBA"};
    private String[] videoIds =  {"rrxamKa5mpk", "jVtB706YX-E",  "UQWUHh3rJFo"};
    private int currentVideoIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_tab, container, false);

        // Initialize WebView
        webView = view.findViewById(R.id.webView);
        //.setLayoutParams(new ViewGroup.LayoutParams(
        //        ViewGroup.LayoutParams.MATCH_PARENT,
        //        getResources().getDimensionPixelSize(R.dimen.video_thumbnail_height) // Adjust the height as needed
        //));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Initialize Spinner
        videoSpinner = view.findViewById(R.id.videoSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, videoTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        videoSpinner.setAdapter(adapter);

        // Set a listener to handle item selection
        videoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Load the selected YouTube video based on the position
                String selectedVideoId = videoIds[position];
                loadYouTubeVideo(selectedVideoId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        // Initialize Change Video Button
        changeVideoButton = view.findViewById(R.id.changeVideoButton);
        changeVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change to the next YouTube video
                changeToNextVideo();
            }
        });

        // Load the first YouTube video
        loadYouTubeVideo(videoIds[currentVideoIndex]);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    private void loadYouTubeVideo(String videoId) {
        // Load the YouTube video based on the selected video ID
        String videoUrl = "https://www.youtube.com/embed/" + videoId;
        webView.loadUrl(videoUrl);
    }

    private void changeToNextVideo() {
        currentVideoIndex = (currentVideoIndex + 1) % videoIds.length;
        loadYouTubeVideo(videoIds[currentVideoIndex]);
    }
}
