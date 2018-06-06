package com.ntucap.itcm.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * Created by ProgrammerYuan on 07/02/18.
 */

public class ITCMByteArrayRequest extends Request<byte[]> {

    private final Listener<byte[]> mListener;

    /**
     * Creates a new GET request.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the byte array response
     */
    public ITCMByteArrayRequest(String url, Listener<byte[]> listener,
                                Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link Method} to use
     * @param url URL to fetch the byte array at
     * @param listener Listener to receive the byte array response or error message
     */
    public ITCMByteArrayRequest(int method, String url, Listener<byte[]> listener,
                                Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    protected void deliverResponse(byte[] response) {
        if(null != mListener){
            mListener.onResponse(response);
        }
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    public String getBodyContentType() {
        return "application/octet-stream";
    }
}
