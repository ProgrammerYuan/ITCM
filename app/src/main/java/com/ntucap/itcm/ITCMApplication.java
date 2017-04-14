package com.ntucap.itcm;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ntucap.itcm.db.ITCMDB;

/**
 * Created by ProgrammerYuan on 12/04/17.
 */

public class ITCMApplication extends Application {

    public static RequestQueue queue;

    public ITCMApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ITCMDB.init(this);
        queue = Volley.newRequestQueue(this);
    }

    public static <T> Request<T> addVolleyRequest(Request<T> request) {
        return queue.add(request);
    }

}
