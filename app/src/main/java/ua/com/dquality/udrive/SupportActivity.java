package ua.com.dquality.udrive;

import android.os.Bundle;

public class SupportActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_support);
    }
}
