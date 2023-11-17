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


import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.List;

public class PlayTabFragment extends Fragment {

    private static final String API_KEY = "AIzaSyCq-rXXQrQ16c4ddDzWtOAPeweGT66tOEA";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_tab, container, false);

        // Example query: "Popular sports videos"
        String query = "Popular sports videos";

        // Make API request
        new YouTubeApiRequest(API_KEY) {
            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    // Process the result (comma-separated video IDs)
                    String[] videoIds = result.split(",");
                    // Now you can use these video IDs in your YouTubePlayerView
                } else {
                    Toast.makeText(getActivity(), "Error fetching videos", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(query);

        return view;
    }

    private static class YouTubeApiRequest extends AsyncTask<String, Void, String> {

        private YouTube youtube;
        private String apiKey;

        public YouTubeApiRequest(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                youtube = new YouTube.Builder(
                        Transport.HTTP,
                        Transport.JSON_FACTORY,
                        null)
                        .setApplicationName("YourAppName")
                        .setJsonHttpRequestInitializer(new GoogleKeyInitializer(apiKey))
                        .build();

                YouTube.Search.List search = youtube.search().list("id,snippet");
                search.setQ(params[0]); // Set the search query
                search.setType("video");
                search.setOrder("viewCount"); // Sort by view count

                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();

                if (searchResultList != null && searchResultList.size() > 0) {
                    // Get the video IDs
                    StringBuilder videoIds = new StringBuilder();
                    for (SearchResult result : searchResultList) {
                        videoIds.append(result.getId().getVideoId()).append(",");
                    }
                    return videoIds.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

