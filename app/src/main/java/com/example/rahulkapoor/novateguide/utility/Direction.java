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

        directionList.add("30.515783,76.659078");//gallileo block
        directionList.add("30.516400,76.659234"); //newton block
        directionList.add("30.516211,76.660359");//de-morgan block
        directionList.add("30.516167,76.659135");//woods
        directionList.add("30.515852,76.657366");//exploretorium
        directionList.add("30.515681,76.660699");//indoor;
        directionList.add("30.518220,76.659376");////ATM;
        directionList.add("30.517212,76.660419");//chitkara school of planning and arch.;
        directionList.add("30.514916,76.659604");//tuck shop
        directionList.add("30.515235,76.659885");//square one cafeteria
        directionList.add("30.517423,76.660147");//babbage block
        directionList.add("30.516375,76.659908");//edison block;
        directionList.add("30.517350,76.660698");//architecture block canteen;
        directionList.add("30.515288,76.659900");//central library;
        directionList.add("30.516437,76.660585");//turing block;


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
