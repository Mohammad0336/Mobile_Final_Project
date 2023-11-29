package com.example.mobile_final_proj;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mobile_final_proj.adapter.PlaceAutoSuggestAdapter;
import com.example.mobile_final_proj.models.PlaceApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchTabFragment extends Fragment implements OnMapReadyCallback, OnConnectionFailedListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // Vars
    private GoogleMap myMap;
    private Boolean LocationPermissionsGranted = false;
    private static final String TAG = "MapActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Widgets
    private AutoCompleteTextView searchText;
    private PlaceAutoSuggestAdapter placeAutoSuggestAdapter;
    private ImageView gps;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_tab, container, false);

        searchText = rootView.findViewById(R.id.input_search);
        gps = rootView.findViewById(R.id.ic_gps);

        placeAutoSuggestAdapter = new PlaceAutoSuggestAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1);
        searchText.setAdapter(placeAutoSuggestAdapter); // Set up the adapter

        getLocationPermission();

        return rootView;
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        gps.setOnClickListener(view -> {
            Log.d(TAG, "onClick: clicked gps icon");
            getDeviceLocation();
        });
        hideSoftKeyboard();
    }

    private void geoLocate() {
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = searchText.getText().toString();

        // Use PlaceApi for autocomplete suggestions
        PlaceApi placeApi = new PlaceApi();
        placeApi.autoComplete(searchString, new PlaceApi.PlacesTaskListener() {
            @Override
            public void onPlacesRetrieved(ArrayList<String> places) {
                if (!places.isEmpty()) {
                    placeAutoSuggestAdapter.clear();
                    placeAutoSuggestAdapter.addAll(places);
                    placeAutoSuggestAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onPlacesError(Exception e) {
                // Handle error, if any
            }
        });

        Geocoder geocoder = new Geocoder(SearchTabFragment.this.requireContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (!list.isEmpty()) {
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());

            // Remove previous markers
            myMap.clear();

            LatLng searchedLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(searchedLatLng, address.getAddressLine(0));

            // Add a marker for the searched address
            myMap.addMarker(new MarkerOptions().position(searchedLatLng).title(address.getAddressLine(0)));

            searchText.getText().clear();

            // Hide the keyboard after searching
            hideSoftKeyboard();
        }
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(requireContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        myMap = googleMap;
        ImageView zoomInButton = requireView().findViewById(R.id.zoom_in_button);
        ImageView zoomOutButton = requireView().findViewById(R.id.zoom_out_button);
        ImageView centerButton = requireView().findViewById(R.id.center_button);

        if (LocationPermissionsGranted) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myMap.setMyLocationEnabled(true);
            myMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();
        }

        zoomInButton.setOnClickListener(view -> {
            if (myMap != null) {
                myMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoomOutButton.setOnClickListener(view -> {
            if (myMap != null) {
                myMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        centerButton.setOnClickListener(view -> recenterMap());

    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        try{
            if(LocationPermissionsGranted){

                final Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                "My Location");

                    }else{
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(SearchTabFragment.this.requireContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }


    private void moveCamera(LatLng latLng, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, SearchTabFragment.DEFAULT_ZOOM));

        if (title.equals("My Location")){
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            myMap.addMarker(options);
        }
        hideSoftKeyboard();
    }

    private void recenterMap() {
        if (myMap != null) {
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                    new CameraPosition.Builder()
                            .target(myMap.getCameraPosition().target) // Center the camera at the current position
                            .zoom(myMap.getCameraPosition().zoom) // Maintain the current zoom level
                            .bearing(0) // Reset the bearing (rotation) to 0 degrees
                            .tilt(0) // Reset the tilt to 0 degrees (flat)
                            .build()
            ));
        }
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.requireContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.requireContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionsGranted = true;
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
                initMap();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        LocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            LocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    LocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}