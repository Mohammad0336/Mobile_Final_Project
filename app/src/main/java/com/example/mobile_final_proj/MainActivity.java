package com.example.mobile_final_proj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager2.setUserInputEnabled(false);

        tabAdapter = new TabAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(tabAdapter);


        // Attach TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            // Set the tab titles based on your TabItems in the XML layout
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.home);
                    break;
                case 1:
                    tab.setIcon(R.drawable.search);
                    break;
                case 2:
                    tab.setIcon(R.drawable.play);
                    break;
                case 3:
                    tab.setIcon(R.drawable.audio);
                    break;
                case 4:
                    tab.setIcon(R.drawable.profile);
                    break;
            }
        }).attach();

    }
    public void signUp(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}