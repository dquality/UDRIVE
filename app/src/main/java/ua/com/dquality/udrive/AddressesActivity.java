package ua.com.dquality.udrive;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import ua.com.dquality.udrive.viewmodels.ActiveViewModel;

public class AddressesActivity extends AuthenticateBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mLoggedInUserId == null || mLoggedInUserId.isEmpty()){
            return;
        }
        setContentView(R.layout.activity_addresses);
    }
}
