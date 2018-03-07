package com.example.rahulkapoor.novateguide.utility;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by rahulkapoor on 07/03/18.
 */

public class Direction {

    private static Context context;
    private static Direction direction = new Direction();
    private ArrayList<String> directionList = new ArrayList<>();

    public static Direction getInstance(final Context mContext) {
        context = mContext;
        return direction;
    }

    /**
     * set direction into array list with delim as lat,lng;
     */
    public void setDirectionList() {

        directionList.add("30.515783,76.659078");//gallileo
        directionList.add("30.516400,76.659234"); //newton
        directionList.add("30.516211,76.660359");//de-morgan
        directionList.add("30.516167,76.659135");//woods
        directionList.add("30.515852,76.657366");//exploretorium

    }

    /**
     * get direction list for lat,lng;
     *
     * @return directions list;
     */
    public ArrayList<String> getDirectionList() {
        return directionList;
    }

}
