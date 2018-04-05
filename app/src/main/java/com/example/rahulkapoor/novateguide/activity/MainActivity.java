package com.example.rahulkapoor.novateguide.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rahulkapoor.novateguide.R;
import com.example.rahulkapoor.novateguide.utility.FacebookManager;
import com.example.rahulkapoor.novateguide.utility.ResponseHandler;
import com.example.rahulkapoor.novateguide.utility.SharedPref;
import com.example.rahulkapoor.novateguide.utility.SocialData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private Button btnGmail, btnFacebook;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private FacebookManager facebookManager;
    private static final Collection<String> PERMISSIONS_LIST = Arrays.asList("public_profile", "email");
    private String facebookID = "";
    private String googleID = "";
    private String facebookEmail;
    private String userFirstName = "";
    private String userLastName = "";
    private String facebookToken = "";
    private String userImage = "";
    private SocialData socialData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationSettings();

        //previously logged in or not;
        if (!SharedPref.getInstance(getApplicationContext()).read_token(getApplicationContext()).isEmpty()) {
            //take user to home activity;
            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
        }

        init();

        //facebook sdk init;
        callbackManager = CallbackManager.Factory.create();
        facebookManager = new FacebookManager(MainActivity.this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //login via gmail;
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //login via facebook;
                facebookSignin();
            }
        });

    }

    /**
     * check for location and gps services;
     */
    private void checkLocationSettings() {

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

            callLocationAlertDialog();
        }

    }

    private void callLocationAlertDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Enable Location Service");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(myIntent);
            }
        });

        dialog.show();

    }

    /**
     * facebook sign in;
     */
    private void facebookSignin() {

        facebookManager.getFbUserDetails(callbackManager, new ResponseHandler() {
            @Override
            public void onSuccess(final SocialData fbUserDetails) {
                Log.i("onSuccess", "Successful hit");
                Log.i("fbid", fbUserDetails.getID());
                Log.i("accesstoken", String.valueOf(AccessToken.getCurrentAccessToken().getToken()));
                facebookID = fbUserDetails.getID();
                facebookToken = AccessToken.getCurrentAccessToken().getToken();
                facebookEmail = fbUserDetails.getEmail();
                userFirstName = fbUserDetails.getFirstName();
                userLastName = fbUserDetails.getLastName();
                Log.i("facebook_data", facebookID + ":" + facebookToken + ":" + facebookEmail + ":" + userFirstName + " " + userLastName);

                if ("".equals(fbUserDetails.getPicture())) {
                    fbUserDetails.getPicture();
                }

                //save access token;
                saveToken();


                socialData = fbUserDetails;

                //save data to shared pref;
                SharedPref.getInstance(getApplicationContext()).save_email(facebookEmail, MainActivity.this);
                String resName = "";
                if (!userFirstName.isEmpty()) {
                    resName = resName + userFirstName + " ";
                }
                if (!userLastName.isEmpty()) {
                    resName = resName + userLastName;
                }
                SharedPref.getInstance(getApplicationContext()).saveUsername(resName);

                //take user to home activity;
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(i);

            }

            @Override
            public void onCancel(final String msg) {
                callSocialAlertDialog(msg);

            }

            @Override
            public void onError(final FacebookException e) {
                callSocialAlertDialog(e.getMessage());
            }
        });


    }

    /**
     * save access token;
     */
    private void saveToken() {
        SharedPref.getInstance(getApplicationContext()).save_token(facebookToken, getApplicationContext());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //for facebook;
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void callSocialAlertDialog(final String errmsg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this)
                .setMessage("Failed Login: " + errmsg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        //let user click again
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    /**
     * init made;
     */
    private void init() {

        btnGmail = (Button) findViewById(R.id.btn_gmail);
        btnFacebook = (Button) findViewById(R.id.btn_facebook);

    }
}
