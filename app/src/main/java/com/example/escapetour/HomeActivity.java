package com.example.escapetour;


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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.material.navigation.NavigationView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long pressedTime;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FloatingSearchView mSearchView;
    private HomeFragment homeFragment;


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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        switch (id) {

            case R.id.all_places_item:
                fragment = new HomeFragment();
                break;
            case R.id.contact_us_item:
                fragment = new ContactUsFragment();
//                ViewPager viewPager=
//              ViewPager2 viewPager2=homeFragment.getViewPager();
//             int t= viewPager2.getCurrentItem();
//                Toast.makeText(getApplicationContext(), "hello"+t, Toast.LENGTH_SHORT).show();
                break;

            case R.id.about_us_item:
                fragment = new AboutUsFragment();
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
            default:
                fragment = new HomeFragment();
        }

//        if (id == R.id.all_places_item) {
//            fragment = new HomeFragment();
//        } else if (id == R.id.contact_us_item) {
//            fragment = new ContactUsFragment();
//
//        } else if (id == R.id.about_us_item) {
//            fragment = new AboutUsFragment();
//
//        } else if (id == R.id.rate_us_item) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//            }
//
//        } else if (id == R.id.more_apps_item) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market")));
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
//            }
//
//        } else {
//            fragment = new HomeFragment();
//        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.share_icon:
//
//                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
//                String app_url = " https://play.google.com/store/apps/details?id=" + getPackageName();
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
//                startActivity(Intent.createChooser(shareIntent, "Share via"));
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawer);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
            } else if (ContactUsFragment.backpressedlistener != null) {
                ContactUsFragment.backpressedlistener.onBackPressed();

            } else if (AboutUsFragment.backpressedlistener != null) {
                AboutUsFragment.backpressedlistener.onBackPressed();
            } else if (SearchBoxFragment.backpressedlistener != null) {
                SearchBoxFragment.backpressedlistener.onBackPressed();
            }
            else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        }
    }


}

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.Menu;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.WindowManager;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.viewpager.widget.ViewPager;
//
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.tabs.TabLayout;
//
//public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//
//    private long pressedTime;
//    DrawerLayout drawerLayout;
//    ActionBarDrawerToggle toggle;
//    NavigationView navigationView;
//
//    private ViewPagerAdapter viewPagerAdapter;
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_home);
//
//
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        drawerLayout = findViewById(R.id.drawer);
//        navigationView = findViewById(R.id.nav_view);
//
//
//        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.setDrawerIndicatorEnabled(true);
//        toggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setHomeButtonEnabled(true);
//
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//
//        viewPager = findViewById(R.id.viewpager);
//
//        // setting up the adapter
//        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        // add the fragments
//        viewPagerAdapter.add(new Page1(), "ALL PLACES");
//        viewPagerAdapter.add(new Page2(), "CATEGORIES");
//        viewPagerAdapter.add(new Page3(), "FAVOURITES");
//
//        // Set the adapter
//        viewPager.setAdapter(viewPagerAdapter);
//
//        tabLayout = findViewById(R.id.tablayout);
//        tabLayout.setupWithViewPager(viewPager);
//
//
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.all_places_item) {
//            viewPager.setCurrentItem(0);
//
//
//        } else if (id == R.id.categories_item) {
//            viewPager.setCurrentItem(1);
//
//
//        } else if (id == R.id.favourite_places_item) {
//            viewPager.setCurrentItem(2);
//
//
//        } else if (id == R.id.contact_us_item) {
//            Intent activityIntent = new Intent(getApplicationContext(), ContactUsActivity.class);
//            startActivity(activityIntent);
//            finish();
//
//
//        } else if (id == R.id.rate_us_item) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
//            }
//
//        } else if (id == R.id.about_us_item) {
//            Intent activityIntent = new Intent(getApplicationContext(), AboutUsActivity.class);
//            startActivity(activityIntent);
//            finish();
//
//        } else if (id == R.id.more_apps_item) {
//            try {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market")));
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")));
//            }
//
//        }
//
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.share_icon:
//
//                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
//                String app_url = " https://play.google.com/store/apps/details?id=" + getPackageName();
//                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
//                startActivity(Intent.createChooser(shareIntent, "Share via"));
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        drawerLayout = findViewById(R.id.drawer);
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            if (pressedTime + 2000 > System.currentTimeMillis()) {
//                super.onBackPressed();
//                finish();
//            } else {
//                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
//            }
//            pressedTime = System.currentTimeMillis();
//        }
//    }
//
//
//}
//
//
//
