package com.example.rahulkapoor.novateguide.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rahulkapoor on 28/02/18.
 */

public class SharedPref {

    private static Context context;
    private static SharedPref sharedPref = new SharedPref();

    public static SharedPref getInstance(final Context mContext) {
        context = mContext;
        return sharedPref;
    }

    /**
     * extract user email ;
     *
     * @param context context;
     * @return return string;
     */
    public String read_email(final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        return sharedpref.getString("email", "");
    }

    /**
     * save email
     *
     * @param email   email
     * @param context context;
     */
    public void save_email(final String email, final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * extract user profile picture ;
     *
     * @param context context;
     * @return return string;
     */
    public String read_picture(final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        return sharedpref.getString("picture", "");
    }

    /**
     * save user profile picture
     *
     * @param picture   picture
     * @param context context;
     */
    public void save_picture(final String picture, final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("picture", picture);
        editor.apply();
    }

    /**
     * extract user access token ;
     *
     * @param context context;
     * @return return string;
     */
    public String read_token(final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        return sharedpref.getString("token", "");
    }

    /**
     * save user access token
     *
     * @param token   token
     * @param context context;
     */
    public void save_token(final String token, final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("token", token);
        editor.apply();
    }

    /**
     * save username and extract in drawer;
     *
     * @param username save username from gmail;
     */
    public void saveUsername(final String username) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();
        editor.putString("username", username);
        editor.apply();
    }

    /**
     * get username from gmail;
     *
     * @param context context;
     * @return return string containing usenrame;
     */
    public String read_username(final Context context) {
        SharedPreferences sharedpref = context.getSharedPreferences("novate", Context.MODE_PRIVATE);
        return sharedpref.getString("username", "");
    }

}
