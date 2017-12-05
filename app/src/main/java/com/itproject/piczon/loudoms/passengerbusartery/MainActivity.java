package com.itproject.piczon.loudoms.passengerbusartery;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.itproject.piczon.loudoms.passengerbusartery.NavigationActivities.PassengerProfileFragment;
import com.itproject.piczon.loudoms.passengerbusartery.NavigationActivities.TravelHistoryFragment;

/**
 * Created by LouDoms on 11/21/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;

    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
            navigationView.setNavigationItemSelectedListener(this);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,
                new MapsActivity()).commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrameLayout,
//                new MapsActivity()).addToBackStack(null).commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int item = menuItem.getItemId();

        if (item == R.id.passenger_profile) {
            PassengerProfileFragment passengerProfileFragment = new PassengerProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout,passengerProfileFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item == R.id.travelHistory) {
            TravelHistoryFragment travelHistoryFragment = new TravelHistoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout,travelHistoryFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item == R.id.logout) {
//            signOut();
            finish();
        }


        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
        return true;
    }
}
