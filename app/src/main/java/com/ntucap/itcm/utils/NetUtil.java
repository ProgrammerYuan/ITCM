package com.ntucap.itcm.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.ntucap.itcm.ITCMApplication;
import com.ntucap.itcm.classes.ITCMUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.GET;

/**
 * Created by ProgrammerYuan on 13/06/17.
 */

public class NetUtil {

    private static final String LOG_TAG = "NETUTIL";
    private static RequestQueue mQueue = null;

    private static Context mContext = null;
    private static final String URL_DOMAIN = "http://155.69.148.209:8888/itcm";

    private static final String URL_USER_REGISTER = URL_DOMAIN + "/user/registration";
    private static final String URL_USER_LOGIN = URL_DOMAIN + "/oauth/token";
    private static final String URL_FORGOT_PASSWORD = URL_DOMAIN + "/user/forgetPassword";
    private static final String URL_RESET_PASSWORD = URL_DOMAIN + "/user/resetPassword";
    private static final String URL_REFRESH_TOKEN = URL_DOMAIN + "/oauth/token";
    private static final String URL_CHANGE_PASSWORD = URL_DOMAIN + "/api/user/updatePassword";
    private static final String URL_GET_USERINFO = URL_DOMAIN + "/api/user/userInfo";
    private static final String URL_UPLOAD_USERINFO = URL_DOMAIN + "/api/user/updateByJson";
    private static final String URL_UPLOAD_USER_PREF = URL_DOMAIN + "/api/user/userPreference";
    private static final String URL_UPLOAD_BAND_DATA = URL_DOMAIN + "/api/dataUpload";
    private static final String URL_GET_USER_PREF = URL_DOMAIN + "/api/user/userPreferenceRecord";
    private static final String URL_GET_BUILDING_STATUS = URL_DOMAIN + "/api/buildingStatus";
    private static final String URL_GET_REWARD = URL_DOMAIN + "/api/rewardHistory";


    private static final String USER_CLIENT_ID = "mobile";
    private static final String USER_CLIENT_SECRET = "e4624f06-9cda-4f25-8c88-2936617c0480";
    private static final String USER_PASSWORD_SALT = "$2a$11$o8vgy4olY7wcraHQKm4sqO";
    private static final String USER_SIGNIN_PROVIDER = "ITCM_USER";
    private static final String USER_LANGUAGE = "zh_CN";

    public static void init(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    private static void addRequestToQueue(Request request) {
        try {
            getQueueInstance().add(request);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "QUEUE NOT AVAILABLE");
        }
    }

    private static RequestQueue getQueueInstance() {
        if (mQueue != null) return mQueue;
        else if (mContext != null) return mQueue = Volley.newRequestQueue(mContext);
        return null;
    }

    public static void register(ITCMUser user, Response.Listener<JSONObject> listener,
                                Response.ErrorListener errorListener) {
        HashMap<String, String> params = user.getData();
        String password = ValidationUtil.validateHashmapGet(params, "password", "");
        password = BCrypt.hashpw(password, USER_PASSWORD_SALT);
        params.put("password", password);
        params.put("matchingPassword", password);
        params.put("signInProvider", USER_SIGNIN_PROVIDER);
        params.put("lang", USER_LANGUAGE);
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_USER_REGISTER, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void login(String username, String password, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", USER_CLIENT_ID);
        params.put("client_secret", USER_CLIENT_SECRET);
        params.put("grant_type", "password");
        params.put("username", username);
        params.put("password", BCrypt.hashpw(password, USER_PASSWORD_SALT));
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_USER_LOGIN, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    /**
     *
     * @param email User's email address to receive the password reset email
     * @param listener Listener for succeed request
     * @param errorListener Listener for failed request
     * JSON Returened:
     *      {
     *          "statue": int
     *          "message": String
     *          "body": String
     *      }
     */

    public static void forgotPassword(String email, Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_FORGOT_PASSWORD, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void resetPassword(String email, String password,
                                     Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", BCrypt.hashpw(password, USER_PASSWORD_SALT));
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_RESET_PASSWORD, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    /**
     *
     * @param listener Listener for succeed request
     * @param errorListener Listener for failed request
     *
     * JSON Returned:
     *      {
     *          "access_token": String
     *          "token_type": String --- "bearer"
     *          "refresh_token": String
     *          "expires_in": long
     *          "scope": "String"
     *      }
     */

    public static void refreshToken(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", USER_CLIENT_ID);
        params.put("client_secret", USER_CLIENT_SECRET);
        params.put("grant_type", "refresh_token");
        params.put("refresh_token", ITCMApplication.getRefreshToken());
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_REFRESH_TOKEN, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    /**
     *
     * @param oldPassword Previous password
     * @param password New password
     * @param listener Listener for succeed request
     * @param errorListener Listener for failed request
     *
     * JSON Returened:
     *      {
     *          "statue": int
     *          "message": String
     *          "body": String
     *      }
     */

    public static void changePassword(String oldPassword, String password, Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        password = BCrypt.hashpw(password, USER_PASSWORD_SALT);
        params.put("password", password);
        params.put("oldpassword", oldPassword);
        params.put("access_token", ITCMApplication.getAccessToken());
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_CHANGE_PASSWORD, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    /**
     *
     * @param listener Listener for succeed request
     * @param errorListener Listener for failed request
     *
     * JSON Returned:
     *      {
     *          "status": int
     *          "message": String
     *          "body": {
     *              "firstName": String
     *              "lastName": String
     *              "gender": String
     *              "createTime": TimeStamp
     *              "signInProvider": String---"ITCM_USER"
     *              "weight": int
     *              "email": String
     *              "age": int
     *              "height": int
     *          }
     *      }
     */

    public static void getUserInfo(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        requestGetWithParam(URL_GET_USERINFO, null, listener, errorListener);
        }

    public static void getUserPreference(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        requestGetWithParam(URL_GET_USER_PREF, null, listener, errorListener);
    }

    public static void updateUserInfo(Map<String, String> params, Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        if(params == null) params = new HashMap<>();
        params.put("lang", USER_LANGUAGE);
        params.put("access_token", ITCMApplication.getAccessToken());
        ITCMJSONRequest request = new ITCMJSONRequest(POST, URL_UPLOAD_USERINFO, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    /**
     *
     * @param listener Listener for succeed request
     * @param errorListener Listener for failed request
     *
     * JSON Returned:
     *      {
     *          "status": int
     *          "message": String
     *          "body": {
     *              "sectionArray": [
     *                  {
     *                      "dataArray": [
     *                          {
     *                              "rewardRemark": String
     *                              "rewardValue": String
     *                              "rewardType": String
     *                              "rewardQuantity": int
     *                              "rewardDate": String
     *                          }, ...
     *                      ]
     *                      "month": String
     *                      "year": String
     *                  }, ...
     *              ]
     *          }
     *      }
     */
    public static void getRewardHistory(Response.Listener<JSONObject> listener,
                                        Response.ErrorListener errorListener) {
        requestGetWithParam(URL_GET_REWARD, null, listener, errorListener);
    }

    /**
     *
     * @param params
     * @param listener
     * @param errorListener
     *
     * JSON Returned:
     *      {
     *
     *      }
     */
    public static void uploadUserPreference(Map<String, String> params,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        requestPostWithParam(URL_UPLOAD_USER_PREF, params, listener, errorListener);
    }

    public static void uploadUserBandData(Map<String, String> params,
                                          Response.Listener<JSONObject> listener,
                                          Response.ErrorListener errorListener) {
        requestPostWithParam(URL_UPLOAD_BAND_DATA, params, listener, errorListener);
    }

    public static void requestGetWithParam(String url, Map<String, String> params,
                                           Response.Listener<JSONObject> listener,
                                           Response.ErrorListener errorListener) {
        if (params == null) params = new HashMap<>();
        params.put("access_token", ITCMApplication.getAccessToken());
        ITCMJSONRequest request = new ITCMJSONRequest(GET, url, params, listener, errorListener);
        addRequestToQueue(request);
    }

    public static void requestPostWithParam(String url, Map<String, String> params,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        if (params == null) params = new HashMap<>();
        params.put("access_token", ITCMApplication.getAccessToken());
        ITCMJSONRequest request = new ITCMJSONRequest(POST, url, params, listener, errorListener);
        addRequestToQueue(request);
    }

    static String generateGETParamStr(String key, String value) {
        return key + "=" + value;
    }
}
