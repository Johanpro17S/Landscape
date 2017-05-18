package com.example.alexeigramajo.landscapingv3.DBClasses;

import android.app.Activity;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexeigramajo on 5/15/2017.
 */

public class EmailList extends ArrayAdapter<String> {
    private Activity context;
    private List<String> emailList, idList;
    DatabaseReference databaseWEmails; //for only weekly emails

    public EmailList(Activity context, List<String> emailList){
        super(context, R.layout.email_item, emailList);
        this.context = context;
        this.emailList = emailList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        databaseWEmails = FirebaseDatabase.getInstance().getReference("Weekly Emails");
        idList = new ArrayList<>();
        View listViewItem = inflater.inflate(R.layout.email_item, null, true);
        TextView emailText = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        ImageButton imageButton = (ImageButton) listViewItem.findViewById(R.id.imageButton);
        databaseWEmails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idList.clear();
                for(DataSnapshot emailSnapshot: dataSnapshot.getChildren()){
                    String id = emailSnapshot.getKey();
                    idList.add(id);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                View mView = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);
                TextView finalemailText = (TextView) mView.findViewById(R.id.warning);
                finalemailText.setText("Would you like to delete " + getItem(position) + "?", TextView.BufferType.NORMAL);

                Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
                Button addBtn = (Button) mView.findViewById(R.id.deleteBtn);
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
                        databaseWEmails.child(idList.get(position)).removeValue();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Email Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

        String email = emailList.get(position);
        emailText.setText(email);

        return listViewItem;
    }
}
