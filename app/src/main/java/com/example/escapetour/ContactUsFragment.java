package com.example.escapetour;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;


public class ContactUsFragment extends Fragment implements Backpressedlisterner {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public static ContactUsFragment backpressedlistener;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    View view;

    public ContactUsFragment() {

    }


    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        backpressedlistener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contact_us, container, false);
//        Toolbar toolbar = view.findViewById(R.id.normal_toolbar);

        MaterialToolbar toolbar = view.findViewById(R.id.normal_toolbar);
        toolbar.setTitle("Contact Us");

        drawerLayout = getActivity().findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_icon:

                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
                        String app_url = " https://play.google.com/store/apps/details?id=" + getActivity().getPackageName();
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                        break;

                }

                return true;
            }
        });
        return view;
    }


    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else {
                onBackPressed();
            }
        }

    }
}
//    @Override
//    public void onBackPressed() {
//        // Get the current fragment manager
//        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//
//        // Check if there are any fragments in the back stack
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            // Pop the top fragment off the back stack
//            fragmentManager.popBackStack();
//        } else {
//            // If there are no fragments in the back stack, call the super method to exit the app
//            onBackPressed();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//
//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.beginTransaction().replace(R.id.flContent)
//        } else {
//            onBackPressed();
//        }
////        AppCompatActivity activity = (AppCompatActivity) getContext();
////        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
//    }


