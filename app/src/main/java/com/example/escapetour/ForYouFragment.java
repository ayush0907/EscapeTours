package com.example.escapetour;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.provider.Settings;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ForYouFragment<REQUEST_CODE_PERMISSIONS> extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "Internet";

    private String mParam1;
    private String mParam2;
    FirebaseDatabase firebaseDatabase;
    View view;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    String[] img_url = new String[10];
    RecyclerView recview1, recview2, recview3, recview4, recview5, recview6;
    ProgressBar main_progress_bar;
    Query query1, query2, query3, query4, query5, query6;
    DatabaseReference myRef;
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    GpsTracker gpsTracker;
    RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6;
    Double current_latitude, current_longitude;


    public ForYouFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
//        gpsTracker.getLocation();

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
        main_progress_bar = view.findViewById(R.id.progressBar);
        query1 = myRef;
        query2 = myRef.orderByChild("cat").equalTo("popular");
        query3 = myRef.orderByChild("cat").equalTo("nature");
        query4 = myRef.orderByChild("cat").equalTo("shopping");
        query5 = myRef.orderByChild("cat").equalTo("history");
        query6 = myRef.orderByChild("cat").equalTo("kids");


        Button retry_button = view.findViewById(R.id.retry_home_button);
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

        rl1 = view.findViewById(R.id.place_near_you_upper_layout);
        rl2 = view.findViewById(R.id.popular_places_upper_layout);
        rl3 = view.findViewById(R.id.talk_with_nature_upper_layout);
        rl4 = view.findViewById(R.id.go_for_shopping_upper_layout);
        rl5 = view.findViewById(R.id.for_history_lovers_upper_layout);
        rl6 = view.findViewById(R.id.kids_section_upper_layout);

        relative_listener();
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

        onlineCheck();
        view.findViewById(R.id.place_near_you_upper_layout).setVisibility(View.GONE);
        recview1.setVisibility(View.GONE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

        } else {
            recview1.setVisibility(View.VISIBLE);
//            fetch_data_recview_1();
            MyTask task1 = new MyTask(view, query1, recview1, R.id.place_near_you_upper_layout, view.getContext(), 1);
            task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

        Handler handler = new Handler();

        MyTask task2 = new MyTask(view, query2, recview2, R.id.popular_places_upper_layout, view.getContext(), 2);
        task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        MyTask task3 = new MyTask(view, query3, recview3, R.id.talk_with_nature_upper_layout, view.getContext(), 2);
        task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        MyTask task4 = new MyTask(view, query4, recview4, R.id.go_for_shopping_upper_layout, view.getContext(), 2);

        MyTask task5 = new MyTask(view, query5, recview5, R.id.for_history_lovers_upper_layout, view.getContext(), 2);

        MyTask task6 = new MyTask(view, query6, recview6, R.id.kids_section_upper_layout, view.getContext(), 2);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000);
//
        task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        task6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        return view;
    }

    private void relative_listener() {

        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "near_you");
                intent.putExtra("title", "Places near you");
                startActivity(intent);
            }
        });

        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "popular");
                intent.putExtra("title", "Popular places");
                startActivity(intent);
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "nature");
                intent.putExtra("title", "Talk with nature");
                startActivity(intent);
            }
        });

        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "shopping");
                intent.putExtra("title", "Go for shopping");
                startActivity(intent);
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "history");
                intent.putExtra("title", "For history lovers");
                startActivity(intent);
            }
        });
        rl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MorePlacesActivity.class);
                intent.putExtra("cat", "kids");
                intent.putExtra("title", "Kids section");
                startActivity(intent);
            }
        });

    }


    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    new ForYouFragment<>();

                    recview1.setVisibility(View.VISIBLE);
//                    fetch_data_recview_1();
                    MyTask task1 = new MyTask(view, query1, recview1, R.id.place_near_you_upper_layout, view.getContext(), 1);
                    task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    MyTask task2 = new MyTask(view, query2, recview2, R.id.popular_places_upper_layout, view.getContext(), 2);
                    task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    MyTask task3 = new MyTask(view, query3, recview3, R.id.talk_with_nature_upper_layout, view.getContext(), 2);
                    task3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    MyTask task4 = new MyTask(view, query4, recview4, R.id.go_for_shopping_upper_layout, view.getContext(), 2);
                    task4.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    MyTask task5 = new MyTask(view, query5, recview5, R.id.for_history_lovers_upper_layout, view.getContext(), 2);
                    task5.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    MyTask task6 = new MyTask(view, query6, recview6, R.id.kids_section_upper_layout, view.getContext(), 2);
                    task6.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            });


    public class MyTask extends AsyncTask<Void, Void, List<model>> {

        private DatabaseReference myRef;
        private Query query;
        private RecyclerView recyclerView;
        private int layout_id;
        private View fragmentView;
        private Context context;
        private int x;
        private RelativeLayout relativeLayout;

        public MyTask(View fragmentView, Query query, RecyclerView recyclerView, int layout_id, Context context, int x) {
            this.fragmentView = fragmentView;
            this.query = query;
            this.recyclerView = recyclerView;
            this.layout_id = layout_id;
            this.context = context;
            this.x = x;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragmentView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            fragmentView.findViewById(layout_id).setVisibility(View.GONE);
        }

        @Override
        protected List<model> doInBackground(Void... voids) {
            List<model> data = new ArrayList<>();
            relativeLayout = fragmentView.findViewById(layout_id);
            MyLocalAdapter adapter = new MyLocalAdapter(context, data, main_progress_bar);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    data.clear();
                    Set<String> addedNodeIds = new HashSet<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        model item = childSnapshot.getValue(model.class);
                        String nodeId = childSnapshot.getKey();
                        if (!addedNodeIds.contains(nodeId)) {
                            if (x == 1) {
                                double latitude = item.getLatitude();
                                double longitude = item.getLongitude();
                                float distance = getLocation(latitude, longitude);
                                if (distance < 50) {
                                    data.add(item);
                                    addedNodeIds.add(nodeId);
                                }
                            } else {
                                data.add(item);
                                addedNodeIds.add(nodeId);
                            }

                        }
                    }
                    if (data.size() == 0) {
                        View viewToHide = fragmentView.findViewById(layout_id);
                        viewToHide.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        adapter.setItems(data);
//                        Collections.shuffle(data); // shuffle the items after adding them to the list
                        recyclerView.setAdapter(adapter);
                    }
                    fragmentView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                    fragmentView.findViewById(layout_id).setVisibility(View.VISIBLE);

                    fragmentView.findViewById(R.id.no_internet).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.retry_home_button).setVisibility(View.GONE);
                    fragmentView.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
                }
            });

            return data;
        }

    }


//    public void fetch_data_recview_1() {
//
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                data.clear();
//                Set<String> addedNodeIds = new HashSet<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    model item = childSnapshot.getValue(model.class);
//                    String nodeId = childSnapshot.getKey();
//                    if (!addedNodeIds.contains(nodeId)) {
//                        double latitude = item.getLatitude();
//                        double longitude = item.getLongitude();
//                        float distance = getLocation(latitude, longitude);
//                        if (distance < 30) {
//                        data.add(item);
//                        addedNodeIds.add(nodeId);
//                        }
//                    }
//                }
//                if (data.size() == 0) {
//                    view.findViewById(R.id.place_near_you_upper_layout).setVisibility(View.GONE);
//                    recview1.setVisibility(View.GONE);
//                } else {
//                    adapterlocal.setItems(data);
//                    recview1.setAdapter(adapterlocal);
////                    view.findViewById(R.id.place_near_you_upper_layout).setVisibility(View.VISIBLE);
////                    view.findViewById(R.id.place_near_button).setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//
//            }
//        });
//
//    }

//    public void fetch_data_recview_2() {
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//        myRef.orderByChild("cat").equalTo("popular").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                data.clear();
//                Set<String> addedNodeIds = new HashSet<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    model item = childSnapshot.getValue(model.class);
////                    String nodeId = childSnapshot.getKey();
////                    if (!addedNodeIds.contains(nodeId)) {
//                    data.add(item);
////                    }
////
//                }
//                if (data.size() == 0) {
//                    view.findViewById(R.id.popular_places_upper_layout).setVisibility(View.GONE);
//                    recview2.setVisibility(View.GONE);
//                } else {
//                    adapterlocal.setItems(data);
//                    recview2.setAdapter(adapterlocal);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//            }
//        });
//
//    }

//    public void fetch_data_recview_3() {
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//        myRef.orderByChild("cat").equalTo("nature").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                data.clear();
//                Set<String> addedNodeIds = new HashSet<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    model item = childSnapshot.getValue(model.class);
//                    String nodeId = childSnapshot.getKey();
//                    if (!addedNodeIds.contains(nodeId)) {
//                        data.add(item);
//                    }
//                }
//                if (data.size() == 0) {
//                    view.findViewById(R.id.talk_with_nature_upper_layout).setVisibility(View.GONE);
//                    recview3.setVisibility(View.GONE);
//                } else {
//                    adapterlocal.setItems(data);
//                    recview3.setAdapter(adapterlocal);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//            }
//        });
//    }

//    private void fetch_data_recview_4() {
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//        myRef.orderByChild("cat").equalTo("shopping").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                data.clear();
//                Set<String> addedNodeIds = new HashSet<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    model item = childSnapshot.getValue(model.class);
//                    String nodeId = childSnapshot.getKey();
//                    if (!addedNodeIds.contains(nodeId)) {
//                        data.add(item);
//                    }
//                }
//                if (data.size() == 0) {
//                    view.findViewById(R.id.go_for_shopping_upper_layout).setVisibility(View.GONE);
//                    recview4.setVisibility(View.GONE);
//                } else {
//                    adapterlocal.setItems(data);
//                    recview4.setAdapter(adapterlocal);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//            }
//        });
//    }

//    private void fetch_data_recview_5() {
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//
//        myRef.orderByChild("cat").equalTo("history")
//                .addValueEventListener(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        data.clear();
//                        Set<String> addedNodeIds = new HashSet<>();
//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            model item = childSnapshot.getValue(model.class);
//                            String nodeId = childSnapshot.getKey();
//                            if (!addedNodeIds.contains(nodeId)) {
//                                data.add(item);
////
//                            }
//                        }
//                        if (data.size() == 0) {
//                            view.findViewById(R.id.for_history_lovers_upper_layout).setVisibility(View.GONE);
//                            recview5.setVisibility(View.GONE);
//                        } else {
//                            adapterlocal.setItems(data);
//                            recview5.setAdapter(adapterlocal);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//                    }
//                });
//    }

//    private void fetch_data_recview_6() {
//        List<model> data = new ArrayList<>();
//        MyLocalAdapter adapterlocal = new MyLocalAdapter(getContext(), data, main_progress_bar);
//        myRef.orderByChild("cat").equalTo("kids")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        data.clear();
//                        Set<String> addedNodeIds = new HashSet<>();
//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            model item = childSnapshot.getValue(model.class);
//                            String nodeId = childSnapshot.getKey();
//                            if (!addedNodeIds.contains(nodeId)) {
//                                data.add(item);
//                            }
//                        }
//                        if (data.size() == 0) {
//                            view.findViewById(R.id.kids_section_upper_layout).setVisibility(View.GONE);
//                            recview6.setVisibility(View.GONE);
//                        } else {
//                            adapterlocal.setItems(data);
//                            recview6.setAdapter(adapterlocal);
//                            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
//                    }
//                });
//    }


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
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onStop() {
        super.onStop();
        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
//        adapter.stopListening();
    }

    public void onlineCheck() {
        if (!isOnline()) {
//
//            view.findViewById(R.id.progressBar).setVisibility(View.GONE);
            view.findViewById(R.id.no_internet).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.main_home_content).setVisibility(View.GONE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.VISIBLE);

        } else {
            view.findViewById(R.id.no_internet).setVisibility(View.GONE);
            view.findViewById(R.id.retry_home_button).setVisibility(View.GONE);
            view.findViewById(R.id.main_home_content).setVisibility(View.VISIBLE);
//            view.findViewById(R.id.progressBar).setVisibility(View.GONE);

//            fetch_data_recview_1();
//            fetch_data_recview_2();
//            fetch_data_recview_3();
//            fetch_data_recview_4();
//            fetch_data_recview_5();
//            fetch_data_recview_6();

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
