package ua.com.dquality.udrive.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import ua.com.dquality.udrive.R;

/**
 * Created by IPFAM on 4/2/2018.
 */

public class SharedPreferencesManager {

    private SharedPreferences mSharedPreferences;

    public SharedPreferencesManager(Context applicationContext){
        Context mApplicationContext = applicationContext;
        mSharedPreferences = mApplicationContext.getSharedPreferences(mApplicationContext.getResources().getString(R.string.app_shared_prefs), Context.MODE_PRIVATE);
    }

    public void clearAll(){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void writeIsLoggedInPreference(boolean isLoggedIn){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

    public boolean readIsLoggedInPreference(){
        return mSharedPreferences.getBoolean("isLoggedIn", false);
    }

    public void writeUserNamePreference(String userName){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public String readUserNamePreference(){
        return mSharedPreferences.getString("userName", null);
    }

    public void writeAccessTokenPreference(String accessToken){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putString("accessToken", accessToken);
        editor.apply();
    }

    public String readAccessTokenPreference(){
        return mSharedPreferences.getString("accessToken", null);
    }
}
