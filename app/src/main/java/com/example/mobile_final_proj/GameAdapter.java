package com.example.mobile_final_proj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    private List<GameItem> gameItemList;

    public GameAdapter(List<GameItem> gameItemList) {
        this.gameItemList = gameItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameItem gameItem = gameItemList.get(position);

        // Bind data to the views in the ViewHolder
        holder.team1TextView.setText(gameItem.getTeam1());
        holder.scoreTextView.setText(gameItem.getScore());
        holder.team2TextView.setText(gameItem.getTeam2());
    }

    @Override
    public int getItemCount() {
        return gameItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView team1TextView;
        TextView scoreTextView;
        TextView team2TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            team1TextView = itemView.findViewById(R.id.team1TextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            team2TextView = itemView.findViewById(R.id.team2TextView);
        }
    }
}

