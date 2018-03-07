package com.example.rahulkapoor.novateguide.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.rahulkapoor.novateguide.R;
import com.example.rahulkapoor.novateguide.adapter.PlacesAdapter;
import com.example.rahulkapoor.novateguide.interfaces.DirectionCallback;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity implements DirectionCallback {

    private ImageView ivBack;
    private RecyclerView rvPlaces;
    private PlacesAdapter placesAdapter;
    private ArrayList<String> placesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        init();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //finish the activity;
                finish();
            }
        });

        addDataToList();

        setAdapter();

    }

    /**
     * adding data to array list;
     */
    private void addDataToList() {

        //add respective lat long in separate hash map in utils;
        placesList.add("Gallileo Block");
        placesList.add("Newton Block");
        placesList.add("Demorgan Block");
        placesList.add("Chitkara Woods");
        placesList.add("Exploretorium");

//        placesList.add("Turing Block");
//        placesList.add("Indoor Sports");
//        placesList.add("Teresa Girl Hostel");
//        placesList.add("Bloom's Building");
//        placesList.add("Main Ground");
//        placesList.add("CSHS Park(Gallileo)");
//        placesList.add("PNB ATM");
//        placesList.add("Chitkara School Of Planning and Architecture");
//        placesList.add("Columbus Hostel");
//        placesList.add("Vasco Da Gama Hostel");
//        placesList.add("Central Library");
//        placesList.add("Babbage Block");
//        placesList.add("Chitkara School Of Hospitality");
//        placesList.add("Square One Cafeteria");
//        placesList.add("Hostel Ground");
//        placesList.add("Pie A Hostel");
//        placesList.add("Pie B Hostel");
//        placesList.add("Pie C Hostel");
//        placesList.add("Tuck Shop");

    }

    /**
     * initialised;
     */
    private void init() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
        rvPlaces = (RecyclerView) findViewById(R.id.rv);

    }

    /**
     * setting up the places adapter;
     */
    private void setAdapter() {

        placesAdapter = new PlacesAdapter(PlacesActivity.this, placesList, this);
        rvPlaces.setLayoutManager(new LinearLayoutManager(PlacesActivity.this, LinearLayoutManager.VERTICAL, false));
        rvPlaces.setAdapter(placesAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * get position of item clicked and return result;
     *
     * @param position pos to fetch lat,lng further;
     */
    @Override
    public void getPosition(final int position) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("pos", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }
}
