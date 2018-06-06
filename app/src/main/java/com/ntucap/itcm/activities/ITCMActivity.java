package com.ntucap.itcm.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.classes.ITCMUserPreference;
import com.ntucap.itcm.utils.NetUtil;
import com.ntucap.itcm.utils.ValidationUtil;

import java.util.HashMap;

import static com.github.johnpersano.supertoasts.library.Style.DURATION_MEDIUM;

/**
 * Created by ProgrammerYuan on 22/07/17.
 */

public class ITCMActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        if (ITCMApplication.hasRefreshToken()) {
            if (ITCMApplication.isTokenExpired()) {
                NetUtil.refreshToken(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ITCMApplication.setAccessToken(response.getString("access_token"));
                        ITCMApplication.setTokenExpireDuration(response.getLong("expires_in"));
                        ITCMApplication.setRefreshToken(response.getString("refresh_token"));
                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        } else {
            ITCMUser user = ITCMApplication.getCurrentUser();
            if (user != null) {
                String username = user.getEmail(), password = user.getPassword();
                NetUtil.login(username, password, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ITCMApplication.setAccessToken(response.getString("access_token"));
                        ITCMApplication.setTokenExpireDuration(response.getLong("expires_in"));
                        ITCMApplication.setRefreshToken(response.getString("refresh_token"));
                    }
                }, new DefaultErrorListener());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected void toast(String message) {
        toastWithDuration(message, DURATION_MEDIUM);
    }

    protected void toastWithDuration(String message, int duration) {
        SuperToast.create(this, message, duration).show();
    }

    class DefaultErrorListener implements ErrorListener {
        private String mDefaultMessage;
        private HashMap<Integer, String> mMessage;

        DefaultErrorListener() {

        }

        DefaultErrorListener(HashMap<Integer, String> hashMap) {

        }

        DefaultErrorListener(String message) {
            this.mDefaultMessage = message;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("ErrorListener ", error.networkResponse.toString());
            if (mMessage != null) toast(ValidationUtil.validateHashmapGet(mMessage,
                    error.networkResponse.statusCode, mDefaultMessage));
            else toast(mDefaultMessage);
        }
    }

}
