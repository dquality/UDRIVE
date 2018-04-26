package ua.com.dquality.udrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AccountInfoActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_account_info);
        SetLogo(this);
    }
}
