package ua.com.dquality.udrive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;

public class PublicOfferActivity extends AuthenticateBaseActivity {

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_public_offer);

        mWebView = findViewById(R.id.public_offer_web_view);
        mWebView.setWebViewClient(new WebViewClient());

        UDriveApplication.getHttpDataProvider().tryGetPublicOfferContent(onHttpCodeResultExposed);

        AuthenticateBaseActivity.SetLogo(this);
    }

    private OnHttpCodeResultExposed onHttpCodeResultExposed= new OnHttpCodeResultExposed(){

        @Override
        public void onResultExposed(Boolean isOkCode, Object data) {
            try {
                JSONObject obj = (JSONObject) data;
                String content = obj.getString("content");
                runOnUiThread(() -> {
                    mWebView.loadData(content, "text/html", "UTF-8");
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
