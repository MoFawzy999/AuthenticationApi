package com.fawzy.volley.controler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fawzy.volley.LoginActivity;
import com.fawzy.volley.model.User;

public class SessionManager {

    private static final String Shared_Pref_Name = "usertoken" ;
    private static final String Key_Name = "name" ;
    private static final String Key_Email = "email" ;
    private static final String Key_Token = "token" ;
    private static final String Key_ID = "user_id" ;

    private static SessionManager Instance ;
    private static Context context ;

    public SessionManager(Context context) {
        this.context = context ;
    }

// n3ml check lal user aza kan 3aml register aw login
    public static synchronized SessionManager getInstance(Context context){
        if (Instance == null){
           Instance = new SessionManager(context);
        }
        return Instance ;
    }

    public void userlogin(User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Key_ID,user.getId());
        editor.putString(Key_Email,user.getEmail());
        editor.putString(Key_Name,user.getName());
        editor.putString(Key_Token,user.getToken());
        editor.apply();
    }

    // check user is login or not
    public boolean chechlogin(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(Key_Token,null) != null;  // lw sawa null al user m3mlsh login aw 3aml logout
    }

    public User getToken (){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(Key_Token,null)
        );
    }

    public void userLogout(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public User UserInfo(User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Shared_Pref_Name,context.MODE_PRIVATE);
        return new User(sharedPreferences.getInt(Key_ID,0),
                sharedPreferences.getString(Key_Name,null),
                sharedPreferences.getString(Key_Email,null),
                sharedPreferences.getString(Key_Token,null)
        );
    }



}
