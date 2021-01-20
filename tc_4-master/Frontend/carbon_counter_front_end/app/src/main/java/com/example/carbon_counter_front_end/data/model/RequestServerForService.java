package com.example.carbon_counter_front_end.data.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for contacting the server
 */
public class RequestServerForService {
    IVolleyListener myListener;
    Context context;

    /**
     * RequestServerForService Constructor - Used for contacting the server
     * @param c context of the view
     * @param l interface for the view defining how to handle the responses
     */
    public RequestServerForService(Context c, IVolleyListener l) {
        this.context = c;
        this.myListener = l;
    }

    /**
     * Method for get server requests to return a JSONObject
     * @param url server url to contact
     */
    public void contactServer(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, // IF YOU WANT TO SEND A JSONOBJECT WITH POST THEN PASS IT HERE
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEY", "SERVER RESPONSE: " + response);
                        try {
                            myListener.onSuccess(response) ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
                System.out.println(error.getMessage());
                //Label stating failed username or password
                myListener.onError();
            }

        }

        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String credentials = UserInformation.username+":"+UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void postServer(String url, JSONObject stats) throws JSONException {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, stats, // IF YOU WANT TO SEND A JSONOBJECT WITH POST THEN PASS IT HERE
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEY", "SERVER RESPONSE: " + response);
                        try {
                            myListener.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
                //Label stating failed username or password
                myListener.onError();
            }
        }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String credentials = UserInformation.username+":"+UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    /**
     * Method for get server requests to return a Bitmap to display an image
     * @param url server url to contact
     */
    public void contactServerImage(String url){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                myListener.onImageSuccess(response);
            }
        }, 750, 500, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String credentials = UserInformation.username + ":" + UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(imageRequest);
    }
            /**
             * Method for get server requests to return a JSONArray
             * @param url server url to contact
             */
    public void contactServerArray(String url) {
        JsonArrayRequest jsonArrReq = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                myListener.onSuccessJSONArray(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String credentials = UserInformation.username + ":" + UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(jsonArrReq);
    }

    public void deleteRequest(String url)
    {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                url, null, // IF YOU WANT TO SEND A JSONOBJECT WITH POST THEN PASS IT HERE
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("VOLLEY", "SERVER RESPONSE: " + response);
                        try {
                            myListener.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("VOLLEY", "Error: " + error.getMessage());
                //Label stating failed username or password
                myListener.onError();
            }

        }


        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String credentials = UserInformation.username+":"+UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_req);
        Volley.newRequestQueue(context).add(jsonObjReq);
    }

    public void contactServerString(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myListener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myListener.onError();
            }
        }){


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                String credentials = UserInformation.username+":"+UserInformation.password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                params.put("Authorization", auth);

                return params;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }






}



