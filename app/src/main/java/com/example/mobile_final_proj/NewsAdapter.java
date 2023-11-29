// NewsAdapter.java
package com.example.mobile_final_proj;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItemList;

    public NewsAdapter(List<NewsItem> newsItemList) {
        this.newsItemList = newsItemList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);

        holder.titleTextView.setText(newsItem.getTitle());
        holder.descriptionTextView.setText(newsItem.getDescription());
        holder.urlTextView.setText(R.string.read_more);

        // Set click listener for the URL TextView
        holder.urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle URL click by opening it in a web browser
                String url = newsItem.getUrl();
                if (url != null && !url.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    v.getContext().startActivity(browserIntent);
                }
            }
        });

        // You can set other views as needed...
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        TextView urlTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
            // Add other views as needed...
        }
    }
}
