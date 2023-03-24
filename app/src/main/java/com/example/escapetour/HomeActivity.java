package com.example.escapetour;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long pressedTime;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FloatingSearchView mSearchView;
    private HomeFragment homeFragment;
    Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        homeFragment = new HomeFragment();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");


//        mSearchView = findViewById(R.id.search_view);
//
//        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//
//            }
//        });


        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);


        toggle = new ActionBarDrawerToggle(this, drawerLayout, null, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
//        mSearchView.attachNavigationDrawerToMenuButton(drawerLayout);


//        searchbar_item();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.flContent, fragment);
        ft.commit();

    }

    private void searchbar_item() {

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.share_icon) {
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
                    String app_url = " https://play.google.com/store/apps/details?id=" + getPackageName();
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));

                }
            }
        });

    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
//
//        switch (id) {
//            case R.id.all_places_item:
//                if (currentFragment instanceof HomeFragment) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else if(currentFragment instanceof ContactUsFragment){
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new ContactUsFragment(), "contact_us_fragment").addToBackStack(null).commit();
//
////                    Intent intent = new Intent(this, HomeActivity.class);
////                    startActivity(intent);
//
////                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
////                    getSupportFragmentManager().beginTransaction()
////                            .replace(R.id.flContent, new HomeFragment())
////                            .commit();
//                }else if(currentFragment instanceof AboutUsFragment){
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new ContactUsFragment(), "contact_us_fragment").addToBackStack(null).commit();
//                }
//                break;
//
//            case R.id.contact_us_item:
//                if (currentFragment instanceof ContactUsFragment) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else  if(currentFragment instanceof HomeFragment){
//
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new ContactUsFragment(), "contact_us_fragment").addToBackStack(null).commit();
//                }else if(currentFragment instanceof AboutUsFragment){
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new ContactUsFragment()).commit();
//                }
//                break;
//
//            case R.id.about_us_item:
//                if (currentFragment instanceof AboutUsFragment) {
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else  if(currentFragment instanceof HomeFragment){
//
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new AboutUsFragment(), "contact_us_fragment").addToBackStack(null).commit();
//                }else if(currentFragment instanceof AboutUsFragment){
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.flContent, new AboutUsFragment(), "contact_us_fragment").commit();
//                }
//                break;
//
//            case R.id.rate_us_item:
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//                }
//                break;
//
//            case R.id.more_apps_item:
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market")));
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
//                }
//                break;
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
        switch (id) {

            case R.id.all_places_item:

                if (currentFragment instanceof HomeFragment) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if (currentFragment instanceof ContactUsFragment) {

//                    fragment =new HomeFragment();
                    ContactUsFragment.backpressedlistener.onBackPressed();
                } else {
//                    fragment =new HomeFragment();
                    AboutUsFragment.backpressedlistener.onBackPressed();
                }

                break;
            case R.id.contact_us_item:

                if (currentFragment instanceof ContactUsFragment) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    fragment = new ContactUsFragment();
                }

                break;

            case R.id.about_us_item:
                if (currentFragment instanceof AboutUsFragment) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    fragment = new AboutUsFragment();
                }
                break;
            case R.id.rate_us_item:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;
            case R.id.more_apps_item:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
                }
                break;

        }

        ;

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() == 0) {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.flContent, fragment).commit();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    public void onBackPressed() {
//        drawerLayout = findViewById(R.id.drawer);
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            if (pressedTime + 2000 > System.currentTimeMillis()) {
//                super.onBackPressed();
//                finish();
//            }
//            else if (ContactUsFragment.backpressedlistener != null) {
//                ContactUsFragment.backpressedlistener.onBackPressed();
//
//            } else if (AboutUsFragment.backpressedlistener != null) {
//                AboutUsFragment.backpressedlistener.onBackPressed();
//            } else if (SearchBoxFragment.backpressedlistener != null) {
//                SearchBoxFragment.backpressedlistener.onBackPressed();
//            } else {
//                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
//            }
//            pressedTime = System.currentTimeMillis();
//        }
//    }


}


