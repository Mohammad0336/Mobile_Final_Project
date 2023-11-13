package com.example.mobile_final_proj;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Create and return the appropriate fragment for each tab position
        switch (position) {
            // Replace with your actual fragment class
            case 1:
                return new SearchTabFragment(); // Replace with your actual fragment class
            case 2:
                return new PlayTabFragment(); // Replace with your actual fragment class
            case 3:
                return new AudioTabFragment(); // Replace with your actual fragment class
            case 4:
                return new ProfileTabFragment(); // Replace with your actual fragment class
            default:
                return new HomeTabFragment(); // Default fragment if position is out of bounds
        }
    }

    @Override
    public int getItemCount() {
        // Return the total number of tabs
        return 5; // Adjust this based on the number of tabs you have
    }
}
