package com.ntucap.itcm.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.ntucap.itcm.classes.ITCMUser;

import java.util.HashMap;

/**
 * Created by ProgrammerYuan on 13/06/17.
 */

public class NetUtil {

    private static RequestQueue mQueue = null;
    private static Context mContext = null;

    private static final String URL_DOMAIN = "http://155.69.148.209:8888/itcm";
    private static final String URL_USER_REGISTER = URL_DOMAIN + "/user/registration";
    private static final String USER_PASSWORD_SALT = "$2a$11$o8vgy4olY7wcraHQKm4sqO";
    private static final String USER_SIGNIN_PROVIDER = "ITCM_USER";
    private static final String USER_LANGUAGE = "zh_CN";
    public static void init(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public static RequestQueue getQueueInstance() {
        if (mQueue != null) return mQueue;
        else if(mContext != null) return mQueue = Volley.newRequestQueue(mContext);
        return null;
    }

    public static void register(ITCMUser user){
        HashMap<String, String> params = user.getData();
        String password = ValidationUtility.validateHashmapGet(params, "password", "");
        password = BCrypt.hashpw(password, USER_PASSWORD_SALT);
        params.put("password", password);
        params.put("matchingPassword", password);
        params.put("signInProvider", USER_SIGNIN_PROVIDER);
        params.put("lang", USER_LANGUAGE);
        ITCMJSONRequest request = new ITCMJSONRequest(Request.Method.POST, URL_USER_REGISTER, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        getQueueInstance().add(request);
    }

}
