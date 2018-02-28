package com.example.rahulkapoor.novateguide.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.rahulkapoor.novateguide.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivAddMarker, ivDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //open close the drawer;
            }
        });

        ivAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //take user to places list screen and get the result from it;

            }
        });
    }

    /**
     * init made;
     */
    private void init() {

        ivAddMarker = (ImageView) findViewById(R.id.iv_add_marker);
        ivDrawer = (ImageView) findViewById(R.id.iv_drawer);

    }
}
