package ua.com.dquality.udrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;

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
        SharedPreferencesManager manager = new SharedPreferencesManager(getApplicationContext());
        if(manager.readIsLoginPreference()){
            HttpDataProvider httpDataProvider = UDriveApplication.getHttpDataProvider();
            mLoggedInUserId = httpDataProvider.mLoggedInUserId = manager.readUserIdPreference();
            httpDataProvider.mCookies = manager.readCookiesPreferences();
            if(httpDataProvider.getDatas() == null) {
                httpDataProvider.initDefaultData();
                Intent intent = new Intent(this, LoadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
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
