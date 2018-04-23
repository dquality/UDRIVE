package ua.com.dquality.udrive;

import android.os.Bundle;

public class AddressesActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_addresses);
        SetLogo(this);
    }
}
