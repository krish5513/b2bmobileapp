package com.rightclickit.b2bsaleon.util;

import android.content.Context;
import android.os.AsyncTask;

import com.rightclickit.b2bsaleon.R;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.customviews.CustomAlertDialog;
import com.rightclickit.b2bsaleon.customviews.CustomProgressDialog;
import com.rightclickit.b2bsaleon.interfaces.OnAsyncRequestCompleteListener;

import org.json.JSONObject;

/**
 * Created by venkat on 21/06/16.
 */
public class AsyncRequest extends AsyncTask<Void, Void, String> {

    OnAsyncRequestCompleteListener listener;
    Context context;
    String requestURL = null;
    JSONObject parameters = null;
    MethodType methodType = MethodType.GET;
    Constants.RequestCode requestCode = null;

    public enum MethodType {
        GET,
        POST
    }

    // Constructors
    public AsyncRequest(Context context, OnAsyncRequestCompleteListener listener) {
        this.listener = listener;
        this.context = context;
    }

    public AsyncRequest(Context context, OnAsyncRequestCompleteListener listener, String url, MethodType method) {
        this.listener = listener;
        this.context = context;
        requestURL = url;
        methodType = method;
    }

    public AsyncRequest(Context context, OnAsyncRequestCompleteListener listener, String url, MethodType method, JSONObject params) {
        this.listener = listener;
        this.context = context;
        requestURL = url;
        methodType = method;
        parameters = params;
    }

    public AsyncRequest(Context context, OnAsyncRequestCompleteListener listener, String url, MethodType method, Constants.RequestCode reqCode) {
        this.listener = listener;
        this.context = context;
        this.requestURL = url;
        this.methodType = method;
        this.requestCode = reqCode;
    }

    public AsyncRequest(Context context, OnAsyncRequestCompleteListener listener, String url, MethodType method, JSONObject params, Constants.RequestCode reqCode) {
        this.listener = listener;
        this.context = context;
        this.requestURL = url;
        this.methodType = method;
        this.parameters = params;
        this.requestCode = reqCode;
    }

    public void setListener(OnAsyncRequestCompleteListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        /*if (new NetworkConnectionDetector(context).isNetworkConnected()) {
            //CustomProgressDialog.showProgressDialog(context, context.getResources().getString(R.string.loading_message));
        }*/
    }

    @Override
    protected String doInBackground(Void... params) {
        String response = null;
        try {
            if (methodType == MethodType.POST) {
                response = new NetworkManager().makeHttpPostConnection(requestURL, parameters);
            } else {
                response = new NetworkManager().makeHttpGetConnection(requestURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String jsonResponseObject) {
        /*if (requestCode != null)
            listener.asyncResponse(jsonResponseObject, requestCode);
        else
            listener.asyncResponse(jsonResponseObject);*/

        // Sending response back to listener
        if (jsonResponseObject.equals("failure") || jsonResponseObject.equals("error")) {
            CustomProgressDialog.hideProgressDialog();
            CustomAlertDialog.showAlertDialog(context, context.getResources().getString(R.string.error_title), context.getResources().getString(R.string.error_failure));
        } else if (jsonResponseObject.equals("timeout")) {
            CustomProgressDialog.hideProgressDialog();
            CustomAlertDialog.showAlertDialog(context, context.getResources().getString(R.string.error_title), context.getResources().getString(R.string.error_request_timeout));
        } else {
            listener.asyncResponse(jsonResponseObject, requestCode);
        }
    }

    @Override
    protected void onCancelled(String jsonResponseObject) {
        CustomProgressDialog.hideProgressDialog();

        // Sending response back to listener
        if (jsonResponseObject.equals("failure") || jsonResponseObject.equals("error")) {
            CustomAlertDialog.showAlertDialog(context, context.getResources().getString(R.string.error_title), context.getResources().getString(R.string.error_failure));
        } else if (jsonResponseObject.equals("timeout")) {
            CustomAlertDialog.showAlertDialog(context, context.getResources().getString(R.string.error_title), context.getResources().getString(R.string.error_request_timeout));
        } else {
            listener.asyncResponse(jsonResponseObject, requestCode);
        }
    }
}
