package com.example.escapetour;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryLocalAdapter extends RecyclerView.Adapter<CategoryLocalAdapter.MyViewHolder> {

    private List<model> mData;
    public GpsTracker gpsTracker;
    double current_latitude, latitude;
    double current_longitude, longitude;
    Context cntxt;
    float[] results = new float[1];

    public CategoryLocalAdapter(Context context, List<model> dataList) {
        mData = dataList;
        cntxt = context;
    }

    public void setItems(List<model> data) {
        // Shuffle the data locally
        Collections.shuffle(data);
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_category_card_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        model item = mData.get(position);

        String place_id = item.getId();
        latitude = item.getLatitude();
        longitude = item.getLongitude();

//        getLocation(cntxt);
        if (ContextCompat.checkSelfPermission(cntxt, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            holder.distance.setVisibility(View.GONE);
        } else {
            getLocation(cntxt);
            holder.distance.setVisibility(View.VISIBLE);
        }
        holder.bind(item, String.format("%.2f", results[0] / 1000));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity context = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("place_id", place_id);
                context.startActivity(intent);
            }
        });
    }

    private void getLocation(Context cntxt) {
        gpsTracker = new GpsTracker(cntxt);
        if (gpsTracker.canGetLocation()) {
            current_latitude = gpsTracker.getLatitude();
            current_longitude = gpsTracker.getLongitude();
        } else {
            gpsTracker.showSettingsAlert();
        }
        Location.distanceBetween(latitude, longitude, current_latitude, current_longitude, results);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView place_name;
        private final TextView city_name;
        private final TextView distance;
        ImageView img1;


        public MyViewHolder(View itemView) {
            super(itemView);
            place_name = itemView.findViewById(R.id.entertainment_place_name);
            city_name = itemView.findViewById(R.id.city_name);
            img1 = itemView.findViewById(R.id.entertainment_place_img);
            distance = itemView.findViewById(R.id.measure_distance);


        }

        public void bind(model item, String results) {
            place_name.setText(item.getName());
            Glide.with(img1.getContext()).load(item.getImageUrl()).into(img1);
            city_name.setText(item.getCity());
            distance.setText(results + " KM Away");

        }
    }
}
