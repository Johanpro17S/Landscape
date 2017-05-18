package com.example.alexeigramajo.landscapingv3.DBClasses;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.inflate;

/**
 * Created by alexeigramajo on 5/8/2017.
 */

public class ParkAdapter extends RecyclerView.Adapter<ParkAdapter.ViewHolder> {
    private ArrayList<String> mDataSet = new ArrayList<>();
    public static int position;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        ArrayList<String> idList = new ArrayList<>();
        DatabaseReference parkDb;
        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textViewPark);
            parkDb = FirebaseDatabase.getInstance().getReference("Parks");
            parkDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    idList.clear();
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String value = snapshot.getValue(ParkClass.class).getId();
                        idList.add(value);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                    final View mView = inflate(v.getContext(), R.layout.dialog_update,null);

                    EditText parkText = (EditText) mView.findViewById(R.id.updatePark);
                    parkText.setText(textView.getText(), TextView.BufferType.EDITABLE);
                    Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtnUpdate);
                    Button updateBtn = (Button) mView.findViewById(R.id.updateBtn);
                    alertBuilder.setView(mView);
                    final AlertDialog dialog = alertBuilder.create();
                    //Listener for cancel Button
                    cancelBtn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            dialog.dismiss();
                        }
                    });
                    //Listener for Update Button
                    updateBtn.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            EditText parkText = (EditText) mView.findViewById(R.id.updatePark);
                            if(!parkText.getText().toString().isEmpty()){
                                ParkClass newPark = new ParkClass(idList.get(position), parkText.getText().toString().trim());
                                parkDb.child(idList.get(position)).setValue(newPark);
                                Toast.makeText(view.getContext(), "Park Updated", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(view.getContext(), "Please fill in empty field", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    dialog.show();

                }
            });
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ParkAdapter(ArrayList<String> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.park_item, viewGroup, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(mDataSet.get(position));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
