package ua.com.dquality.udrive.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;
import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;
import ua.com.dquality.udrive.viewmodels.ActiveViewModel;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;

public class HttpDataProvider {
    private static int HTTP_OK_CODE = 200;
    private static int HTTP_UNAUTHORIZED_CODE = 401;

    private Context mApplicationContext;
    private OkHttpClient mOkHttpClient;
    private DataModels mDataModels;

    public String mAccessToken;
    public String mUserName;

    public HttpDataProvider(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        initNetworkClient();
    }

    public DataModels getDataModels() {
        return mDataModels;
    }

    private void initNetworkClient() {
        if(mOkHttpClient ==null) {

            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                HostnameVerifier verifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                        return hv.verify(hostname, session);
                    }
                };

                mOkHttpClient = new OkHttpClient().newBuilder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier(verifier)
                        .build();
                AndroidNetworking.initialize(mApplicationContext, mOkHttpClient);
                AndroidNetworking.setParserFactory(new JacksonParserFactory());
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    private <TViewModel extends ViewModel> TViewModel safeGetViewModel(FragmentActivity fragmentActivity, Class<TViewModel> type){
        if(fragmentActivity == null)
            return null;
        return ViewModelProviders.of(fragmentActivity).get(type);
    }

    public void changePeriod(CalendarDay fromDay, CalendarDay toDay, ProfitStatementViewModel viewModel) {
        refreshProfitStatementViewModelData(viewModel);
    }

    public void refreshAllData(FragmentActivity fragmentActivity, OnRefreshHideListener onRefreshHideListener){
        new Thread(() -> {
            refreshActiveViewModelData(safeGetViewModel(fragmentActivity, ActiveViewModel.class));

            refreshHomeViewModelData(safeGetViewModel(fragmentActivity, HomeViewModel.class));

            refreshProfitHistoryViewModelData(safeGetViewModel(fragmentActivity, ProfitHistoryViewModel.class));

            refreshProfitStatementViewModelData(safeGetViewModel(fragmentActivity, ProfitStatementViewModel.class));

            if(onRefreshHideListener != null)
                onRefreshHideListener.onRefreshHide();
        }).start();
    }

    public void refreshActiveViewModelData(ActiveViewModel activeViewModel){

        ANRequest request = AndroidNetworking.get("https://backend.uberdrive.com.ua/Driver/Profile/GetDriverStatus")
                .setTag("Active")
                .addHeaders("Authorization", "Bearer " + mAccessToken)
                .setPriority(Priority.IMMEDIATE)
                .build();


        ANResponse<JSONObject> responce = request.executeForJSONObject();
        if(responce.isSuccess() && responce.getOkHttpResponse().code() == HTTP_OK_CODE){
            ActiveModel activeModel  = new ActiveModel();
            activeModel.IsActive = responce.getResult().equals("ACTIVE");
            mDataModels.ActiveData = activeModel;

            if(activeViewModel != null)
            {
                activeViewModel.updateData(activeModel);
            }
        }
        else if(responce.getOkHttpResponse().code() == HTTP_UNAUTHORIZED_CODE){
            SharedPreferencesManager manager = new SharedPreferencesManager(mApplicationContext);
            manager.clearAll();
        }
        else{
            HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
        }
    }

    public void refreshHomeViewModelData(HomeViewModel viewModel){
        //HomeViewModel homeViewModel = ViewModelProviders.of(mFragmentActivity).get(HomeViewModel.class);
//        HomeModel homeModel = new HomeModel();
//
//        homeModel.Level = StatusLevel.Gold;
//        homeModel.NextLevelPercentage = 45;
//        homeModel.UcoinsCount = 128;
//        homeModel.Barcode = "3356 4673 7990 5332";
//
//        homeModel.PrevMonthTripsCount = 100;
//        homeModel.WeekTripsCount = 15;
//        homeModel.RemainsTripsCount = 800;
//        homeModel.BalanceAmount = -895;
//
//        if(viewModel != null)
//            viewModel.updateData(homeModel);
    }

    public void refreshProfitHistoryViewModelData(ProfitHistoryViewModel viewModel){

    }

    public void refreshProfitStatementViewModelData(ProfitStatementViewModel viewModel){

    }

    public void LoginByPhone(String phone){
        ANRequest request = AndroidNetworking.post("https://backend.uberdrive.com.ua/Mobile/Account/LoginBySms")
                .addQueryParameter("Phone", phone)
                .setTag("LoginByPhone")
                .setPriority(Priority.IMMEDIATE)
                .build();

        request.getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response okHttpResponse) {
                if(okHttpResponse.code() == HTTP_OK_CODE){
                    try {
                        String successMsg = okHttpResponse.body().string();
                        HttpDataProvider.this.showUIMessage(successMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
                    }
                }
            }

            @Override
            public void onError(ANError error) {
                HttpDataProvider.this.showUIMessage(error.getErrorDetail());
            }
        });
    }

    public void LoginByCode(String code, OnRefreshHideListener onRefreshHideListener){
        ANRequest request = AndroidNetworking.post("https://backend.uberdrive.com.ua/Mobile/Account/LoginBySmsCode")
                .addQueryParameter("Code", code)
                .setTag("LoginByCode")
                .setPriority(Priority.IMMEDIATE)
                .build();

        request.getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                if(okHttpResponse.code() == HTTP_OK_CODE){
                    try {
                        String userName = response.getString("username");
                        String accessToken = response.getString("access_token");

                        SharedPreferencesManager manager = new SharedPreferencesManager(mApplicationContext);
                        manager.clearAll();

                        Boolean isLoggedIn  = (userName != null && !userName.isEmpty()) &&(accessToken != null && !accessToken.isEmpty());
                        manager.writeIsLoggedInPreference(isLoggedIn);
                        manager.writeUserNamePreference(userName);
                        manager.writeAccessTokenPreference(accessToken);

                        HttpDataProvider.this.mAccessToken = accessToken;
                        HttpDataProvider.this.mUserName = userName;
                        HttpDataProvider.this.mDataModels = new DataModels();
                        if(onRefreshHideListener != null)
                            onRefreshHideListener.onRefreshHide();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
                    }
                }
            }

            @Override
            public void onError(ANError error) {
                HttpDataProvider.this.showUIMessage(error.getErrorDetail());
            }
        });
    }

    private void showUIMessage(String message){
        if(message != null && !message.isEmpty()){
            Toast.makeText(mApplicationContext, message, Toast.LENGTH_LONG).show();
        }
    }

    public class DataModels{
        public ActiveModel ActiveData;
        public HomeModel HomeData;
        public List<ProfitStatementGroupModel> ProfitStatementData;
        public List<ProfitHistoryGroupModel> ProfitHistoryData;
    }
}
