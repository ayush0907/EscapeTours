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
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback, WeatherApi.OnWeatherDataListener {

    String place_id;
    private GoogleMap mMap;
    TextView holder6, holder7, holder8, holder9, holder10, holder11, holder12, measure_distance, temperature_text_view, temperature_min_text_view, temperature_max_text_view;
    LatLng point, current_point;
    TextView humidity_text_view, temperature_condition;
    double current_latitude, latitude1;
    double current_longitude, longitude1;
    float[] results = new float[1];
    private GpsTracker gpsTracker;
    LinearLayout linearLayout;
    String[] img_url = new String[5];
    Button map_view_button, map_navigate_button;
    private DatabaseReference dbreference;
    DescriptionModel dpm = new DescriptionModel();
    ImageView weatherIcon;
    Query query;
    String temperature_string;

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        place_id = getIntent().getExtras().get("place_id").toString();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        dbreference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference();
        query = dbreference.child("entertainment").child(place_id);
//        temperature = findViewById(R.id.temperature);
        viewSet();
        Toolbar toolbar = findViewById(R.id.normal_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        try {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        getLocation();

//
        map_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationMapActivity.class);
                intent.putExtra("point", point);
                intent.putExtra("name", dpm.getName());
                startActivity(intent);
            }
        });

        map_navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr=" + dpm.getAddress();
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);
//                String uri = "http://maps.google.com/maps?q=" + dpm.getLatitude() + "," + dpm.getLongitude();
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                startActivity(intent);
//                String plusCode = "89XX+948, Jahaj Mahal Internal Rd, Mandu, Mandav, Madhya Pradesh 454010";
//


            }
        });

        dataFetch();


//        findViewById(R.id.progressBar).setVisibility(View.GONE);

        mapFragment.getMapAsync(this);
    }


    private void getLocation() {
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {
            current_latitude = gpsTracker.getLatitude();
            current_longitude = gpsTracker.getLongitude();

        } else {

//            gpsTracker.showSettingsAlert();
        }
    }

    public void dataFetch() {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                dpm = snapshot.getValue(DescriptionModel.class);
                if (dpm != null) {
                    img_url = new String[dpm.getImages()];
                    for (int i = 0, j = 1; i < dpm.getImages(); i++, j++) {
                        img_url[i] = snapshot.child("img" + j + "url").getValue(String.class);
                    }

                }
                point = new LatLng(dpm.getLatitude(), dpm.getLongitude());
//                WeatherApiClient Weatherdata=new WeatherApiClient();
//                Weatherdata.getCurrentWeather(dpm.getLatitude(), dpm.getLongitude()) ;

                latitude1 = dpm.getLatitude();
                longitude1 = dpm.getLongitude();

                imageSlider();
                valueSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MyApp", "DatabaseError in onCancelled: " + databaseError.getMessage());
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
                super.onBackPressed();
//
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void valueSet() {
        WeatherApi.getCurrentWeather(latitude1, longitude1, this);
        holder6.setText(dpm.getName());
//        temperature.setText(temperature_string);
        holder12.setText(dpm.getDescription());
        holder7.setText(dpm.getFacilities());
        holder8.setText(dpm.getHours());
        holder9.setText(dpm.getAddress());
        holder10.setText(dpm.getPhone());
        holder11.setText(dpm.getWebsite());

        getSupportActionBar().setTitle(dpm.getName());
//        findViewById(R.id.progressBar).setVisibility(View.GONE);

    }

    public void viewSet() {
        humidity_text_view = findViewById(R.id.humidity);
        measure_distance = findViewById(R.id.measure_distance);
        map_view_button = findViewById(R.id.map_view_button);
        map_navigate_button = findViewById(R.id.map_navigate_button);
        holder6 = findViewById(R.id.holder6);
        holder7 = findViewById(R.id.holder7);
        holder8 = findViewById(R.id.holder8);
        holder9 = findViewById(R.id.holder9);
        holder10 = findViewById(R.id.holder10);
        holder11 = findViewById(R.id.holder11);
        holder12 = findViewById(R.id.holder12);
        linearLayout = findViewById(R.id.linear_layout_temperature);
        temperature_text_view = findViewById(R.id.temperature);
//        temperature_condition = findViewById(R.id.temperature_condition);
        // Assuming that you have an ImageView with id "weatherIcon"
        weatherIcon = findViewById(R.id.weatherIcon);


//        temperature_min_text_view = findViewById(R.id.temperature_min_text);
//        temperature_max_text_view = findViewById(R.id.temperature_max_text);


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
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation();
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap = googleMap;
        current_point = new LatLng(current_latitude, current_longitude);

        Location.distanceBetween(dpm.getLatitude(), dpm.getLongitude(), current_latitude, current_longitude, results);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            measure_distance.setVisibility(View.GONE);
        } else {
            measure_distance.setText(String.format("%.2f", results[0] / 1000) + " KM Away");
        }
        mMap.addPolyline((new PolylineOptions()).add(current_point, point).width(10).color(Color.BLUE).geodesic(true));
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
                markerOptions.position(point);
                markerOptions.title(dpm.getName());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                googleMap.addMarker(markerOptions);
            }
        });

    }

    BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onWeatherDataReceived(double temperature, int humidity, String icon) {
        // Update the UI with the temperature data
        String iconUrl = String.format(Locale.US, "https://openweathermap.org/img/w/%s.png", icon);
        Glide.with(this).load(iconUrl).into(weatherIcon);
//        temperature_condition.setText(condition);
//        temperature_text_view.setText(String.format("%.1f", ) + " KM Away");
        String temperatureString = String.format(Locale.getDefault(), "Temperature: %.1f°C", temperature);
//        String temp_max_string = String.format(Locale.getDefault(), "%.1f°C", temp_max);
//        String temp_min_string = String.format(Locale.getDefault(), "%.1f°C", temp_min);
        String humidity_string = String.format(Locale.getDefault(), "Humidity: %d%%", humidity);
        temperature_text_view.setText(temperatureString);
//        temperature_max_text_view.setText(temp_max_string);
//        temperature_min_text_view.setText(temp_min_string);
        humidity_text_view.setText(humidity_string);
//        temperature_text_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWeatherDataError(String error) {
//        temperature_text_view.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        // Handle any errors that occur during the weather data request
//        Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

//    public class WeatherApiClient {
//        private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
//        private static final String APP_ID = "YOUR_API_KEY";
//
//        public void getCurrentWeather(double latitude, double longitude) {
//            String url = String.format(Locale.US, "%s?lat=%f&lon=%f&units=metric&appid=%s", BASE_URL, latitude, longitude, APP_ID);
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONObject main = response.getJSONObject("main");
//                                double temperature = main.getDouble("temp");
//                                temperature_string = String.valueOf(temperature);
//                                int humidity = main.getInt("humidity");
//                                JSONArray weatherArray = response.getJSONArray("weather");
//                                JSONObject weather = weatherArray.getJSONObject(0);
//                                String description = weather.getString("description");
//                                String icon = weather.getString("icon");
////                                temperature=temperature;
////                                listener.onWeatherDataReceived(temperature, humidity, description, icon);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
////                                listener.onWeatherDataError("Failed to parse weather data");
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            error.printStackTrace();
////                            listener.onWeatherDataError("Failed to fetch weather data");
//                        }
//                    });
////            Volley.newRequestQueue(listener.getContext()).add(request);
//        }
//
//
//    }


}


//interface OnWeatherDataListener {
//    void onWeatherDataReceived(double temperature, int humidity, String description, String icon);
//
//    void onWeatherDataError(String errorMessage);
//
//    Context getContext();
//}