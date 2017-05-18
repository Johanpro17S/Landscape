package com.example.alexeigramajo.landscapingv3.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alexeigramajo.landscapingv3.DBClasses.EmailList;
import com.example.alexeigramajo.landscapingv3.DBClasses.EmailListMonth;
import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alexeigramajo on 5/7/2017.
 */

public class    Notifications extends Fragment {
    Spinner numSpinner;
    ArrayAdapter<String> myAdapter;

    DatabaseReference databaseWEmails; //for only weekly emails
    DatabaseReference databaseMEmails; //for only monthly emails
    DatabaseReference weeklyNotifications; //for only weekly notifications
    Button addWEmails, addMEmails;
    ListView listWEmails, listMEmails;
    List<String> weekEmailList, monthEmailList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.notifications,container,false);
        //Spinner
        numSpinner = (Spinner) rootview.findViewById(R.id.spinnerNum);
        myAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.numOptions));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numSpinner.setAdapter(myAdapter);
        weeklyNotifications = FirebaseDatabase.getInstance().getReference("Weekly Notifications");
        //database - weekly emails
        addWEmails = (Button) rootview.findViewById(R.id.addBtnWeek);
        databaseWEmails = FirebaseDatabase.getInstance().getReference("Weekly Emails");
        listWEmails = (ListView) rootview.findViewById(R.id.weeklyEmails);
        weekEmailList = new ArrayList<>();

        addWEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_email_add,null);
                final EditText emailText = (EditText) mView.findViewById(R.id.addEmail);
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
                        if(!emailText.getText().toString().isEmpty() && !Arrays.asList(weekEmailList).contains(emailText.getText().toString().trim())){
                            addWEmail(emailText.getText().toString().trim());
                            Toast.makeText(getActivity(), "Email Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Please fill in empty field", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        //database - monthly emails
        addMEmails = (Button) rootview.findViewById(R.id.addBtn);
        databaseMEmails = FirebaseDatabase.getInstance().getReference("Monthly Emails");
        listMEmails = (ListView) rootview.findViewById(R.id.monthlyEmails);
        monthEmailList = new ArrayList<>();
        addMEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.dialog_email_add,null);
                final EditText emailText = (EditText) mView.findViewById(R.id.addEmail);
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
                        if(!emailText.getText().toString().isEmpty() && !Arrays.asList(monthEmailList).contains(emailText.getText().toString().trim())){
                            addMEmail(emailText.getText().toString().trim());
                            Toast.makeText(getActivity(), "Email Added", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Please fill in empty field", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.show();
            }
        });
        return rootview;
    }

    @Override
    public void onStart() {
        super.onStart();
        weeklyNotifications.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numSpinner.setSelection(myAdapter.getPosition(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseWEmails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                weekEmailList.clear();
                for(DataSnapshot emailSnapshot: dataSnapshot.getChildren()){
                    String email = emailSnapshot.getValue(String.class);

                    weekEmailList.add(email);
                }
                EmailList adapter = new EmailList(getActivity(), weekEmailList);
                listWEmails.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        databaseMEmails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                monthEmailList.clear();
                for(DataSnapshot emailSnapshot: dataSnapshot.getChildren()){
                    String email = emailSnapshot.getValue(String.class);

                    monthEmailList.add(email);
                }
                EmailListMonth adapter = new EmailListMonth(getActivity(), monthEmailList);
                listMEmails.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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

    private void addWEmail(String emailAddress){
        String id = databaseWEmails.push().getKey();
        databaseWEmails.child(id).setValue(emailAddress);
    }
    private void addMEmail(String emailAddress){
        String id = databaseMEmails.push().getKey();
        databaseMEmails.child(id).setValue(emailAddress);
    }
}