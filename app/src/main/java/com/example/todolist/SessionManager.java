package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    public static final String PREF_NAME="LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String FULLNAME = "FULLNAME";
    public static final String ID_USER = "ID_USER";


    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String nama_lengkap,String id_user){
        editor.putBoolean(LOGIN,true);
        editor.putString(FULLNAME,nama_lengkap);
        editor.putString(ID_USER,id_user);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin(){
        if (!this.isLoggin()) {
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            ((Home)context).finish();
        }
    }


    public HashMap<String, String> getUserDetail(){
        HashMap<String,String>user = new HashMap<>();
        user.put(FULLNAME,sharedPreferences.getString(FULLNAME,null));
        user.put(ID_USER,sharedPreferences.getString(ID_USER,null));

        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,MainActivity.class);
        context.startActivity(i);
        ((Home)context).finish();
    }
}
