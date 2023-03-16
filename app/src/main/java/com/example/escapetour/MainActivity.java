package com.example.escapetour;


import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;


public class MainActivity extends AppIntro {
    String prevStarted = "yes";
    private long pressedTime;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            moveToSecondary();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addSlide(AppIntroFragment.newInstance("Welcome", "Join us to enjoy amazing features of this app.",
                R.drawable.city_guide, ContextCompat.getColor(getApplicationContext(), R.color.blue_50)));

        // below line is for creating second slide.
        addSlide(AppIntroFragment.newInstance("Location", "This app calculates distance between your location and destination.", R.drawable.location,
                ContextCompat.getColor(getApplicationContext(), R.color.green_location)));

        // below line is use to create third slide.
        addSlide(AppIntroFragment.newInstance("Ready to Escape", "Here we escape...", R.drawable.ready_sign,
                ContextCompat.getColor(getApplicationContext(), R.color.brown_ready)));

        setColorTransitionsEnabled(true);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }

    public void moveToSecondary() {
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}