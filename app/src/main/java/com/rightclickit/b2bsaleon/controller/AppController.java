package com.rightclickit.b2bsaleon.controller;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.rightclickit.b2bsaleon.constants.Constants;
import com.rightclickit.b2bsaleon.util.LruBitmapCache;

/**
 * Created by venkat on 11/07/16.
 */
public class AppController {
    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mAppControllerInstance;
    private static Context mApplicationContext;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private AppController(Context context) {
        mApplicationContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized AppController getInstance(Context context) {
        if (mAppControllerInstance == null) {
            mAppControllerInstance = new AppController(context);
        }

        return mAppControllerInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            /*Cache cache = new DiskBasedCache(mApplicationContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();*/

            mRequestQueue = Volley.newRequestQueue(mApplicationContext);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();

        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }

        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        // set retry policy
        req.setRetryPolicy(new DefaultRetryPolicy(Constants.VOLLEY_SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(Constants.VOLLEY_SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}