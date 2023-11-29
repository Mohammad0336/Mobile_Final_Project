package com.example.mobile_final_proj.models;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {

    public interface PlacesTaskListener {
        void onPlacesRetrieved(ArrayList<String> places);
        void onPlacesError(Exception e);
    }

    public static void autoComplete(String input, PlacesTaskListener listener) {
        new AsyncTask<String, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(String... inputs) {
                ArrayList<String> arrayList = new ArrayList<>();
                HttpURLConnection connection = null;
                StringBuilder jsonResult = new StringBuilder();

                try {
                    StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
                    stringBuilder.append("input=" + inputs[0]);
                    stringBuilder.append("&types=geocode"); // You can specify other types here
                    stringBuilder.append("&key=AIzaSyCPLWfsbqhABxGkdthSBs3YbiZYZvJXQUw"); // Replace with your API key
                    URL url = new URL(stringBuilder.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

                    int read;
                    char[] buff = new char[1024];

                    while ((read = inputStreamReader.read(buff)) != -1) {
                        jsonResult.append(buff, 0, read);
                    }

                    // Parse JSON response and populate the arrayList
                    // ... (Your JSON parsing logic here)

                } catch (MalformedURLException e) {
                    return null;
                } catch (IOException e) {
                    return null;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                try {
                    JSONObject jsonObject = new JSONObject(jsonResult.toString());
                    JSONArray predictions = jsonObject.getJSONArray("predictions");
                    for (int i = 0; i < predictions.length(); i++) {
                        arrayList.add(predictions.getJSONObject(i).getString("description"));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                return arrayList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> arrayList) {
                // Call the listener's method with retrieved data or error
                if (arrayList != null) {
                    listener.onPlacesRetrieved(arrayList);
                } else {
                    listener.onPlacesError(new Exception("No autocomplete results"));
                }
            }
        }.execute(input);
    }
}