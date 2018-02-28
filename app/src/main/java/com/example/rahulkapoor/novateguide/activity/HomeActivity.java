package com.example.rahulkapoor.novateguide.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rahulkapoor.novateguide.R;

public class HomeActivity extends AppCompatActivity {

    private ImageView ivAddMarker, ivDrawer, ivUserImage;
    private DrawerLayout drawerLayout;
    private TextView tvUsername, tvEmail;
    private static final int REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //open close the drawer;
                if (drawerLayout.isDrawerOpen(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.END);
                } else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        ivAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //take user to places list screen and get the result from it;

                Intent i = new Intent(HomeActivity.this, PlacesActivity.class);
                startActivityForResult(i, REQ_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE) {
                //perform the necessary option on callback;

            }
        }
    }

    /**
     * init made;
     */
    private void init() {

        ivAddMarker = (ImageView) findViewById(R.id.iv_add_marker);
        ivDrawer = (ImageView) findViewById(R.id.iv_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        ivUserImage = (ImageView) findViewById(R.id.iv_user);

    }
}
