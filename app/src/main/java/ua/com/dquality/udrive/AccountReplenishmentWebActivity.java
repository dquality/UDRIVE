package ua.com.dquality.udrive;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.Response;
import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;

public class AccountReplenishmentWebActivity extends AppCompatActivity {
    private WebView mWebView;
    //private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_replenishment_web);
        mWebView = findViewById(R.id.account_replenishment_web_view);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient());
        UDriveApplication.getHttpDataProvider().postAccountReplenishment(onHttpCodeResultExposed);

        AuthenticateBaseActivity.SetLogo(this);
    }

    private OnHttpCodeResultExposed onHttpCodeResultExposed= new OnHttpCodeResultExposed(){

        @Override
        public void onResultExposed(Boolean isOkCode, Object data) {
            runOnUiThread(() -> {
                try {
                    Response response = (Response) data;
                    String content = response.body().string();
                    mWebView.loadDataWithBaseURL(response.request().url().uri().toString(), content, "text/html", "UTF-8", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    };
}
