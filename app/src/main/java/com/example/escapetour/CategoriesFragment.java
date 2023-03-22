package com.example.escapetour;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View view;
    private RecyclerView recyclerView;
    private ArrayList<CategoryDataModel> recyclerDataArrayList;


    public CategoriesFragment() {

    }

    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_categories, container, false);

        recyclerView = view.findViewById(R.id.category_name_recview);
        recyclerDataArrayList = new ArrayList<>();

        recyclerDataArrayList.add(new CategoryDataModel("Shopping Malls", R.drawable.shopping_mall_img, "malls"));
        recyclerDataArrayList.add(new CategoryDataModel("Restaurant and Cafes", R.drawable.restaurant_img, "r&c"));
        recyclerDataArrayList.add(new CategoryDataModel("Supermarkets", R.drawable.supermarket_img, "supermarkets"));
        recyclerDataArrayList.add(new CategoryDataModel("Historical Places", R.drawable.historical_img, "hp"));
        recyclerDataArrayList.add(new CategoryDataModel("Rivers and Waterfalls", R.drawable.river_waterfall_img, "r&w"));
        recyclerDataArrayList.add(new CategoryDataModel("Wildlife", R.drawable.national_parks_img, "wild"));

        CategoryAdapter adapter = new CategoryAdapter(recyclerDataArrayList, getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }


}
