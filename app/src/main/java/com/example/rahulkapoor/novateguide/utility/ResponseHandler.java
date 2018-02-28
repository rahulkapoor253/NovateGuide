package com.example.rahulkapoor.novateguide.utility;

import com.facebook.FacebookException;

/**
 * Created by rahulkapoor on 28/02/18.
 */

public interface ResponseHandler {

    /**
     * On success.
     *
     * @param fbData the fb user data
     */
    void onSuccess(SocialData fbData);

    /**
     * On cancel.
     *
     * @param msg msg
     */
    void onCancel(String msg);

    /**
     * On error.
     *
     * @param e exception;
     */
    void onError(FacebookException e);
}
