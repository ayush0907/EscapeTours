package com.example.escapetour;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

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
        holder.titleTextView.setText(item.getName());

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
                if (item.getName().contains(text)) {
                    filteredList.add(item);
                }
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
