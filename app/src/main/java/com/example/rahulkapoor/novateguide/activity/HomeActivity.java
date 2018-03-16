package com.example.rahulkapoor.novateguide.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.rahulkapoor.novateguide.R;
import com.example.rahulkapoor.novateguide.utility.Direction;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, RoutingListener {

    private ImageView ivAddMarker, ivDrawer, ivUserImage;
    private Button btnLogout;
    private DrawerLayout drawerLayout;
    private TextView tvUsername, tvEmail;
    private static final int REQ_CODE = 1;
    private static final int REQ_LOCATION_CODE = 99;
    private static final int MAP_ZOOM = 14;
    private static final int REQ_CODE_DIRECTION = 2;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private Location lastLocation;
    private Polyline polyline;
    private Marker currentLocationMarker;
    private Marker destinationMarker;
    Circle circle;
    private ArrayList<String> directionList = new ArrayList<>();

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorAccent};


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

                currentLocationSetup();

                Intent i = new Intent(HomeActivity.this, PlacesActivity.class);
                startActivityForResult(i, REQ_CODE_DIRECTION);

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //clear access token and logout;
                SharedPref.getInstance(getApplicationContext()).save_token(null, getApplicationContext());
                finish();
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

        //set up the direction arraylist;
        Direction.getInstance(getApplicationContext()).setDirectionList();

        directionList = Direction.getInstance(getApplicationContext()).getDirectionList();

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_CODE_DIRECTION) {
                //perform the necessary option on callback;
                int pos = data.getIntExtra("pos", 0);
                //set up the marker and polyline;
                addDestMarker(pos);
            }
        }
    }

    /**
     * position to fetch lat,lng and add marker;
     * draw lines b/w both the markers to guide the user;
     *
     * @param pos pos;
     */
    private void addDestMarker(final int pos) {

        String mLatLng = directionList.get(pos);
        String[] splitData = mLatLng.split(",");
        Double latitude = Double.parseDouble(splitData[0]);
        Double longitude = Double.parseDouble(splitData[1]);

        //if dest marker is present so is route;
        //also clear the route list;
        if (destinationMarker != null) {
            destinationMarker.remove();
            destinationMarker = null;

            for (Polyline line : polylines) {
                line.remove();
            }
            polylines.clear();

//            polyline.remove();
//            polyline = null;
        }
        //new current location;
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Destination");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        //add marker and move camera;
        destinationMarker = mGoogleMap.addMarker(markerOptions);

        drawPath(latLng);


    }

    /**
     * draw a route between both the markers;
     *
     * @param latLng latlng of destination;
     */
    private void drawPath(final LatLng latLng) {

//draw a route by giving start and end latlng;
        try {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .waypoints(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), latLng)
                    .build();
            routing.execute();
        } catch (Exception e) {
            Log.i("route", e.getMessage() + "");
        }

//        PolylineOptions polylineOptions = new PolylineOptions()
//                .add(currentLocationMarker.getPosition())
//                .add(destinationMarker.getPosition())
//                .color(Color.RED)
//                .width(2);
//
//        polyline = mGoogleMap.addPolyline(polylineOptions);


    }

    /**
     * init made;
     */
    private void init() {

        //allocating memory to route list;
        polylines = new ArrayList<>();

        ivAddMarker = (ImageView) findViewById(R.id.iv_add_marker);
        ivDrawer = (ImageView) findViewById(R.id.iv_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvUsername = (TextView) findViewById(R.id.tv_username);
        ivUserImage = (ImageView) findViewById(R.id.iv_user);
        btnLogout = (Button) findViewById(R.id.btn_logout);

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

        //if we already have a marker then we also have a circle to it;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
            currentLocationMarker = null;
            circle.remove();
            circle = null;
        }

        //new current location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        //add marker and move camera;
        currentLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM));
        //mGoogleMap.animateCamera(CameraUpdateFactory.zoomBy(MAP_ZOOM));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        //adding circle to the current location of user;

        circle = getCircle(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    /**
     * circle around our current location ;
     *
     * @param latLng latlng;
     * @return return circle object;
     */
    private Circle getCircle(final LatLng latLng) {

        CircleOptions circleOptions = new CircleOptions()
                .radius(200)
                .center(latLng)
                .fillColor(0xBBDEFB)
                .strokeColor(Color.CYAN)
                .strokeWidth(2);

        return mGoogleMap.addCircle(circleOptions);
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

    //------------------------ draw a route between our marker -----------------------------//

    @Override
    public void onRoutingFailure(final RouteException e) {
        Log.i("route", "onRoutingFailure");
//        if (e != null) {
//            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onRoutingStart() {
        Log.i("route", "onRoutingStart");
    }

    @Override
    public void onRoutingSuccess(final ArrayList<Route> route, final int shortestRouteIndex) {

        Log.i("route", "onRoutingSuccess");

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mGoogleMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {
        Log.i("route", "onRoutingCancelled");
    }
}
