package com.example.escapetour;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ForYouFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Internet";

    private String mParam1;
    private String mParam2;
    ImageView imageView;
    private DatabaseReference db_reference;
    View view;
    String[] img_url = new String[10];
    RecyclerView recview1, recview2, recview3, recview4;
    myadapter adapter;
    ProgressBar progressBar;
    GpsTracker gpsTracker;
    Double current_latitude, current_longitude;


    public ForYouFragment() {

    }

    public static ForYouFragment newInstance(String param1, String param2) {
        ForYouFragment fragment = new ForYouFragment();
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

        view = inflater.inflate(R.layout.fragment_for_you, container, false);

        Button retry_button = view.findViewById(R.id.retry_home_button);
//        progressBar = view.findViewById(R.id.ppbar);
        onlineCheck();
        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onlineCheck();
            }
        });

//        img_taker();

        recview1 = (RecyclerView) view.findViewById(R.id.recview1);
        recview2 = (RecyclerView) view.findViewById(R.id.recview2);
        recview3 = (RecyclerView) view.findViewById(R.id.recview3);
        recview4 = (RecyclerView) view.findViewById(R.id.recview4);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recview1.setLayoutManager(layoutManager);
        recview2.setLayoutManager(layoutManager2);
        recview3.setLayoutManager(layoutManager3);


        fetch_data_recview_1();
        fetch_data_recview_2();
        fetch_data_recview_3();


//        spinnerDistance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                String distanceSelected = distances[position];
//                // Parse the distanceSelected string to get the distance value
//                int distanceValue = parseDistance(distanceSelected);
//                // Call fetch_data_recview_1() and fetch_data_recview_2() methods with the new distance value
//                fetch_data_recview_1(distanceValue);
//                fetch_data_recview_2(distanceValue);
//                fetch_data_recview_3(distanceValue);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//        fetch_data_recview_3(dist);


        return view;
    }

//    private int parseDistance(String distanceString) {
//        switch (distanceString) {
//            case "< 1 km":
////                return 10;
//            case "1 - 5 km":
//                return 5;
//            case "5 - 10 km":
//                return 100;
//            case "> 10 km":
//                return 20;
//            default:
//                return 30;
//        }
//    }

    public void fetch_data_recview_1() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment").orderByChild("category").equalTo("Shopping malls and marts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            double latitude = item.getLatitude();
                            double longitude = item.getLongitude();
                            float distance = getLocation(latitude, longitude);
//                            if (distance < dist) {
                            data.add(item);
//                            }

                        }
                        if (data.size() == 0) {
                            view.findViewById(R.id.shopping_mall_txt).setVisibility(View.GONE);
                            recview1.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview1.setAdapter(adapterlocal);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

    }

    public void fetch_data_recview_2() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment").orderByChild("category").equalTo("Restaurants and cafes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            double latitude = item.getLatitude();
                            double longitude = item.getLongitude();
                            float distance = getLocation(latitude, longitude);
//                            if (distance < dist) {
                            data.add(item);
//                            }
                        }
                        if (data.size() == 0) {
                            view.findViewById(R.id.Restaurant_txt).setVisibility(View.GONE);
                            recview2.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview2.setAdapter(adapterlocal);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });

    }

    public void fetch_data_recview_3() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment").orderByChild("category").equalTo("Rivers and waterfalls")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            double latitude = item.getLatitude();
                            double longitude = item.getLongitude();
                            float distance = getLocation(latitude, longitude);
//                            if (distance < dist) {
                            data.add(item);
//                            }
                        }
                        if (data.size() == 0) {
                            view.findViewById(R.id.rivers_txt).setVisibility(View.GONE);
                            recview3.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview3.setAdapter(adapterlocal);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
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

    private float getLocation(Double latitude, Double longitude) {
        gpsTracker = new GpsTracker(getContext());
        float[] results = new float[1];
        if (gpsTracker.canGetLocation()) {
            current_latitude = gpsTracker.getLatitude();
            current_longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        Location.distanceBetween(latitude, longitude, current_latitude, current_longitude, results);
        return results[0] / 1000;
    }

    @Override
    public void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    public void onlineCheck() {
        if (!isOnline()) {
//            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
            view.findViewById(R.id.recview1).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            view.findViewById(R.id.main_home_content).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.ppbar).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.no_internet).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.recview1).setVisibility(View.VISIBLE);
            view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
//


        }
    }

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

    public boolean isOnline() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return connected;
    }


//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        if(position==1) {
//
//            fetch_data_recview_1(5);
//            fetch_data_recview_2(5);
//            fetch_data_recview_3(5);
//        }
//            else if(position==2){
//
//
//
//
//
//
////                fetch_data_recview_1(10);
//                fetch_data_recview_2(10);
////                fetch_data_recview_3(10);
//
//
//
////            case 3:
////                fetch_data_recview_1(15);
////                break;
////            case 4:
////                fetch_data_recview_1(20);
////                break;
////            case 5:
////                fetch_data_recview_1(30);
////                break;
////            case 6:
////                fetch_data_recview_1(50);
////                break;
////            case 7:
////                fetch_data_recview_1(100);
////                break;
////            case 8:
////                fetch_data_recview_1(200);
////                break;
////            case 9:
////                fetch_data_recview_1(500);
////                break;
////            case 10:
////                fetch_data_recview_1(1000);
////                break;
//
//
//        }
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
