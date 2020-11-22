package com.fawzy.volley.controler;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleTone {

    private static VolleySingleTone Instance ;
    private RequestQueue requestQueue ;
    private static Context context ;

    public VolleySingleTone(Context context) {
        this.context =context ;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleTone getInstance(Context context){
        if (Instance == null){
            Instance = new VolleySingleTone(context);
        }
        return Instance ;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue ;
    }

    public <T> void addRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}
