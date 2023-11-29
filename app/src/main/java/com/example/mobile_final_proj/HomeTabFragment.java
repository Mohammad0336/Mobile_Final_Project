// HomeTabFragment.java
package com.example.mobile_final_proj;

import android.os.AsyncTask;
import android.os.Bundle;
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
    private ProgressBar progressBarGames;
    private ProgressBar progressBarNews;
    private TextView matchesLabel;
    private TextView newsLabel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        matchesLabel = view.findViewById(R.id.matchesLabel);
        newsLabel = view.findViewById(R.id.newsLabel);
        recyclerViewGames = view.findViewById(R.id.recyclerViewGames);
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        progressBarGames = view.findViewById(R.id.progressBarGames);
        progressBarNews = view.findViewById(R.id.progressBarNews);

        // Set up RecyclerView for games
        recyclerViewGames.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up RecyclerView for news
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Execute the API tasks
        new GameApiTask().execute();
        new NewsApiTask().execute();

        return view;
    }

    private class GameApiTask extends AsyncTask<Void, Void, List<GameItem>> {

        @Override
        protected void onPreExecute() {
            progressBarGames.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<GameItem> doInBackground(Void... voids) {
            List<GameItem> gameItemList = new ArrayList<>();

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://free-nba.p.rapidapi.com/games?page=0&per_page=25")
                        .header("X-RapidAPI-Key", "55267d0dfemsh288146d4ea89887p102994jsnedf26b1f5ac3")
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
            progressBarGames.setVisibility(View.GONE);

            // Create and set up the adapter for games
            GameAdapter gameAdapter = new GameAdapter(gameItemList);

            // Check if the RecyclerView is not null before setting the adapter
            if (recyclerViewGames != null) {
                recyclerViewGames.setAdapter(gameAdapter);
            }
        }
    }

    private class NewsApiTask extends AsyncTask<Void, Void, List<NewsItem>> {

        @Override
        protected void onPreExecute() {
            progressBarNews.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<NewsItem> doInBackground(Void... voids) {
            List<NewsItem> newsItemList = new ArrayList<>();

            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://newsapi.org/v2/top-headlines?country=us&category=sports&apiKey=c85f639984534b31b9e3ff2060973dc9\n")  // Replace with the actual API endpoint
                        .addHeader("X-Api-Key", "c85f639984534b31b9e3ff2060973dc9")  // Replace with your API key
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();

                if (result != null) {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray articlesArray = jsonResponse.getJSONArray("articles");

                    // Iterate through each article in the array
                    for (int i = 0; i < articlesArray.length(); i++) {
                        JSONObject article = articlesArray.getJSONObject(i);

                        String sourceName = article.getJSONObject("source").getString("name");
                        String author = article.getString("author");
                        String title = article.getString("title");
                        String description = article.getString("description");
                        String url = article.getString("url");
                        String urlToImage = article.getString("urlToImage");
                        String publishedAt = article.getString("publishedAt");
                        String content = article.getString("content");

                        NewsItem newsItem = new NewsItem(sourceName, author, title, description, url, urlToImage, publishedAt, content);
                        newsItemList.add(newsItem);
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return newsItemList;
        }

        @Override
        protected void onPostExecute(List<NewsItem> newsItemList) {
            progressBarNews.setVisibility(View.GONE);

            // Create and set up the adapter for news
            NewsAdapter newsAdapter = new NewsAdapter(newsItemList);

            // Check if the RecyclerView is not null before setting the adapter
            if (recyclerViewNews != null) {
                recyclerViewNews.setAdapter(newsAdapter);
            }
        }
    }
}
