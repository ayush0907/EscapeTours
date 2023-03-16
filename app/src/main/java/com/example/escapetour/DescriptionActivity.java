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

import java.util.ArrayList;

public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "No Data";
    String name, address, description, facilities, website, phone, hours, place_id;
    int images;
    private GoogleMap mMap;
    Document doc;
    TextView holder6, holder7, holder8, holder9, holder10, holder11, holder12, measure_distance;
    LatLng point, current_point;
    double latitude;
    double longitude;
    double current_latitude;
    double current_longitude;
    //    String distance;
    float[] results = new float[1];
    private GpsTracker gpsTracker;
    String[] img_url = new String[5];
    Button map_view_button, map_navigate_button;
    private DatabaseReference dbreference;
    LatLng start, end;
    GoogleMap map;
    MapFragment map1 = null;
    String parsedDistance;
    String response;
    DescriptionModel dpm = new DescriptionModel();
    String TotalDistance, TotalDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLocation();


        Toolbar toolbar = findViewById(R.id.normal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Description");


        viewSet();

        place_id = getIntent().getExtras().get("place_id").toString();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map12);
        mapFragment.getMapAsync(this);
//
        measure_distance = findViewById(R.id.measure_distance);

//        gd = new GoogleDirection(this);


        map_view_button = findViewById(R.id.map_view_button);
        map_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationMapActivity.class);
                intent.putExtra("point", point);
                intent.putExtra("name", dpm.getName());
                startActivity(intent);
            }
        });

        map_navigate_button = findViewById(R.id.map_navigate_button);
        map_navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr=" + dpm.getAddress();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);

            }
        });


        dataFetch();

    }


    private void getLocation() {
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            current_latitude = gpsTracker.getLatitude();
            current_longitude = gpsTracker.getLongitude();

        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void dataFetch() {
        dbreference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment").child(place_id);
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dpm = snapshot.getValue(DescriptionModel.class);
                if (dpm != null) {
                    img_url = new String[dpm.getImages()];
                    for (int i = 0, j = 1; i < dpm.getImages(); i++, j++) {
                        img_url[i] = snapshot.child("img" + j + "url").getValue(String.class);
                    }

                }
//                name = snapshot.child("name").getValue(String.class);
//                address = snapshot.child("address").getValue(String.class);
//                description = snapshot.child("description").getValue(String.class);
//                hours = snapshot.child("hours").getValue(String.class);
//                facilities = snapshot.child("facilities").getValue(String.class);
//                website = snapshot.child("website").getValue(String.class);
//                images = snapshot.child("images").getValue(Integer.class);
//
//                for (int i = 0, j = 1; i < images; i++, j++) {
//                    img_url[i] = snapshot.child("img" + j + "url").getValue(String.class);
//                }
////                img1url = snapshot.child("img1url").getValue(String.class);
////                img2url = snapshot.child("img2url").getValue(String.class);
////                img3url = snapshot.child("img3url").getValue(String.class);
////                img4url = snapshot.child("img4url").getValue(String.class);
////                img5url = snapshot.child("img5url").getValue(String.class);
//                phone = snapshot.child("phone").getValue(String.class);
//                latitude = snapshot.child("latitude").getValue(Double.class);
//                longitude = snapshot.child("longitude").getValue(Double.class);
                imageSlider();
                valueSet();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.normal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_icon:

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Escape Tour App Link");
                String app_url = " https://play.google.com/store/apps/details?id=" + getPackageName();
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
                break;

            case android.R.id.home:
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void valueSet() {

        holder6.setText(dpm.getName());
        holder7.setText(dpm.getFacilities());
        holder8.setText(dpm.getHours());
        holder9.setText(dpm.getAddress());
        holder10.setText(dpm.getPhone());
        holder11.setText(dpm.getWebsite());
        holder12.setText(dpm.getDescription());

    }

    public void viewSet() {
        holder6 = findViewById(R.id.holder6);
        holder7 = findViewById(R.id.holder7);
        holder8 = findViewById(R.id.holder8);
        holder9 = findViewById(R.id.holder9);
        holder10 = findViewById(R.id.holder10);
        holder11 = findViewById(R.id.holder11);
        holder12 = findViewById(R.id.holder12);

    }

    public void imageSlider() {

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        SliderView sliderView = findViewById(R.id.slider_description);

        for (int i = 0; i < dpm.getImages(); i++) {
            sliderDataArrayList.add(new SliderData(img_url[i]));
        }
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);

        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();

    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        point = new LatLng(dpm.getLatitude(), dpm.getLongitude());
        current_point = new LatLng(current_latitude, current_longitude);
        Location.distanceBetween(dpm.getLatitude(), dpm.getLongitude(), current_latitude, current_longitude, results);
        measure_distance.setText(String.format("%.2f", results[0] / 1000) + " KM Away");
        mMap.addPolyline((new PolylineOptions()).add(current_point, point).width(10).color(Color.BLUE).geodesic(true));
//        Toast.makeText(this, "Distance "+distance1, Toast.LENGTH_LONG).show();


//        Toast.makeText(this, "Hello"+ Arrays.toString(results), Toast.LENGTH_SHORT).show();
//        distance = SphericalUtil.computeDistanceBetween(current_point,point);
//        Location.distanceBetween(current_latitude, current_longitude,latitude, longitude, results);
//        Toast.makeText(this, "Hello" + results[0], Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(current_point).title("I am here"));
        Marker destination_marker = mMap.addMarker(new MarkerOptions().position(point).title(dpm.getName()).icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_baseline_flag_24)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
        destination_marker.showInfoWindow();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // When clicked on map
                // Initialize marker options
                MarkerOptions markerOptions = new MarkerOptions();

                // Set position of marker
                markerOptions.position(point);
                // Set title of marker
                markerOptions.title(dpm.getName());
                // Remove all marker
                // Animating to zoom the marker
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                // Add marker on map
                googleMap.addMarker(markerOptions);
            }
        });

    }

    BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}