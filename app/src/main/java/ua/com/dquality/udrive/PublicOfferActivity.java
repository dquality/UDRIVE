package ua.com.dquality.udrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PublicOfferActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mLoggedInUserId == null || mLoggedInUserId.isEmpty()){
            return;
        }
        setContentView(R.layout.activity_public_offer);
    }
}
