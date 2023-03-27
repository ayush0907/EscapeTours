package com.example.escapetour;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;


public class DetailsFragment extends Fragment implements Backpressedlisterner {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    String name, address, description, facilities, website, img1url, img2url, img3url, img4url, img5url, phone, hours;
    private GoogleMap mMap;
    int images;
    TextView holder6, holder7, holder8, holder9, holder10, holder11, holder12, measure_distance;
    ImageView holder1, holder2, holder3, holder4, holder5;
    LatLng point;
    Double latitude, longitutde;
    SupportMapFragment supportMapFragment;
    Button map_view_button, map_navigate_button;
    private DatabaseReference dbreference;
    private DrawerLayout drawerLayout;
    public static DetailsFragment backpressedlistener;
    private String mParam1;
    private String mParam2;
    String place_id;
    LatLng current_point;
    double current_latitude;
    double current_longitude;
    float[] results = new float[1];
    private GpsTracker gpsTracker;
    String[] img_url = new String[5];
    MaterialToolbar toolbar;


    public DetailsFragment() {

    }

    @Override
    public void onPause() {
        backpressedlistener = null;
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        backpressedlistener = this;
    }

    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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

    public DetailsFragment(String place_id) {
        this.place_id = place_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_details, container, false);
        toolbar = view.findViewById(R.id.normal_toolbar);
        measure_distance = view.findViewById(R.id.measure_distance);
        map_view_button = view.findViewById(R.id.map_view_button);
        map_navigate_button = view.findViewById(R.id.map_navigate_button);
        drawerLayout = getActivity().findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

        viewSet();

        dataSet();


        map_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationMapActivity.class);
                intent.putExtra("point", point);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        map_navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr=" + address;
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);
            }
        });

        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
        return view;
    }

    private void getLocation() {
        gpsTracker = new GpsTracker(getActivity());
        if (gpsTracker.canGetLocation()) {
            current_latitude = gpsTracker.getLatitude();
            current_longitude = gpsTracker.getLongitude();

        } else {

//            gpsTracker.showSettingsAlert();
        }
    }


    private void maAttach() {
        getLocation();
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                current_point = new LatLng(current_latitude, current_longitude);
                point = new LatLng(latitude, longitutde);
                Location.distanceBetween(latitude, longitutde, current_latitude, current_longitude, results);
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    measure_distance.setVisibility(View.GONE);
                } else {
                    measure_distance.setText(String.format("%.2f", results[0] / 1000) + " KM Away");
                }
                mMap.addPolyline((new PolylineOptions()).add(current_point, point).width(10).color(Color.BLUE).geodesic(true));
                mMap.addMarker(new MarkerOptions().position(current_point).title("I am here"));
                Marker destination_marker = mMap.addMarker(new MarkerOptions().position(point).title(name).icon(BitmapFromVector(getActivity(), R.drawable.ic_baseline_flag_24)));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                destination_marker.showInfoWindow();

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(point);
                        markerOptions.title(name);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                        googleMap.addMarker(markerOptions);
                    }
                });

            }
        });
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        current_point = new LatLng(current_latitude, current_longitude);
        point = new LatLng(latitude, longitutde);
        Location.distanceBetween(latitude, longitutde, current_latitude, current_longitude, results);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            measure_distance.setVisibility(View.GONE);
        } else {
            measure_distance.setText(String.format("%.2f", results[0] / 1000) + " KM Away");
        }
        mMap.addPolyline((new PolylineOptions()).add(current_point, point).width(10).color(Color.BLUE).geodesic(true));
        mMap.addMarker(new MarkerOptions().position(current_point).title("I am here"));
        Marker destination_marker = mMap.addMarker(new MarkerOptions().position(point).title(name).icon(BitmapFromVector(getActivity(), R.drawable.ic_baseline_flag_24)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
        destination_marker.showInfoWindow();

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                markerOptions.title(name);
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

    private void dataSet() {

        dbreference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment").child(place_id);
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = snapshot.child("name").getValue(String.class);
                address = snapshot.child("address").getValue(String.class);
                description = snapshot.child("description").getValue(String.class);
                hours = snapshot.child("hours").getValue(String.class);
                facilities = snapshot.child("facilities").getValue(String.class);
                website = snapshot.child("website").getValue(String.class);
                images = snapshot.child("images").getValue(Integer.class);
                for (int i = 0, j = 1; i < images; i++, j++) {
                    img_url[i] = snapshot.child("img" + j + "url").getValue(String.class);
                }
                phone = snapshot.child("phone").getValue(String.class);
                latitude = snapshot.child("latitude").getValue(Double.class);
                longitutde = snapshot.child("longitude").getValue(Double.class);
                imageSlider();
                valueSet();
                maAttach();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void valueSet() {
        holder6.setText(name);
        holder7.setText(facilities);
        holder8.setText(hours);
        holder9.setText(address);
        holder10.setText(phone);
        holder11.setText(website);
        holder12.setText(description);
        toolbar.setTitle(name);

    }


    public void viewSet() {
        holder6 = view.findViewById(R.id.holder6);
        holder7 = view.findViewById(R.id.holder7);
        holder8 = view.findViewById(R.id.holder8);
        holder9 = view.findViewById(R.id.holder9);
        holder10 = view.findViewById(R.id.holder10);
        holder11 = view.findViewById(R.id.holder11);
        holder12 = view.findViewById(R.id.holder12);

    }

    public void imageSlider() {

        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        SliderView sliderView = view.findViewById(R.id.slider_description);

        for (int i = 0; i < images; i++) {
            sliderDataArrayList.add(new SliderData(img_url[i]));
        }
        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);

        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        sliderView.setSliderAdapter(adapter);

        sliderView.setScrollTimeInSec(3);

        sliderView.setAutoCycle(true);

        sliderView.startAutoCycle();

    }


    @Override
    public void onBackPressed() {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            onBackPressed();

        }
    }

}














