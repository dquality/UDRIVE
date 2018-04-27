package ua.com.dquality.udrive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.IOException;

import okhttp3.Response;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.data.HttpDataProvider;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;

public class AccountReplenishmentWebActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_replenishment_web);

        mWebView = findViewById(R.id.account_replenishment_web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebViewClient(new WebViewClient(){
            @SuppressLint("NewApi")
            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.contains("/Checkout/PaymentIsSuccessful/")){
                    Intent upIntent = getParentActivityIntent();
                    if(upIntent != null){
                        upIntent.putExtra(Const.RELOAD_DATA, true);
                    }
                    navigateUpTo(upIntent);
                }
                super.onPageFinished(view, url);
            }
        });
        UDriveApplication.getHttpDataProvider().postAccountReplenishment(onHttpCodeResultExposed);

        AuthenticateBaseActivity.SetLogo(this);
    }

    private final OnHttpCodeResultExposed onHttpCodeResultExposed= new OnHttpCodeResultExposed(){

        @Override
        public void onResultExposed(Boolean isOkCode, Object data) {
            try {
                Response response = (Response) data;
                String content = response.body().string();
                String url = response.request().url().toString();
                runOnUiThread(() -> {
                    mWebView.loadDataWithBaseURL(url, content, "text/html", "UTF-8", null);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}
