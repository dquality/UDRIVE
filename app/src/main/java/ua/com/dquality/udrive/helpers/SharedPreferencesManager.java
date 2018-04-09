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

    private Context mApplicationContext;
    private SharedPreferences mSharedPreferences;

    public SharedPreferencesManager(Context applicationContext){
        mApplicationContext = applicationContext;
        mSharedPreferences = mApplicationContext.getSharedPreferences(mApplicationContext.getResources().getString(R.string.app_shared_prefs), Context.MODE_PRIVATE);
    }

    public void clearAll(){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void writeCookiesPreferences(List<Cookie> cookieList){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();

//        String name, String value, long expiresAt, String domain, String path,
//        boolean secure, boolean httpOnly, boolean hostOnly, boolean persistent
        editor.putInt("CookieSize", cookieList.size());
        for (int i = 0; i < cookieList.size(); i++) {
            Cookie cookie = cookieList.get(i);
            String nameF = String.format("%d_%s", i, "name");
            String valueF = String.format("%d_%s", i, "value");
            String expiresAtF = String.format("%d_%s", i, "expiresAt");
            String domainF = String.format("%d_%s", i, "domain");
            String pathF = String.format("%d_%s", i, "path");
            String secureF = String.format("%d_%s", i, "secure");
            String httpOnlyF = String.format("%d_%s", i, "httpOnly");
            String hostOnlyF = String.format("%d_%s", i, "hostOnly");
            String persistentF = String.format("%d_%s", i, "persistent");

            editor.putString(nameF, cookie.name());
            editor.putString(valueF, cookie.value());
            editor.putLong(expiresAtF, cookie.expiresAt());
            editor.putString(domainF, cookie.domain());
            editor.putString(pathF, cookie.path());
            editor.putBoolean(secureF, cookie.secure());
            editor.putBoolean(httpOnlyF, cookie.httpOnly());
            editor.putBoolean(hostOnlyF, cookie.hostOnly());
            editor.putBoolean(persistentF, cookie.persistent());
        }
        editor.commit();
    }

    public List<Cookie> readCookiesPreferences(){
        List<Cookie> list = new ArrayList<Cookie>();
        for (int i = 0; i < mSharedPreferences.getInt("CookieSize", 0); i++) {

            String nameF = String.format("%d_%s", i, "name");
            String valueF = String.format("%d_%s", i, "value");
            String expiresAtF = String.format("%d_%s", i, "expiresAt");
            String domainF = String.format("%d_%s", i, "domain");
            String pathF = String.format("%d_%s", i, "path");
            String secureF = String.format("%d_%s", i, "secure");
            String httpOnlyF = String.format("%d_%s", i, "httpOnly");
            String hostOnlyF = String.format("%d_%s", i, "hostOnly");
            String persistentF = String.format("%d_%s", i, "persistent");

            Cookie.Builder cookieBuilder = new Cookie.Builder()
            .name(mSharedPreferences.getString(nameF, ""))
            .value(mSharedPreferences.getString(valueF, ""))
            .expiresAt(mSharedPreferences.getLong(expiresAtF, 0))
            .domain(mSharedPreferences.getString(domainF, ""))
            .path(mSharedPreferences.getString(pathF, ""));

            if(mSharedPreferences.getBoolean(secureF, false)){
                cookieBuilder.secure();
            }

            if(mSharedPreferences.getBoolean(httpOnlyF, false)){
                cookieBuilder.httpOnly();
            }

            if(mSharedPreferences.getBoolean(hostOnlyF, false)){
                cookieBuilder.hostOnlyDomain(mSharedPreferences.getString(domainF, ""));
            }
            list.add(cookieBuilder.build());
        }

        return list;
    }

    public void writeIsLoginPreference(boolean isLogin){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putBoolean("loggedInState", isLogin);
        editor.commit();
    }

    public boolean readIsLoginPreference(){
        //return false;
        return mSharedPreferences.getBoolean("loggedInState", false);
    }

    public void writeUserIdPreference(String userId){
        SharedPreferences.Editor editor =  mSharedPreferences.edit();
        editor.putString("loggedInUserId", userId);
        editor.commit();
    }

    public String readUserIdPreference(){
        return mSharedPreferences.getString("loggedInUserId", null);
    }
}
