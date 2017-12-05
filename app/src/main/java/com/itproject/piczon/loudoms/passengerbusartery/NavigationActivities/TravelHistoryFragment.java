package com.itproject.piczon.loudoms.passengerbusartery.NavigationActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itproject.piczon.loudoms.passengerbusartery.R;

/**
 * Created by LouDoms on 11/23/2017.
 */

public class TravelHistoryFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_travel_history, container, false);
        return v;
    }
}
