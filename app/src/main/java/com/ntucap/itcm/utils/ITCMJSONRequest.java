package com.ntucap.itcm.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ProgrammerYuan on 23/06/17.
 */

public class ITCMJSONRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> mListener;
    private Map<String, String> mParams;
    private final String mUrl;

    private static final String DEFAULT_BODY_KEY = "body";

    public ITCMJSONRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        this(method, url, null, listener, errorListener);
    }

    public ITCMJSONRequest(int method, String url, Map<String, String> params,
                           Response.Listener<JSONObject> listener, Response.ErrorListener errorListsener) {
        super(method, url, errorListsener);
        this.mUrl = url;
        this.mListener = listener;
        setParams(params);
    }

    @Override
    public Map<String, String> getParams() {
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    JSONObject.parseObject(json),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (JSONException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.mListener.onResponse(response);
    }

    public Map<String, String> getParamsMap() {
        if (mParams == null) mParams = new HashMap<>();
        return mParams;
    }

    public void setParams(Map<String, String> params) {
        mParams = params;
    }

    public void addParam(String key, String value) {
        getParamsMap().put(key, value);
    }

    public void addAllParams(Map<String, String> params) {
        getParamsMap().putAll(params);
    }
}
