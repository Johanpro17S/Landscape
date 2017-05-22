package com.example.alexeigramajo.landscapingv3.Fragments;

/**
 * Created by alexeigramajo on 5/7/2017.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexeigramajo.landscapingv3.DBClasses.ParkClass;
import com.example.alexeigramajo.landscapingv3.Fragments.decorators.EventDecorator;
import com.example.alexeigramajo.landscapingv3.Fragments.decorators.HighlightWeekendsDecorator;
import com.example.alexeigramajo.landscapingv3.Fragments.decorators.MySelectorDecorator;
import com.example.alexeigramajo.landscapingv3.Fragments.decorators.OneDayDecorator;
import com.example.alexeigramajo.landscapingv3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

public class Spraying extends Fragment {
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    MaterialCalendarView calendar;
    TextView monthView;
    TextView yearView;
    DatabaseReference databasePark;
    SpinnerAdapter arrayAdapter;
    Spinner spinner;
    ArrayList<String> parkList = new ArrayList<>();
    ArrayAdapter<String> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.spraying,container,false);
        databasePark = FirebaseDatabase.getInstance().getReference("Parks");
        FloatingActionButton myFab = (FloatingActionButton) rootview.findViewById(R.id.floatingActionButton);

        //Spinner items
        spinner = (Spinner) rootview.findViewById(R.id.spinner);
        mAdapter = new ArrayAdapter<String>(rootview.getContext(), R.layout.spinner_item, parkList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(mAdapter);

        monthView = (TextView) rootview.findViewById(R.id.monthView);
        yearView = (TextView) rootview.findViewById(R.id.yearView);
        initialize();
        //Database
        databasePark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String value = snapshot.getValue(ParkClass.class).getPark();
                    parkList.add(value.toLowerCase());
                }
                Collections.sort(parkList);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        //Everything about the calendar -- Spraying Calendar
        calendar = (MaterialCalendarView) rootview.findViewById(R.id.calendarView);
        Calendar instance = Calendar.getInstance();
        calendar.setSelectedDate(instance.getTime());
        calendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 1, 1))
                .setMaximumDate(CalendarDay.from(2021, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //Everytime papi clicks on the button
                Toast.makeText(getActivity(), "" + date, Toast.LENGTH_SHORT).show();
                oneDayDecorator.setDate(date.getDate());
                calendar.invalidateDecorators();

            }
        });
        calendar.setTopbarVisible(false);
        calendar.addDecorators(new MySelectorDecorator(getActivity()),
                new HighlightWeekendsDecorator(),oneDayDecorator);
        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                SimpleDateFormat month = new SimpleDateFormat("MMM");
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                monthView.setText(month.format(date.getDate()), TextView.BufferType.NORMAL);
                yearView.setText(year.format(date.getDate()), TextView.BufferType.NORMAL);
            }
        });

        //Floating Action listener
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                View mView = getActivity().getLayoutInflater().inflate(R.layout.add_spray,null);
                Button cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
                Button saveBtn = (Button) mView.findViewById(R.id.saveBtn);
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
                saveBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });//end of Floating ActionButton listener
        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        return rootview;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // write logic here b'z it is called when fragment is visible to user
    }
    public void initialize(){
        CalendarDay today = CalendarDay.today();
        SimpleDateFormat month = new SimpleDateFormat("MMM");
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        monthView.setText(month.format(today.getDate()), TextView.BufferType.NORMAL);
        yearView.setText(year.format(today.getDate()), TextView.BufferType.NORMAL);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Simulate an API call to show how to add decorators
     */
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -2);
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
                calendar.add(Calendar.DATE, 5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            if (getActivity().isFinishing()) {
                return;
            }
            calendar.addDecorator(new EventDecorator(ContextCompat.getColor(getContext(), R.color.colorAccent), calendarDays));
        }
    }
}