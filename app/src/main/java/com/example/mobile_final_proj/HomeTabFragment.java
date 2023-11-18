package com.example.mobile_final_proj;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeTabFragment extends Fragment {

    private RecyclerView recyclerViewGames;
    private RecyclerView recyclerViewNews;
    private ProgressBar progressBar;
    private TextView matchesLabel;
    private TextView newsLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        matchesLabel = view.findViewById(R.id.matchesLabel);
        recyclerViewGames = view.findViewById(R.id.recyclerViewGames);
        progressBar = view.findViewById(R.id.progressBar);

        // Set up RecyclerView for games
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Execute the API tasks
        new GameApiTask().execute();

        return view;
    }

    private class GameApiTask extends AsyncTask<Void, Void, List<GameItem>> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<GameItem> doInBackground(Void... voids) {
            List<GameItem> gameItemList = new ArrayList<>();

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://free-nba.p.rapidapi.com/games?page=0&per_page=25")
                        .header("X-RapidAPI-Key", "pasteapikeyhere")
                        .header("X-RapidAPI-Host", "free-nba.p.rapidapi.com")
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                if (result != null) {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray gamesArray = jsonResponse.getJSONArray("data");

                    // Iterate through each game in the array
                    for (int i = 0; i < 3; i++) { // gamesArray.length()
                        JSONObject game = gamesArray.getJSONObject(i);

                        // Extract relevant information from the JSON and create GameItem object
                        String team1 = game.getJSONObject("home_team").getString("full_name");
                        String team2 = game.getJSONObject("visitor_team").getString("full_name");
                        String score = game.getString("home_team_score") + " - " + game.getString("visitor_team_score");

                        GameItem gameItem = new GameItem(team1, team2, score);
                        gameItemList.add(gameItem);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return gameItemList;
        }

        @Override
        protected void onPostExecute(List<GameItem> gameItemList) {
            progressBar.setVisibility(View.GONE);

            // Create and set up the adapter for games
            GameAdapter gameAdapter = new GameAdapter(gameItemList);

            // Check if the RecyclerView is not null before setting the adapter
            if (recyclerViewGames != null) {
                recyclerViewGames.setAdapter(gameAdapter);
            }
        }
    }

}
