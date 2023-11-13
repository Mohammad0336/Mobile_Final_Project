package com.example.mobile_final_proj;

public class GameItem {

    private String team1;
    private String team2;
    private String score;

    public GameItem(String team1, String team2, String score) {
        this.team1 = team1;
        this.team2 = team2;
        this.score = score;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getScore() {
        return score;
    }
}

