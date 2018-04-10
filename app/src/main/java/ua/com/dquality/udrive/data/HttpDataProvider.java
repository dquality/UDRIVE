package ua.com.dquality.udrive.data;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import ua.com.dquality.udrive.LoginActivity;
import ua.com.dquality.udrive.MainActivity;
import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;
import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;
import ua.com.dquality.udrive.viewmodels.ActiveViewModel;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemType;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class HttpDataProvider {
    private static int HTTP_OK_CODE = 200;

    private Context mApplicationContext;
    private OkHttpClient mOkHttpClient;
    private DataModels mDatas;

    public List<Cookie> mCookies = new ArrayList<Cookie>();
    public String mLoggedInUserId;

    public HttpDataProvider(Context applicationContext) {
        this.mApplicationContext = applicationContext;
        initNetworkClient();
    }

    public DataModels getDatas() {
        return mDatas;
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
                        .cookieJar(new CookieJar() {
                            @Override
                            public List<Cookie> loadForRequest(HttpUrl url) {
                                return mCookies;
                            }
                            @Override
                            public void saveFromResponse(HttpUrl url, List<Cookie> cookies){
                                List<Cookie> toAdd= new ArrayList<>();
                                for (int i = 0; i < cookies.size(); i++) {
                                    Cookie cookie = cookies.get(i);
                                    boolean find = false;
                                    for (int j = 0; j < mCookies.size(); j++) {
                                        Cookie mCookie = mCookies.get(i);
                                        if(mCookie.name().equals(cookie.name())){
                                            mCookies.set(i, cookie);
                                            find = true;
                                            break;
                                        }
                                    }
                                    if(!find){
                                        toAdd.add(cookie);
                                    }
                                }
                                mCookies.addAll(toAdd);
                            }
                        })
                        .build();
                AndroidNetworking.initialize(mApplicationContext, mOkHttpClient);
                AndroidNetworking.setParserFactory(new JacksonParserFactory());
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }

    public void initDefaultData(){
        if(mDatas == null)
            mDatas = new DataModels();

        //Init Active Model with default values
        mDatas.ActiveData = new ActiveModel();

        //Init Home Model with default values
        HomeModel homeData = new HomeModel();

        homeData.Level = StatusLevel.Classic;
        homeData.NextLevelPercentage = 20;
        homeData.UcoinsCount = 444;
        homeData.Barcode = "3356 4673 7990 5332";

        homeData.PrevMonthTripsCount = 1450;
        homeData.TodayTripsCount = 1;
        homeData.RemainsTripsCount = 80;
        homeData.BalanceAmount = 16245.4;

        mDatas.HomeData = homeData;

        //Init Profit History Model with default test data
        //For testing purpouse only
        List<ProfitHistoryGroupModel> profitHistoryData = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);

        ProfitHistoryGroupModel group1 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Комиссия сервиса", -51.41 ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 150.50 ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 12d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 250.00 ));


        ProfitHistoryGroupModel group2 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 15d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 3250.00 ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));

        ProfitHistoryGroupModel group3 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Комиссия сервиса", -350.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 100.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 1250.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));

        profitHistoryData.add(group1);
        profitHistoryData.add(group2);
        profitHistoryData.add(group3);

        mDatas.ProfitHistoryData = profitHistoryData;

        List<ProfitStatementGroupModel> profitStatementData = new ArrayList<>();
        mDatas.ProfitStatementData = profitStatementData;
        
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
                .addQueryParameter("driverId", mLoggedInUserId)
                .setPriority(Priority.IMMEDIATE)
                .build();


        ANResponse<JSONObject> responce = request.executeForJSONObject();
        if(responce.isSuccess() && responce.getOkHttpResponse().code() == HTTP_OK_CODE){
            ActiveModel activeModel  = new ActiveModel();
            try {
                activeModel.IsActive = responce.getResult().getString("status").equals("ACTIVE");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mDatas.ActiveData = activeModel;

            if(activeViewModel != null)
            {
                activeViewModel.updateData(activeModel);
            }
        }
        else{
            Toast.makeText(mApplicationContext, mApplicationContext.getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
        }
    }

    public void refreshHomeViewModelData(HomeViewModel viewModel){
        //HomeViewModel homeViewModel = ViewModelProviders.of(mFragmentActivity).get(HomeViewModel.class);
        HomeModel homeModel = new HomeModel();

        homeModel.Level = StatusLevel.Gold;
        homeModel.NextLevelPercentage = 45;
        homeModel.UcoinsCount = 128;
        homeModel.Barcode = "3356 4673 7990 5332";

        homeModel.PrevMonthTripsCount = 100;
        homeModel.TodayTripsCount = 15;
        homeModel.RemainsTripsCount = 800;
        homeModel.BalanceAmount = -895;

        if(viewModel != null)
            viewModel.updateData(homeModel);
    }

    public void refreshProfitHistoryViewModelData(ProfitHistoryViewModel viewModel){

    }

    public void refreshProfitStatementViewModelData(ProfitStatementViewModel viewModel){

    }

    public void Login2(String token){
        ANRequest request = AndroidNetworking.post("https://backend.uberdrive.com.ua/Account/Login")
                .addBodyParameter("LoginName", "pasha.rudenok@gmail.com")
                .addBodyParameter("Password", "20KHs54A")
                .addBodyParameter("RememberMe", "true")
                .addBodyParameter("__RequestVerificationToken", token)
                .setTag("Login2")
                .setPriority(Priority.HIGH)
                .build();

        request.getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response okHttpResponse) {
                if(okHttpResponse.code() == HTTP_OK_CODE){
                    SharedPreferencesManager manager = new SharedPreferencesManager(mApplicationContext);
                    manager.clearAll();
                    manager.writeIsLoginPreference(true);
                    manager.writeUserIdPreference("c2c20f43-f8b3-4178-afb6-0923f6d91f8c");
                    manager.writeCookiesPreferences(mCookies);
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(mApplicationContext, mApplicationContext.getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LogIn(){
        ANRequest request = AndroidNetworking.get("https://backend.uberdrive.com.ua/Account/Login")
                .setTag("Login")
                .setPriority(Priority.HIGH)
                .build();

        request.getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response okHttpResponse) {
                if(okHttpResponse.code() == HTTP_OK_CODE){
                    String token = null;
                    try {
                        String strBody = okHttpResponse.body().string();
                        String toFind = "CfDJ8F7tNUzGfNpPl";
                        int startIndex = strBody.indexOf(toFind);
                        int endIndex = strBody.indexOf("\" />", startIndex);
                        token = strBody.substring(startIndex, endIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(token != null)
                    {
                        Login2(token);
                    }
                }
            }

            @Override
            public void onError(ANError error) {
                Toast.makeText(mApplicationContext.getApplicationContext(), mApplicationContext.getString(R.string.login_error_message), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class DataModels{
        public ActiveModel ActiveData;
        public HomeModel HomeData;
        public List<ProfitStatementGroupModel> ProfitStatementData;
        public List<ProfitHistoryGroupModel> ProfitHistoryData;
    }
}
