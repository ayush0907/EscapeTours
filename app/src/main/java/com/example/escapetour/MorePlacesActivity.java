package com.example.escapetour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MorePlacesActivity extends AppCompatActivity {
    RecyclerView recview;
    GpsTracker gpsTracker;
    Double current_latitude, current_longitude;
    String cat, title;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_places);

        cat = getIntent().getExtras().get("cat").toString();
        title = getIntent().getExtras().get("title").toString();

        recview = findViewById(R.id.recview1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recview.setLayoutManager(gridLayoutManager);

        toolbar = findViewById(R.id.normal_toolbar);
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_icon:
                        // Create an intent to share the app's Play Store link
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
                        String app_url = " https://play.google.com/store/apps/details?id=" + getPackageName();
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                        startActivity(Intent.createChooser(shareIntent, "Share via"));
                        break;
                }

                return true;
            }
        });
        if (cat.equals("near_you")) {
            fetch_data_recview_2();
        } else {
            fetch_data_recview_1();
        }


    }

    public void fetch_data_recview_1() {
        List<model> data = new ArrayList<>();
        CategoryLocalAdapter adapterlocal = new CategoryLocalAdapter(this, data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment").orderByChild("cat").equalTo(cat)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            data.add(item);
                        }
                        if (data.size() == 0) {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            recview.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview.setAdapter(adapterlocal);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
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
        CategoryLocalAdapter adapterlocal = new CategoryLocalAdapter(this, data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        data.clear();
                        Set<String> addedNodeIds = new HashSet<>();
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            model item = childSnapshot.getValue(model.class);
                            String nodeId = childSnapshot.getKey();
                            if (!addedNodeIds.contains(nodeId)) {
                                double latitude = item.getLatitude();
                                double longitude = item.getLongitude();
                                float distance = getLocation(latitude, longitude);
                                if (distance < 50) {
                                    data.add(item);
                                    addedNodeIds.add(nodeId);
                                }
                            }
                        }
                        if (data.size() == 0) {
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                            recview.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview.setAdapter(adapterlocal);
                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyApp", "DatabaseError in onCancelled: " + error.getMessage());
                    }
                });

    }

    private float getLocation(Double latitude, Double longitude) {
        gpsTracker = new GpsTracker(this);
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
}