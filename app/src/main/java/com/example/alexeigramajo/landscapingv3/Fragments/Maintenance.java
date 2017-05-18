package com.example.alexeigramajo.landscapingv3.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.alexeigramajo.landscapingv3.DBClasses.ParkClass;
import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by alexeigramajo on 5/7/2017.
 */

public class Maintenance extends Fragment {
    DatabaseReference databasePark;
    SpinnerAdapter arrayAdapter;
    Spinner spinner;
    ArrayList<String> parkList = new ArrayList<>();
    ArrayAdapter<String> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.maintenance,container,false);
        databasePark = FirebaseDatabase.getInstance().getReference("Parks");

        //Spinner items
        spinner = (Spinner) rootview.findViewById(R.id.spinner);
        mAdapter = new ArrayAdapter<String>(rootview.getContext(), android.R.layout.simple_spinner_item, parkList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(mAdapter);

        databasePark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String value = snapshot.getValue(ParkClass.class).getPark();
                    parkList.add(value);
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return rootview;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // write logic here b'z it is called when fragment is visible to user
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}