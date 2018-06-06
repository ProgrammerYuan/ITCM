package com.ntucap.itcm.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.HashMap;

import static com.github.johnpersano.supertoasts.library.Style.DURATION_MEDIUM;

/**
 * Created by ProgrammerYuan on 06/02/18.
 */

public class ITCMErrorListener implements Response.ErrorListener {
    
    private static ITCMErrorListener sInstance;
    
    private String mDefaultMessage;
    private HashMap<Integer, String> mMessage;

    private static Context sContext;
    private static final String DEFAULT_HINT_MESSAGE = "Something Wrong With Your Network";


    public static void init(Context context) {
        sContext = context;
    }

    public static ITCMErrorListener getInstance() {
        if(sInstance == null) sInstance = new ITCMErrorListener();
        return sInstance;
    }

    private ITCMErrorListener() {
        this(DEFAULT_HINT_MESSAGE);
        mMessage.put(400, "Username or password is wrong");
        mMessage.put(401, "Username or password is wrong");
    }

    ITCMErrorListener(HashMap<Integer, String> errorMessages) {
        this(errorMessages, DEFAULT_HINT_MESSAGE);
    }

    ITCMErrorListener(String message) {
        this(null, message);
    }

    ITCMErrorListener(HashMap<Integer, String> errorMessage, String defaultMessage) {
        if (errorMessage == null) mMessage = new HashMap<>();
        else mMessage = errorMessage;
        mDefaultMessage = defaultMessage;
    }

    private void toast(String message) {
        SuperToast.create(sContext, message, DURATION_MEDIUM).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("ErrorListener ", error.networkResponse.toString());
        toast(ValidationUtil.validateHashmapGet(mMessage, error.networkResponse.statusCode, mDefaultMessage));
    }
}
