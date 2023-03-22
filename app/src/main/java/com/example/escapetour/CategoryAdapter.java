package com.example.escapetour;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {

    private ArrayList<CategoryDataModel> categoryDataArrayList;
    private Context mcontext;
    View view;

    public CategoryAdapter(ArrayList<CategoryDataModel> recyclerDataArrayList, Context mcontext) {
        this.categoryDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        CategoryDataModel recyclerData = categoryDataArrayList.get(position);
        holder.category_name.setText(recyclerData.getTitle());
        holder.category_image.setImageResource(recyclerData.getImgid());
        String category_title = recyclerData.getTitle();
        String category_id = recyclerData.getCategory();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                AppCompatActivity context = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(context, CategoriesActivity.class);
                intent.putExtra("category_title", category_title);
                intent.putExtra("category_id", category_id);
                context.startActivity(intent);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new Categories2Fragment(category_id,category_title)).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return categoryDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView category_name;
        private ImageView category_image;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            category_image = itemView.findViewById(R.id.category_image);
        }
    }
}

