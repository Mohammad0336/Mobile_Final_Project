package com.example.mobile_final_proj;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import android.media.MediaPlayer;

public class AudioTabFragment extends Fragment {
    private MediaPlayer mediaPlayer;
    private ImageView playPauseButton;
    private ImageView previousButton;
    private ImageView nextButton;
    private SeekBar seekBar;
    private Handler seekBarHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_tab, container, false);

        // Initialize media player and UI elements
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.testsong_20_sec);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        previousButton = view.findViewById(R.id.previousButton);
        nextButton = view.findViewById(R.id.nextButton);
        seekBar = view.findViewById(R.id.seekBar);

        // Initialize SeekBar
        seekBar.setMax(mediaPlayer.getDuration());

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setImageResource(R.drawable.play);
                } else {
                    mediaPlayer.start();
                    playPauseButton.setImageResource(R.drawable.pause);
                    updateSeekBar();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to play the previous track
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement logic to play the next track
            }
        });

        // Initialize SeekBar handler
        seekBarHandler = new Handler(Looper.getMainLooper());

        return view;
    }

    private void updateSeekBar() {
        seekBarHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
                updateSeekBar(); // Schedule the next update
            }
        }, 1000); // Update every 1 second
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
