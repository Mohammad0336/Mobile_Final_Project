package com.example.mobile_final_proj;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;

public class AudioTabFragment extends Fragment {

    private WebView spotifyWebView1;
    private WebView spotifyWebView2;
    private WebView spotifyWebView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_tab, container, false);

        // Initialize UI elements
        spotifyWebView1 = view.findViewById(R.id.spotifyWebView1);
        spotifyWebView2 = view.findViewById(R.id.spotifyWebView2);
        spotifyWebView3 = view.findViewById(R.id.spotifyWebView3);

        // Initialize WebView settings
        initWebViewSettings(spotifyWebView1);
        initWebViewSettings(spotifyWebView2);
        initWebViewSettings(spotifyWebView3);

        // Load Spotify embed codes into WebViews
        loadSpotifyEmbedCode(spotifyWebView1, "<iframe style=\"border-radius:12px\" src=\"https://open.spotify.com/embed/episode/4edPl7ENIMzemyodvJw2gJ?utm_source=generator&theme=0\" width=\"100%\" height=\"152\" frameBorder=\"0\" allowfullscreen=\"\" allow=\"autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture\" loading=\"lazy\"></iframe>");
        loadSpotifyEmbedCode(spotifyWebView2, "<iframe style=\"border-radius:12px\" src=\"https://open.spotify.com/embed/episode/6n36RpwqpgZI5Z8qFf4Abj?utm_source=generator&theme=0\" width=\"100%\" height=\"152\" frameBorder=\"0\" allowfullscreen=\"\" allow=\"autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture\" loading=\"lazy\"></iframe>");
        loadSpotifyEmbedCode(spotifyWebView3, "<iframe style=\"border-radius:12px\" src=\"https://open.spotify.com/embed/episode/2OnpPtbN5d4JjgtV8iyLxS?utm_source=generator\" width=\"100%\" height=\"152\" frameBorder=\"0\" allowfullscreen=\"\" allow=\"autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture\" loading=\"lazy\"></iframe>");

        return view;
    }

    private void initWebViewSettings(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void loadSpotifyEmbedCode(WebView webView, String modifiedEmbedCode) {
        webView.loadData(modifiedEmbedCode, "text/html", "utf-8");
    }
}
