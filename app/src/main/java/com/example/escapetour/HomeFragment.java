package com.example.escapetour;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DatabaseReference;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "No Internet";
    public static HomeFragment backpressedlistener;
    private String mParam1;
    private String mParam2;
    ImageView imageView;
    private DatabaseReference img_db_reference;
    View view;
    String[] img_url = new String[10];
    RecyclerView recview;
    myadapter adapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    FloatingSearchView mSearchView;
    MaterialToolbar toolbar;
    ViewPager2 viewPager2;
    ViewPager viewPager;
    String searchText;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;

    public HomeFragment() {
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        toolbar = view.findViewById(R.id.home_toolbar);
        toolbar.setTitle("");
        drawerLayout = getActivity().findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar_menu_item();

        tabSetUp();

        return view;
    }


    public void tabSetUp() {
        viewPager2 = view.findViewById(R.id.home_viewpager);
        tabLayout = view.findViewById(R.id.home_tablayout);
        MyFragmentStateAdapter adapter = new MyFragmentStateAdapter(getActivity());
//
        adapter.addFragment(new ForYouFragment(), "For you");
        adapter.addFragment(new CategoriesFragment(), "Categories");


        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> tab.setText(adapter.getTitle(position))
        ).attach();
    }

    private void toolbar_menu_item() {
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

                    case R.id.search_view:

//                        Intent intent=new Intent(getActivity(),SearchActivity.class);
//                        startActivity(intent);


                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new SearchBoxFragment()).addToBackStack(null).commit();
                        break;

                }

                return true;
            }
        });
    }


//    public void img_taker() {
//
//        img_db_reference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("images");
//        img_db_reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (int i = 0, j = 1; i < 10; i++, j++) {
//                    img_url[i] = snapshot.child("img" + j).getValue(String.class);
//                }
//
//                imageSlider();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//
//    }


//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


//    public void onlineCheck() {
//        if (!isOnline()) {
////            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//            view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.main_home_content).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.retry_home_button).setVisibility(View.VISIBLE);
//
////            alert.setIcon(android.R.drawable.ic_dialog_alert)
////                    .setTitle("Internet Connection Alert")
////                    .setMessage("Please Check Your Internet Connection")
////                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialogInterface, int i) {
////
////                        }
////                    }).show();// Online
//        } else {
//            view.findViewById(R.id.no_internet).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.retry_home_button).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
//
//
//        }
//    }

//    public void imageSlider() {
//
//        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
//
//        SliderView sliderView = view.findViewById(R.id.slider);
//
//        for (int i = 0; i < 10; i++) {
//            sliderDataArrayList.add(new SliderData(img_url[i]));
//
//        }
//
//        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);
//
//        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
//
//        sliderView.setSliderAdapter(adapter);
//
//        sliderView.setScrollTimeInSec(3);
//
//        sliderView.setAutoCycle(true);
//
//        sliderView.startAutoCycle();
//
//    }

//    public boolean isOnline() {
//        boolean connected = false;
//        try {
//            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return connected;
//    }

}