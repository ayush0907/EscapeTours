package com.example.escapetour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayList;

public class ContactUsActivity extends AppCompatActivity {

    String place_id;
    private GoogleMap mMap;
    TextView holder6, holder7, holder8, holder9, holder10, holder11, holder12, measure_distance;
    LatLng point, current_point;
    double current_latitude;
    double current_longitude;
    float[] results = new float[1];
    private GpsTracker gpsTracker;
    String[] img_url = new String[5];
    Button map_view_button, map_navigate_button;
    private DatabaseReference dbreference;
    DescriptionModel dpm = new DescriptionModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        place_id = getIntent().getExtras().get("place_id").toString();
        findViewById(R.id.map12).setVisibility(View.VISIBLE);
//        measure_distance = findViewById(R.id.measure_distance);
//        map_view_button = findViewById(R.id.map_view_button);
//        map_navigate_button = findViewById(R.id.map_navigate_button);
//        getLocation();

//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Toolbar toolbar = findViewById(R.id.normal_toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        viewSet();
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map12);
//        mapFragment.getMapAsync(this);
////
//
//        map_view_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LocationMapActivity.class);
//                intent.putExtra("point", point);
//                intent.putExtra("name", dpm.getName());
//                startActivity(intent);
//            }
//        });
//
//
//        map_navigate_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "http://maps.google.com/maps?daddr=" + dpm.getAddress();
//                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                startActivity(intent1);
//
//            }
//        });
//
//        dataFetch();
//        findViewById(R.id.progressBar).setVisibility(View.GONE);

    }


//    private void getLocation() {
//        gpsTracker = new GpsTracker(this);
//        if (gpsTracker.canGetLocation()) {
//            current_latitude = gpsTracker.getLatitude();
//            current_longitude = gpsTracker.getLongitude();
//
//        } else {
//
////            gpsTracker.showSettingsAlert();
//        }
//    }
//
//    public void dataFetch() {
//        dbreference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment").child(place_id);
//        dbreference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                dpm = snapshot.getValue(DescriptionModel.class);
//                if (dpm != null) {
//                    img_url = new String[dpm.getImages()];
//                    for (int i = 0, j = 1; i < dpm.getImages(); i++, j++) {
//                        img_url[i] = snapshot.child("img" + j + "url").getValue(String.class);
//                    }
//
//                }
////                imageSlider();
////                valueSet();
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("MyApp", "DatabaseError in onCancelled: " + databaseError.getMessage());
//            }
//        });
//
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.normal_menu, menu);
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
//
//            case android.R.id.home:
//                super.onBackPressed();
////
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    public void valueSet() {
//
//        holder6.setText(dpm.getName());
//        holder7.setText(dpm.getFacilities());
//        holder8.setText(dpm.getHours());
//        holder9.setText(dpm.getAddress());
//        holder10.setText(dpm.getPhone());
//        holder11.setText(dpm.getWebsite());
//        holder12.setText(dpm.getDescription());
//        getSupportActionBar().setTitle(dpm.getName());
////        findViewById(R.id.progressBar).setVisibility(View.GONE);
//
//    }
//
//    public void viewSet() {
//        holder6 = findViewById(R.id.holder6);
//        holder7 = findViewById(R.id.holder7);
//        holder8 = findViewById(R.id.holder8);
//        holder9 = findViewById(R.id.holder9);
//        holder10 = findViewById(R.id.holder10);
//        holder11 = findViewById(R.id.holder11);
//        holder12 = findViewById(R.id.holder12);
//
//    }
//
//    public void imageSlider() {
//
//        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
//
//        SliderView sliderView = findViewById(R.id.slider_description);
//
//        for (int i = 0; i < dpm.getImages(); i++) {
//            sliderDataArrayList.add(new SliderData(img_url[i]));
//        }
//        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
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
//
//
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        current_point = new LatLng(current_latitude, current_longitude);
//        point = new LatLng(dpm.getLatitude(), dpm.getLongitude());
//        Location.distanceBetween(dpm.getLatitude(), dpm.getLongitude(), current_latitude, current_longitude, results);
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            measure_distance.setVisibility(View.GONE);
//        } else {
//            measure_distance.setText(String.format("%.2f", results[0] / 1000) + " KM Away");
//        }
//        mMap.addPolyline((new PolylineOptions()).add(current_point, point).width(10).color(Color.BLUE).geodesic(true));
//        mMap.addMarker(new MarkerOptions().position(current_point).title("I am here"));
//        Marker destination_marker = mMap.addMarker(new MarkerOptions().position(point).title(dpm.getName()).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
//        destination_marker.showInfoWindow();
//
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                // When clicked on map
//                // Initialize marker options
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(point);
//                markerOptions.title(dpm.getName());
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
//                googleMap.addMarker(markerOptions);
//            }
//        });
//
//    }
//
//    BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(bitmap);
//
//        vectorDrawable.draw(canvas);
//
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }


}