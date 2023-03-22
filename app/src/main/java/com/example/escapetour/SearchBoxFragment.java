package com.example.escapetour;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchBoxFragment extends Fragment implements Backpressedlisterner {

    private RecyclerView recyclerView;
    private SearchBarAdapter adapter;
    private List<MyModel> dataList;
    public static SearchBoxFragment backpressedlistener;
    private DatabaseReference databaseRef;

    private MaterialSearchView searchView;

    public SearchBoxFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_box, container, false);

        databaseRef = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment");
        databaseRef.keepSynced(true);

        searchView = rootView.findViewById(R.id.search_box_view);

        recyclerView = rootView.findViewById(R.id.search_recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setVisibility(View.INVISIBLE);
        dataList = new ArrayList<>();
        adapter = new SearchBarAdapter(dataList, getContext());
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                if (query.length() > 0) {
//                    filter(query);
//
//                    recyclerView.setVisibility(View.VISIBLE);
//                } else {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
//                if (newText.length() == 0) {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                }
//                    recyclerView.setVisibility(View.INVISIBLE);


                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                searchView.clearFocus();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // or getChildFragmentManager() if it's a child fragment
                        fragmentManager.popBackStack();
                        // show popup or navigate to new fragment here
                    }
                }, 200);

            }
        });

//        loadData();

        return rootView;
    }


    private void loadData() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyModel model = snapshot.getValue(MyModel.class);
                    dataList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void filter(String text) {
//        Query query;
//        if (TextUtils.isEmpty(text)) {
//            query = databaseRef;
//        } else {
//            query = databaseRef.orderByChild("name").startAt(text.toUpperCase()).endAt(text.toLowerCase());
//        }
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    MyModel model = snapshot.getValue(MyModel.class);
//                    dataList.add(model);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Failed to load filtered data!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
        searchView.showSearch();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        backpressedlistener = this;
    }

    @Override
    public void onStart() {
        super.onStart();
        searchView.showSearch();

        Query query = databaseRef.orderByChild("name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyModel model = snapshot.getValue(MyModel.class);
                    dataList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load filtered data!", Toast.LENGTH_SHORT).show();
            }
        });
//        databaseRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<MyModel> products = new ArrayList<>();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    MyModel model = dataSnapshot.getValue(MyModel.class);
//                    products.add(model);
//                }
//                adapter.notifyDataSetChanged();
////                adapter.setProducts(products);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });

    }

    @Override
    public void onBackPressed() {
//        searchView.closeSearch();
//        searchView.clearFocus();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // or getChildFragmentManager() if it's a child fragment
        fragmentManager.popBackStack();

//        FragmentManager fragmentManager = getFragmentManager();
//        if (fragmentManager.getBackStackEntryCount() > 0) {
//            fragmentManager.popBackStack();
//        } else {
//            onBackPressed();
//        }

//        AppCompatActivity activity = (AppCompatActivity) getContext();
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
    }

}


//public class SearchBoxFragment extends Fragment implements Backpressedlisterner {
//
//    private String mParam1;
//    private String mParam2;
//    View view;
//    MaterialSearchView searchView;
//    public static SearchBoxFragment backpressedlistener;
//    DatabaseReference mDatabase;
//    MaterialToolbar toolbar;
//    RecyclerView recyclerView;
//    SearchBarAdapter adapter;
//    String editText;
//
//    public SearchBoxFragment() {
//
//    }
//
//
//    public static SearchBoxFragment newInstance(String param1, String param2) {
//        SearchBoxFragment fragment = new SearchBoxFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        adapter.stopListening();
//        searchView.showSearch();
////        adapter.startListening();
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public void onPause() {
//        backpressedlistener = null;
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
////        adapter.stopListening();
////        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        searchView.showSearch();
////        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//        backpressedlistener = this;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_search_box, container, false);
//        toolbar = view.findViewById(R.id.normal_toolbar);
//        recyclerView = view.findViewById(R.id.search_recview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        searchView = view.findViewById(R.id.search_box_view);
//        searchView.setEllipsize(true);
//        searchView.setVoiceSearch(true);
//
//        FirebaseRecyclerOptions<MyModel> options =
//                new FirebaseRecyclerOptions.Builder<MyModel>()
//                        .setQuery(FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment"), MyModel.class)
//                        .build();
//
//
//        adapter = new SearchBarAdapter(options, getContext());
//        recyclerView.setAdapter(adapter);
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<MyModel> dataList = new ArrayList<>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String name = snapshot.child("name").getValue().toString();
//                    MyModel item=new MyModel(name);
//                    dataList.add(item);
//                }
//                adapter.setDataList(dataList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                // Call onBackPressed when the search view is closed
//                onBackPressed();
//            }
//        });
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
////                FirebaseRecyclerOptions<> options =
////                        new FirebaseRecyclerOptions.Builder<>()
////                                .setQuery(FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment").orderByChild("name").startAt(editText.toUpperCase()).endAt(editText.toUpperCase() + "\uf8ff"), .class)
////                                .build();
////                recyclerView.setVisibility(View.VISIBLE);
////                adapter = new SearchBarAdapter(options,getContext());
////                adapter.startListening();
////                recyclerView.setAdapter(adapter);
////                searchView.clearFocus();
//
//                return true;
//            }
////
//            @Override
//            public boolean onQueryTextChange(String newText) {
////                editText = newText;
//
//
////
////                    FirebaseRecyclerOptions<> options =
////                            new FirebaseRecyclerOptions.Builder<>()
////                                    .setQuery(FirebaseDatabase.getInstance("https://escape-tours-c343a-default-rtdb.firebaseio.com/").getReference().child("entertainment").orderByChild("name").startAt(newText.toUpperCase()).endAt(newText.toLowerCase() + "\uf8ff"), .class)
////                                    .build();
////
////                    adapter = new SearchAdapter(options);
//
////                    recyclerView.setAdapter(adapter);
////                } else {
////                    recyclerView.setVisibility(View.INVISIBLE);
////                }
//                    adapter.getFilter().filter(newText);
//                    recyclerView.setAdapter(adapter);
//
//
//                return true;
//            }
//        });
//
//
//        return view;
//    }
//
//
//    @Override
//    public void onBackPressed() {
//
//        AppCompatActivity activity = (AppCompatActivity) getContext();
//        activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContent, new HomeFragment()).commit();
//    }
//
//
//}