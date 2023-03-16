package com.example.escapetour;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class SearchAdapter extends FirebaseRecyclerAdapter<model, SearchAdapter.myviewholder> {


    public SearchAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull SearchAdapter.myviewholder holder, int position, @NonNull model model) {

        holder.nametext.setText(model.getName());

        holder.nametext.setOnClickListener(new View.OnClickListener() {
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
    public SearchAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_row_design, parent, false);
        return new SearchAdapter.myviewholder(view);
    }


    public class myviewholder extends RecyclerView.ViewHolder {

        TextView nametext;
        RelativeLayout relativeLayout_holder;

        public myviewholder(@NonNull View itemView) {
            super(itemView);


            nametext = itemView.findViewById(R.id.search_bar_place_name);

        }
    }


}

