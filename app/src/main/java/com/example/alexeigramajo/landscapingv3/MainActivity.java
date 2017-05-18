package com.example.alexeigramajo.landscapingv3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.alexeigramajo.landscapingv3.Fragments.Maintenance;
import com.example.alexeigramajo.landscapingv3.Fragments.Notifications;
import com.example.alexeigramajo.landscapingv3.Fragments.Park;
import com.example.alexeigramajo.landscapingv3.Fragments.Settings;
import com.example.alexeigramajo.landscapingv3.Fragments.Spraying;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        changeFragment(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                changeFragment(0);
                                break;
                            case R.id.navigation_spraying:
                                changeFragment(1);
                                break;
                            case R.id.navigation_park:
                                changeFragment(2);
                                break;
                            case R.id.navigation_notifications:
                                changeFragment(3);
                                break;
                            case R.id.navigation_settings:
                                changeFragment(4);
                                break;
                        }
                        return true;
                    }
                });
   }

    /**
     * To load fragments for sample
     * @param position menu index
     */
    private void changeFragment(int position) {
        Fragment newFragment = null;
        switch(position){
            case 0:
                newFragment = new Maintenance();
                break;
            case 1:
                newFragment = new Spraying();
                break;
            case 2:
                newFragment = new Park();
                break;
            case 3:
                newFragment = new Notifications();
                break;
            case 4:
                newFragment = new Settings();
                break;
            default:
                newFragment = new Spraying();
        }

        getFragmentManager().beginTransaction().replace(
                R.id.content, newFragment).commit();
    }
}
