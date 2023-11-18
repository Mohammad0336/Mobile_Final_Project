package com.example.mobile_final_proj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlayTabFragment extends Fragment {

    // private static final String API_KEY = "AIzaSyCq-rXXQrQ16c4ddDzWtOAPeweGT66tOEA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_tab, container, false);
        return view;
    }
}

