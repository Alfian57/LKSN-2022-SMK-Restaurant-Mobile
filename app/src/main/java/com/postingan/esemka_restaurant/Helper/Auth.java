package com.postingan.esemka_restaurant.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Auth {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Auth(Context context) {
        this.sharedPreferences = context.getSharedPreferences("AUTH", Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public String getToken(){
        return sharedPreferences.getString("TOKEN", null);
    }

    public void setToken(String token){
        editor.putString("TOKEN", "Bearer " + token);
        editor.commit();
        editor.apply();
    }
}
