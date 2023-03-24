package com.example.escapetour;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ForYouFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Internet";

    private String mParam1;
    private String mParam2;
    ImageView imageView;
    private FirebaseDatabase db_reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseApp firebaseApp;
    View view;
    String[] img_url = new String[10];
    RecyclerView recview1, recview2, recview3, recview4, recview5, recview6;
    myadapter adapter;
    ProgressBar main_progress_bar;
    DatabaseReference myRef;
    GpsTracker gpsTracker;
    Double current_latitude, current_longitude;


    public ForYouFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();


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
        firebaseDatabase = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com");
        myRef = firebaseDatabase.getReference("entertainment");
        Button retry_button = view.findViewById(R.id.retry_home_button);
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
        recview5 = (RecyclerView) view.findViewById(R.id.recview5);
        recview5 = (RecyclerView) view.findViewById(R.id.recview5);
        recview6 = (RecyclerView) view.findViewById(R.id.recview6);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getContext(), 2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager5 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager6 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recview1.setLayoutManager(layoutManager);
        recview2.setLayoutManager(layoutManager2);
        recview3.setLayoutManager(layoutManager3);
        recview4.setLayoutManager(layoutManager4);
        recview5.setLayoutManager(layoutManager5);
        recview6.setLayoutManager(layoutManager6);


//        fetch_data_recview_1();
//        fetch_data_recview_2();
//        fetch_data_recview_3();
//        fetch_data_recview_4();
//        fetch_data_recview_5();
//        fetch_data_recview_6();

//        main_progress_bar.setVisibility(View.GONE);

        return view;
    }

    public void fetch_data_recview_1() {

        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                Set<String> addedNodeIds = new HashSet<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    model item = childSnapshot.getValue(model.class);
                    String nodeId = childSnapshot.getKey();
                    if (!addedNodeIds.contains(nodeId)) { //good
                        double latitude = item.getLatitude();
                        double longitude = item.getLongitude();
                        float distance = getLocation(latitude, longitude);
                        if (distance < 30) {
                            data.add(item);
                            addedNodeIds.add(nodeId);
                        }
                    }
                }
                if (data.size() == 0) {
                    view.findViewById(R.id.shopping_mall_txt).setVisibility(View.GONE);
                    view.findViewById(R.id.place_near_button).setVisibility(View.GONE);
                    recview1.setVisibility(View.GONE);
                } else {
                    adapterlocal.setItems(data);
                    recview1.setAdapter(adapterlocal);
                    view.findViewById(R.id.shopping_mall_txt).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.place_near_button).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());

            }
        });

    }

    public void fetch_data_recview_2() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        myRef.orderByChild("cat").equalTo("popular").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                Set<String> addedNodeIds = new HashSet<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    model item = childSnapshot.getValue(model.class);
                    String nodeId = childSnapshot.getKey();
                    if (!addedNodeIds.contains(nodeId)) {
                        data.add(item);
                    }
//
                }
                if (data.size() == 0) {
                    view.findViewById(R.id.Restaurant_txt).setVisibility(View.GONE);
                    recview2.setVisibility(View.GONE);
                } else {
                    adapterlocal.setItems(data);
                    view.findViewById(R.id.Restaurant_txt).setVisibility(View.VISIBLE);
                    recview2.setAdapter(adapterlocal);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
            }
        });

    }

    public void fetch_data_recview_3() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        myRef.orderByChild("cat").equalTo("nature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                Set<String> addedNodeIds = new HashSet<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    model item = childSnapshot.getValue(model.class);
                    String nodeId = childSnapshot.getKey();
                    if (!addedNodeIds.contains(nodeId)) {
                        data.add(item);
                    }
                }
                if (data.size() == 0) {
                    view.findViewById(R.id.rivers_txt).setVisibility(View.GONE);
                    recview3.setVisibility(View.GONE);
                } else {
                    adapterlocal.setItems(data);
                    view.findViewById(R.id.rivers_txt).setVisibility(View.VISIBLE);
                    recview3.setAdapter(adapterlocal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
            }
        });
    }

    private void fetch_data_recview_4() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        myRef.orderByChild("cat").equalTo("shopping").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                data.clear();
                Set<String> addedNodeIds = new HashSet<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    model item = childSnapshot.getValue(model.class);
                    String nodeId = childSnapshot.getKey();
                    if (!addedNodeIds.contains(nodeId)) {
                        data.add(item);
                    }
                }
                if (data.size() == 0) {
                    view.findViewById(R.id.supermarket_txt).setVisibility(View.GONE);
                    recview4.setVisibility(View.GONE);
                } else {
                    adapterlocal.setItems(data);
                    view.findViewById(R.id.supermarket_txt).setVisibility(View.VISIBLE);
                    recview4.setAdapter(adapterlocal);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
            }
        });
    }

    private void fetch_data_recview_5() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);

        myRef.orderByChild("cat").equalTo("history")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.clear();
                        Set<String> addedNodeIds = new HashSet<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            String nodeId = childSnapshot.getKey();
                            if (!addedNodeIds.contains(nodeId)) {
                                data.add(item);
//
                            }
                        }
                        if (data.size() == 0) {
                            view.findViewById(R.id.historical_places_txt).setVisibility(View.GONE);
                            recview5.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            view.findViewById(R.id.historical_places_txt).setVisibility(View.VISIBLE);
                            recview5.setAdapter(adapterlocal);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
                    }
                });
    }

    private void fetch_data_recview_6() {
        List<model> data = new ArrayList<>();
        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data);
        myRef.orderByChild("cat").equalTo("kids")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.clear();
                        Set<String> addedNodeIds = new HashSet<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            String nodeId = childSnapshot.getKey();
                            if (!addedNodeIds.contains(nodeId)) {
                                data.add(item);
                            }
                        }
                        if (data.size() == 0) {
                            view.findViewById(R.id.wildlife_txt).setVisibility(View.GONE);
                            recview6.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            view.findViewById(R.id.wildlife_txt).setVisibility(View.VISIBLE);
                            recview6.setAdapter(adapterlocal);
                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
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
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    public void onlineCheck() {
        if (!isOnline()) {
//
            view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.recview1).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.main_home_content).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.main_home_content).setVisibility(View.GONE);
//            view.findViewById(R.id.ppbar).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.no_internet).setVisibility(View.GONE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.GONE);
            view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);

            fetch_data_recview_1();
            fetch_data_recview_2();
            fetch_data_recview_3();
            fetch_data_recview_4();
            fetch_data_recview_5();
            fetch_data_recview_6();

//            view.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
//            view.findViewById(R.id.recview1).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
//


        }
    }


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


}
