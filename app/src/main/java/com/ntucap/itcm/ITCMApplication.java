package com.ntucap.itcm;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ntucap.itcm.classes.ITCMUser;
import com.ntucap.itcm.db.ITCMDB;
import com.ntucap.itcm.utils.DataUtil;
import com.ntucap.itcm.utils.NetUtil;

import java.util.Date;

/**
 * Created by ProgrammerYuan on 12/04/17.
 */

public class ITCMApplication extends Application {

    private static String sAccessToken = null;
    private static String sRefreshToken = null;
    private static long sTokenTimeStamp;
    private static long sTokenExpireDuration = 0;
    private static ITCMUser sCurrentUser = null;

    public ITCMApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ITCMDB.init(this);
        NetUtil.init(this);
    }

    public static void setAccessToken(String accessToken) {
        sAccessToken = accessToken;
        setTokenTimeStamp(new Date().getTime());
    }

    public static void setTokenExpireDuration(long tokenExpireDuration) {
        sTokenExpireDuration = tokenExpireDuration;
    }

    public static void setTokenTimeStamp(long tokenTimeStamp) {
        sTokenTimeStamp = tokenTimeStamp;
    }

    public static void setRefreshToken(String refreshToken) {
        sRefreshToken = refreshToken;
    }

    public static void setCurrentUser(ITCMUser user) {
        sCurrentUser = user;
    }

    public static String getAccessToken() {
        return sAccessToken;
    }

    public static String getRefreshToken() {
        return sRefreshToken;
    }

    public static long getTokenTimeStamp() {
        return sTokenTimeStamp;
    }

    public static long getsTokenExpireDuration() {
        return sTokenExpireDuration;
    }

    public static ITCMUser getCurrentUser() {
        if(sCurrentUser != null) return sCurrentUser;
        return sCurrentUser = ITCMDB.getCurrentUser();
    }

    public static String getCurrentUserEmail() {
        if(getCurrentUser() != null) return getCurrentUser().getEmail();
        return null;
    }

    public static boolean isTokenExpired() {
        if (sAccessToken == null) return true;
        long currentTime = new Date().getTime();
        return (currentTime > sTokenTimeStamp + sTokenExpireDuration);
    }

    public static boolean hasRefreshToken() {
        return sRefreshToken != null;
    }

}
