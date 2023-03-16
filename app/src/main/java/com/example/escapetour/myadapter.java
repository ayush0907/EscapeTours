package com.example.escapetour;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {

    FragmentManager fragmentManager;
    Fragment fragment = null;
    ScrollView main_content;


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull model model) {

        holder.nametext.setText(model.getName());
        Glide.with(holder.img1.getContext()).load(model.getImageUrl()).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity context = (AppCompatActivity) view.getContext();
                String place_id = getRef(holder.getAdapterPosition()).getKey();
//                context.getSupportFragmentManager().beginTransaction().replace(R.id.flContent,new DetailsFragment(place_id)).addToBackStack(null).commit();
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("place_id", place_id);
                context.startActivity(intent);

            }
        });

    }

    @NonNull
    @Override
    public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView nametext;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.entertainment_place_img);
            nametext = itemView.findViewById(R.id.entertainment_place_name);

        }
    }


}
