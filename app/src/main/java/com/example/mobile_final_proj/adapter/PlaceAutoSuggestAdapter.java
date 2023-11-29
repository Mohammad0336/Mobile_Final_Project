package com.example.mobile_final_proj.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.example.mobile_final_proj.models.PlaceApi;
import java.util.ArrayList;

public class PlaceAutoSuggestAdapter extends ArrayAdapter<String> implements Filterable {

    private ArrayList<String> results;
    private Context context;
    private PlaceApi placeApi = new PlaceApi();

    public PlaceAutoSuggestAdapter(Context context, int resId){
        super(context, resId);
        this.context = context;
        this.results = new ArrayList<>();
    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public String getItem(int pos){
        return results.get(pos);
    }

    @Override
    public Filter getFilter(){
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    placeApi.autoComplete(constraint.toString(), new PlaceApi.PlacesTaskListener() {
                        @Override
                        public void onPlacesRetrieved(ArrayList<String> places) {
                            if (!places.isEmpty()) {
                                results = places;
                                filterResults.values = results;
                                filterResults.count = results.size();
                                notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onPlacesError(Exception e) {
                            // Handle error, if any
                        }
                    });
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0){
                    // Update results ArrayList with filtered data
                    PlaceAutoSuggestAdapter.this.results = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }
}