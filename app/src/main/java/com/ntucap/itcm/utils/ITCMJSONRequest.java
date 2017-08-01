package com.ntucap.itcm.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ProgrammerYuan on 23/06/17.
 */

public class ITCMJSONRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> mListener;
    private Map<String, String> mParams;
    private final String mUrl;

    private static final String DEFAULT_BODY_KEY = "body";
    private static final String DEFAULT_STATUS_KEY = "status";
    private static final String DEFAULT_MESSAGE_KEY = "message";

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
    public String getUrl() {
        if (getMethod() == Method.GET) {
            String param = "?", sep;
            Set<String> keyset = getParams().keySet();
            int count = 0;
            for (String key : keyset) {
                if (count == 0) sep = "";
                else sep = "&";
                param += (sep + NetUtil.generateGETParamStr(
                            key, ValidationUtil.validateHashmapGet(getParams(), key, "")));
                count ++;
            }
            return mUrl + param;
        }
        return mUrl;
    }

    @Override
    protected Map<String, String> getParams() {
        if (mParams == null) mParams = new HashMap<>();
        return mParams;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            JSONObject jsonObject = JSONObject.parseObject(json), ret = jsonObject;
            if (jsonObject.containsKey(DEFAULT_BODY_KEY)) {
                Object body = jsonObject.get(DEFAULT_BODY_KEY);
                if(body instanceof JSONObject)
                    ret = jsonObject.getJSONObject(DEFAULT_BODY_KEY);
                int status;
                if(jsonObject.containsKey(DEFAULT_STATUS_KEY)) {
                    status = jsonObject.getIntValue(DEFAULT_STATUS_KEY);
                    if(status != 200)
                        return Response.error(
                            new VolleyError(jsonObject.getString(DEFAULT_MESSAGE_KEY))
                        );
                }
            }

            return Response.success(
                    ret,
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
