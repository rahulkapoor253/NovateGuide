package com.example.rahulkapoor.novateguide.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahulkapoor.novateguide.R;
import com.example.rahulkapoor.novateguide.utility.SharedPref;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ImageView ivAddMarker, ivDrawer, ivUserImage;
    private DrawerLayout drawerLayout;
    private TextView tvUsername, tvEmail;
    private static final int REQ_CODE = 1;
    private static final int REQ_LOCATION_CODE = 99;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private Location lastLocation;
    private Marker currentLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkLocationPermission();
        }

        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);

        currentLocationSetup();

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

    /**
     * google api client builder;
     */
    private synchronized void googleApiClientBuilder() {
        //google api client builder;
        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .build();

        mGoogleApiClient.connect();
    }

    /**
     * to set the objects required to fetch current location of user;
     */
    private void currentLocationSetup() {

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

        //set email and username;
        tvEmail.setText(SharedPref.getInstance(getApplicationContext()).read_email(getApplicationContext()));
        tvUsername.setText(SharedPref.getInstance(getApplicationContext()).read_username(getApplicationContext()));

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.mGoogleMap = googleMap;

        //to check working of maps;
//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            googleApiClientBuilder();
            mGoogleMap.setMyLocationEnabled(true);
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }


    }


    @Override
    public void onConnected(@Nullable final Bundle bundle) {
        Log.i("maps", "on connected");
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }


    }

    @Override
    public void onConnectionSuspended(final int i) {
        Log.i("maps", "on connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {
        Log.i("maps", "on Connection Failed");
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.i("maps", "on Location Changed");
        lastLocation = location;

        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        //new current location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        //add marker and move camera;
        currentLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomBy(50));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public boolean checkLocationPermission() {

        Log.i("maps", "check location pemission");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION_CODE);
            }
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case REQ_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission for maps granted;
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            googleApiClientBuilder();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

        }

    }
}
