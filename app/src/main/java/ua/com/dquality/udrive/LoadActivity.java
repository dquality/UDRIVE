package ua.com.dquality.udrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;

public class LoadActivity extends AppCompatActivity implements OnRefreshHideListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        UDriveApplication.getHttpDataProvider().refreshAllData(null, this);
    }

    @Override
    public void onRefreshHide() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
