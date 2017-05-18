package com.example.alexeigramajo.landscapingv3.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.alexeigramajo.landscapingv3.DBClasses.ParkAdapter;
import com.example.alexeigramajo.landscapingv3.DBClasses.ParkClass;
import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;

public class Park extends Fragment {
    DatabaseReference databasePark;
    ArrayList<String> parkList = new ArrayList<>();
    protected RecyclerView mRecyclerView;
    protected ParkAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_park, container, false);
        FloatingActionButton myFab = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        //RecycleView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ParkAdapter(parkList);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        //database items
        databasePark = FirebaseDatabase.getInstance().getReference("Parks");
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),new LinearLayoutManager(getActivity()).getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        //FireBase - database event listener
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
        //Recycle Action Listener

        //Floating Action listener
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_add,null);
                final EditText parkText = (EditText) mView.findViewById(R.id.addPark);
                Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
                Button addBtn = (Button) mView.findViewById(R.id.addBtn);
                alertBuilder.setView(mView);
                final AlertDialog dialog = alertBuilder.create();
                //Listener for cancel Button
                cancelBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                    }
                });
                //Listener for Add Button
                addBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        if(!parkText.getText().toString().isEmpty() && !Arrays.asList(parkList).contains(parkText.getText().toString().trim())){
                            addPark(parkText.getText().toString().trim());
                            Toast.makeText(getActivity(), "Park Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Please fill in empty field", Toast.LENGTH_LONG).show();
                        }
                    }
                });

               dialog.show();
            }
        });//end of Floating ActionButton listener
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void addPark(String parkName){
        String id = databasePark.push().getKey();
        ParkClass park = new ParkClass(id, parkName);
        databasePark.child(id).setValue(park);
    }
}
