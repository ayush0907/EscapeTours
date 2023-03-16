package com.example.escapetour;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.LayoutInflater;
import android.view.ViewGroup;


public class DetailsFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View view;
    String name, address, description, facilities, website, img1url, img2url, img3url, img4url, img5url, phone, hours;
    private GoogleMap mMap;
    TextView holder6, holder7, holder8, holder9, holder10, holder11, holder12;
    ImageView holder1, holder2, holder3, holder4, holder5;
    LatLng point;
    Double latitude, longitutde;
    SupportMapFragment supportMapFragment;
    Button map_view_button, map_navigate_button;
    private DatabaseReference dbreference;

    private String mParam1;
    private String mParam2;
    String place_id;

    public DetailsFragment() {

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

        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("");

        viewSet();

        dataSet();

        maAttach();

        map_view_button = view.findViewById(R.id.map_view_button);
        map_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), LocationMapActivity.class);
//                intent.putExtra("point", point);
//                intent.putExtra("name", name);
//                startActivity(intent);
            }
        });

        map_navigate_button = view.findViewById(R.id.map_navigate_button);
        map_navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://maps.google.com/maps?daddr=" + address;
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent1);

            }
        });


        return view;
    }

    private void maAttach() {

        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                point = new LatLng(latitude, longitutde);
                mMap.addMarker(new MarkerOptions().position(point).title(name));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        // When clicked on map
                        // Initialize marker options
                        MarkerOptions markerOptions = new MarkerOptions();
                        // Set position of marker
                        markerOptions.position(point);
                        // Set title of marker
                        markerOptions.title(name);

                        // Animating to zoom the marker
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 12));
                        // Add marker on map
                        googleMap.addMarker(markerOptions);
                    }
                });
            }
        });
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
                img1url = snapshot.child("img1url").getValue(String.class);
                img2url = snapshot.child("img2url").getValue(String.class);
                img3url = snapshot.child("img3url").getValue(String.class);
                img4url = snapshot.child("img4url").getValue(String.class);
                img5url = snapshot.child("img5url").getValue(String.class);
                phone = snapshot.child("phone").getValue(String.class);
                latitude = snapshot.child("latitude").getValue(Double.class);
                longitutde = snapshot.child("longitude").getValue(Double.class);
                valueSet();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void valueSet() {
        Glide.with(holder1.getContext()).load(img1url).into(holder1);
//        Glide.with(holder2.getContext()).load(img2url).into(holder2);
//        Glide.with(holder3.getContext()).load(img3url).into(holder3);
//        Glide.with(holder4.getContext()).load(img4url).into(holder4);
//        Glide.with(holder5.getContext()).load(img5url).into(holder5);
        holder6.setText(name);
        holder7.setText(facilities);
        holder8.setText(hours);
        holder9.setText(address);
        holder10.setText(phone);
        holder11.setText(website);
        holder12.setText(description);

    }

    public void viewSet() {
        holder1 = view.findViewById(R.id.holder1);
//        holder2 = findViewById(R.id.holder2);
//        holder3 = findViewById(R.id.holder3);
//        holder4 = findViewById(R.id.holder4);
//        holder5 = findViewById(R.id.holder5);
        holder6 = view.findViewById(R.id.holder6);
        holder7 = view.findViewById(R.id.holder7);
        holder8 = view.findViewById(R.id.holder8);
        holder9 = view.findViewById(R.id.holder9);
        holder10 = view.findViewById(R.id.holder10);
        holder11 = view.findViewById(R.id.holder11);
        holder12 = view.findViewById(R.id.holder12);

    }


}














