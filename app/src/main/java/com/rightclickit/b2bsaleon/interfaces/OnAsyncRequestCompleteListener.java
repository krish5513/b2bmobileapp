package com.rightclickit.b2bsaleon.interfaces;

import com.rightclickit.b2bsaleon.constants.Constants;

/**
 * Created by venkat on 01/07/16.
 *
 * Interface - all activities which are using this AsyncRequest class should implement this interface & override asyncResponse method
 *
 */
public interface OnAsyncRequestCompleteListener {
    //void asyncResponse(String response);

    void asyncResponse(String response, Constants.RequestCode requestCode);
}
