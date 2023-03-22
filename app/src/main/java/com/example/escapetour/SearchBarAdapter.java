package com.example.escapetour;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchBarAdapter extends RecyclerView.Adapter<SearchBarAdapter.ViewHolder> {
    private List<MyModel> itemList;
    private List<MyModel> filteredList;
    private Context context;
    private FirebaseRecyclerOptions<MyModel> options;

    public SearchBarAdapter(List<MyModel> itemList, Context context) {
        this.itemList = itemList;
        this.filteredList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_single_row_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyModel item = filteredList.get(position);
        String place_id = item.getId();
        holder.titleTextView.setText(item.getName());

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

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String text) {
        filteredList = new ArrayList<>();
        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(itemList);
        } else {
            for (MyModel item : itemList) {
                if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            if(filteredList.isEmpty()){
                Toast.makeText(context.getApplicationContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
            }
        }
        notifyDataSetChanged();

    }

    public void updateOptions(FirebaseRecyclerOptions<MyModel> options) {
        this.options = options;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.search_bar_place_name);

        }
    }
}
