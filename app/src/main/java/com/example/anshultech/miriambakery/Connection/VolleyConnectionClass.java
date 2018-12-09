package com.example.anshultech.miriambakery.Connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyConnectionClass {
    private static VolleyConnectionClass mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private NetworkConnectionInferface mNetworkConnectionInferface;

    private VolleyConnectionClass(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyConnectionClass getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyConnectionClass(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public JsonArrayRequest volleyJSONArrayRequest(String connectionURL,
                                                   Response.Listener<JSONArray> jsonArrayListener, Response.ErrorListener errorListener) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(connectionURL, jsonArrayListener, errorListener);
        return jsonArrayRequest;

    }

    public JsonObjectRequest VolleyJSONRequest(int InputMethod, String ConnectionURL, JSONObject jsonObject,
                                               Response.Listener<JSONObject> jsonObjectListener,
                                               Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(InputMethod, ConnectionURL, jsonObject,
                jsonObjectListener,
                errorListener
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        return jsonObjectRequest;
    }

    public StringRequest VolleyStringRequest(int InputMetod, String ConnectionURL, final String requestBody,
                                             Response.Listener<String> stringListener,
                                             Response.ErrorListener stringErrorListener) {

        StringRequest stringRequest = new StringRequest(InputMetod, ConnectionURL,
                stringListener,
                stringErrorListener) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        return stringRequest;
    }


    public <T> void addToRequestQueue(Request<T> req, NetworkConnectionInferface networkConnectionInferface) {
        mNetworkConnectionInferface= networkConnectionInferface;
        boolean netwoekChecking= checkNetworkAccess();
        if(netwoekChecking==true) {
            getRequestQueue().add(req);
        }
        else {
            mNetworkConnectionInferface.isNetworkAvailable();
        }
    }

    public interface NetworkConnectionInferface {
        void isNetworkAvailable();
    }

    private boolean checkNetworkAccess() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}