package com.itproject.piczon.loudoms.passengerbusartery.DirectionFinder;

import java.util.List;

/**
 * Created by LouDoms on 11/23/2017.
 */

public interface DirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
