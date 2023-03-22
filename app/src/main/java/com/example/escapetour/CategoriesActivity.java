package com.example.escapetour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    RecyclerView recview;
    String category_id, category_title;
    GpsTracker gpsTracker;
    Double current_latitude, current_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        category_title = getIntent().getExtras().get("category_title").toString();
        category_id = getIntent().getExtras().get("category_id").toString();

        MaterialToolbar toolbar = findViewById(R.id.normal_toolbar);
        toolbar.setTitle(category_title);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        recview = (RecyclerView) findViewById(R.id.recview1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recview.setLayoutManager(gridLayoutManager);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoriesActivity.super.onBackPressed();

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_icon:

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
        fetch_data_recview_1();
    }

    public void fetch_data_recview_1() {
        List<model> data = new ArrayList<>();
        CategoryLocalAdapter adapterlocal = new CategoryLocalAdapter(this, data);
        FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference("entertainment").orderByChild("category").equalTo(category_id)
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
                            findViewById(R.id.shopping_mall_txt).setVisibility(View.GONE);
                            recview.setVisibility(View.GONE);
                        } else {
                            adapterlocal.setItems(data);
                            recview.setAdapter(adapterlocal);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
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