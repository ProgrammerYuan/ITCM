package com.ntucap.itcm.activities;

import android.support.v7.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.github.johnpersano.supertoasts.library.SuperToast;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.utils.NetUtil;

import static com.github.johnpersano.supertoasts.library.Style.DURATION_MEDIUM;

/**
 * Created by ProgrammerYuan on 22/07/17.
 */

public class ITCMActivity extends AppCompatActivity {

    @Override
    public void onStart() {
        super.onStart();
        if(ITCMApplication.hasRefreshToken()) {
            if(ITCMApplication.isTokenExpired()) {
                NetUtil.refreshToken(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
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
        private String message;

        public DefaultErrorListener() {
            this("Something Wrong With Your Network");
        }

        DefaultErrorListener(String message) {
            this.message = message;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            toast(message);
        }
    }

}
