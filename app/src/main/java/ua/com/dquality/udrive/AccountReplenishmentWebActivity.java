package ua.com.dquality.udrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;

public class AccountReplenishmentWebActivity extends AppCompatActivity {
    private WebView mWebView;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_replenishment_web);
        mWebView = findViewById(R.id.account_replenishment_web_view);
        mProgressBar = findViewById(R.id.progressBar);
        UDriveApplication.getHttpDataProvider().postAccountReplenishment(onHttpCodeResultExposed);

        AuthenticateBaseActivity.SetLogo(this);
    }

    private OnHttpCodeResultExposed onHttpCodeResultExposed= new OnHttpCodeResultExposed(){

        @Override
        public void onResultExposed(Boolean isOkCode, Object data) {
            mProgressBar.setVisibility(View.GONE);
            AccountReplenishmentModel model = UDriveApplication.getHttpDataProvider().getAccountReplenishmentModel();
            mWebView.loadDataWithBaseURL(model.PaymentUrl, (String)data, "text/html", "UTF-8", null);
        }
    };
}
