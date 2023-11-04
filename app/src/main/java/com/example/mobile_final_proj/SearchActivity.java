package com.example.mobile_final_proj;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class SearchActivity extends Activity {

    private TextView textView;

    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String playerInfo = (String) msg.obj;
            if (playerInfo != null) {
                textView.setText(playerInfo);
            } else {
                textView.setText("no playerInfo");
            }
        }
    };
        private List<Player> playerList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search_view);
        }


    public void testSearch(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String api_key = "55267d0dfemsh288146d4ea89887p102994jsnedf26b1f5ac3";
                String playerInfo = null;

                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("https://free-nba.p.rapidapi.com/players?page=0&per_page=25&search=Lebron")
                            .addHeader("X-RapidAPI-Key", api_key)
                            .addHeader("X-RapidAPI-Host", "free-nba.p.rapidapi.com")
                            .get()
                            .build();

                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    Gson gson = new Gson();
                    SearchActivity.Player.PlayersResponse playersResponse = gson.fromJson(responseBody, SearchActivity.Player.PlayersResponse.class);

                    playerInfo = "Player Name: " + playersResponse.getData().get(0).getFirst_name() + " " + playersResponse.getData().get(0).getLast_name() + "\n"
                            + "Team: " + playersResponse.getData().get(0).getTeam().getFull_name() + "\n"
                            + "Position: " + playersResponse.getData().get(0).getPosition();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message message = handler.obtainMessage();
                message.obj = playerInfo;
                handler.sendMessage(message);
            }
        }).start();
    }

    public class Player {
        private int id;
        private String first_name;
        private int height_feet;
        private int height_inches;
        private String last_name;
        private String position;
        private SearchActivity.Player.Team team;
        private int weight_pounds;

        // Getters

        public String getFirst_name() {
            return first_name;
        }

        public int getHeight_feet() {
            return height_feet;
        }

        public int getHeight_inches() {
            return height_inches;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getPosition() {
            return position;
        }

        public SearchActivity.Player.Team getTeam() {
            return team;
        }

        public int getWeight_pounds() {
            return weight_pounds;
        }

        public class Team {
            private int id;
            private String abbreviation;
            private String city;
            private String conference;
            private String division;
            private String full_name;
            private String name;

            // Getters
            public int getId() {
                return id;
            }

            public String getAbbreviation() {
                return abbreviation;
            }

            public String getCity() {
                return city;
            }

            public String getConference() {
                return conference;
            }

            public String getDivision() {
                return division;
            }

            public String getFull_name() {
                return full_name;
            }

            public String getName() {
                return name;
            }

        }

        public class PlayersResponse {
            private List<SearchActivity.Player> data;
            private SearchActivity.Player.Meta meta;
            public List<SearchActivity.Player> getData() {
                return data;
            }

            public SearchActivity.Player.Meta getMeta() {
                return meta;
            }
        }

        public class Meta {
            private Integer next_page;

            public Integer getNext_page() {
                return next_page;
            }

        }
    }
}
