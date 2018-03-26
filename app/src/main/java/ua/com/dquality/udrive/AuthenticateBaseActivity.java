package ua.com.dquality.udrive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.prefs.Preferences;

public class AuthenticateBaseActivity extends AppCompatActivity {

    protected String mLoggedInUserId;


    @Override
    protected void onResume() {
        checkUserAuthentication();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        checkUserAuthentication();
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserAuthentication();
    }

    private void checkUserAuthentication(){
//        mLoggedInUserId = "ssadfsd";
        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.app_shared_prefs), Context.MODE_PRIVATE);
        boolean isUserLoggedIn = sharedPrefs.getBoolean("loggedInState", false);

        if(isUserLoggedIn){
            mLoggedInUserId = sharedPrefs.getString("loggedInUserId", null);
        }
        else {
            mLoggedInUserId = null;
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
