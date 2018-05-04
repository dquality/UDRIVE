package ua.com.dquality.udrive.data;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SimpleTimeZone;

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

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.fragments.dialogs.DateRangePickerFragment;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;
import ua.com.dquality.udrive.interfaces.OnHttpCodeResultExposed;
import ua.com.dquality.udrive.interfaces.OnRefreshHideListener;
import ua.com.dquality.udrive.viewmodels.ActiveViewModel;
import ua.com.dquality.udrive.viewmodels.DriverInfoViewModel;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;
import ua.com.dquality.udrive.viewmodels.UDriveInfoViewModel;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupType;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementItemModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoAddressModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoModel;

public class HttpDataProvider {
    private static final int HTTP_OK_CODE = 200;
    private static final int HTTP_UNAUTHORIZED_CODE = 401;

    private final Application mApplicationContext;
    private OkHttpClient mOkHttpClient;
    private DataModels mDataModels;
    private List<Cookie> mCookies;
    private AccountReplenishmentModel mAccountReplenishmentModel;

    public String mAccessToken;
    private String mUserName;

    public HttpDataProvider(Application applicationContext) {
        this.mApplicationContext = applicationContext;
        initNetworkClient();

        SharedPreferencesManager manager = new SharedPreferencesManager(mApplicationContext);

        mAccessToken = manager.readAccessTokenPreference();
        mUserName = manager.readUserNamePreference();
        mDataModels = new DataModels();
    }

    public DataModels getDataModels() {
        return mDataModels;
    }

    public AccountReplenishmentModel getAccountReplenishmentModel() {
        return mAccountReplenishmentModel;
    }

    private void initNetworkClient() {
        if(mOkHttpClient ==null) {
            mCookies = new ArrayList<>();
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
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

                HostnameVerifier verifier = (hostname, session) -> {
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                    return hv.verify(hostname, session);
                };

                mOkHttpClient = new OkHttpClient().newBuilder()
                        .addNetworkInterceptor(new StethoInterceptor())
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier(verifier)
                        .cookieJar(new CookieJar() {
                            @Override
                            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                                return mCookies;
                            }
                            @Override
                            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies){
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

    private <TViewModel extends ViewModel> TViewModel safeGetViewModel(FragmentActivity fragmentActivity, Class<TViewModel> type){
        if(fragmentActivity == null)
            return null;
        return ViewModelProviders.of(fragmentActivity).get(type);
    }

    public void changePeriod(CalendarDay fromDay, CalendarDay toDay, ProfitStatementViewModel viewModel) {
        new Thread(() -> refreshProfitStatementViewModelData(fromDay, toDay, viewModel)).start();
    }

    public void refreshAllData(FragmentActivity fragmentActivity, OnRefreshHideListener onRefreshHideListener){
        new Thread(() -> {
            refreshActiveViewModelData(safeGetViewModel(fragmentActivity, ActiveViewModel.class));

            refreshHomeViewModelData(safeGetViewModel(fragmentActivity, HomeViewModel.class));

            refreshProfitHistoryViewModelData(safeGetViewModel(fragmentActivity, ProfitHistoryViewModel.class));

            refreshProfitStatementViewModelData(null, null, safeGetViewModel(fragmentActivity, ProfitStatementViewModel.class));

            refreshDriverInfoViewModelData(safeGetViewModel(fragmentActivity, DriverInfoViewModel.class));

            refreshUDriveInfoViewModelData(safeGetViewModel(fragmentActivity, UDriveInfoViewModel.class));

            if(onRefreshHideListener != null)
                onRefreshHideListener.onRefreshHide();
        }).start();
    }

    public void refreshActiveViewModelData(ActiveViewModel activeViewModel){

        mDataModels.ActiveData  = new ActiveModel();

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetDriverStatus", "Active");

        ANResponse response = request.executeForOkHttpResponse();
        if(validateResponse(response)){
            try {
                mDataModels.ActiveData.StatusName = Objects.requireNonNull(response.getOkHttpResponse().body()).string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mDataModels.ActiveData.IsActive = mDataModels.ActiveData.StatusName.equals("ACTIVE");

            if(activeViewModel != null)
            {
                activeViewModel.updateData(mDataModels.ActiveData);
            }
        }

    }

    public void refreshHomeViewModelData(HomeViewModel homeViewModel){
        mDataModels.HomeData  = new HomeModel();

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetCurrentBalance", "Balance");

        ANResponse response = request.executeForOkHttpResponse();
        if(validateResponse(response)){
            try {
                String resp = Objects.requireNonNull(response.getOkHttpResponse().body()).string();
                if(!resp.isEmpty()){
                    mDataModels.HomeData.BalanceAmount = Double.valueOf(resp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetHomeData", "Home");

        ANResponse response2 = request.executeForJSONObject();
        if(validateResponse(response2)){
            try {
                JSONObject obj = (JSONObject)response2.getResult();
                mDataModels.HomeData.Level = StatusLevel.valueOfCustom(obj.getJSONObject("level").getString("levelName"));
                mDataModels.HomeData.NextMonthLevel = StatusLevel.valueOfCustom(obj.getJSONObject("nextLevel").getString("levelName"));
                mDataModels.HomeData.NextLevelPercentage = (int) (obj.getDouble("nextLevelPercentage") * 100);
                mDataModels.HomeData.UcoinsCount = obj.getInt("ucoinsCount");
                mDataModels.HomeData.WeekTripsCount = obj.getInt("weekTripsCount");
                mDataModels.HomeData.setBarcode(obj.getString("barcode"), mApplicationContext);
                mDataModels.HomeData.PrevMonthTripsCount = obj.getInt("prevMonthTripsCount");
                mDataModels.HomeData.RemainsTripsCount = obj.getInt("remainsTripsCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(homeViewModel != null)
            {
                homeViewModel.updateData(mDataModels.HomeData);
            }
        }
    }

    public void refreshProfitHistoryViewModelData(ProfitHistoryViewModel profitHistoryViewModel){
        mDataModels.ProfitHistoryData  = new ArrayList<>();

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetDriverProfitHistory", "ProfitHistory");

        ANResponse response = request.executeForJSONArray();
        if(validateResponse(response)){
            try {
                JSONArray arrayObj = (JSONArray)response.getResult();
                for (int groupIndex = 0 ; groupIndex < arrayObj.length(); groupIndex++ ) {
                    JSONObject groupEl = (JSONObject) arrayObj.get(groupIndex);

                    Date date = parseDate(groupEl.getString("date"));

                    List<ProfitHistoryItemModel> profitHistoryItems = new ArrayList<>();

                    if(groupEl.has("items")) {

                        JSONArray items = groupEl.getJSONArray("items");

                        for (int itemIndex = 0; itemIndex < items.length(); itemIndex++) {
                            JSONObject itemEl = (JSONObject) items.get(itemIndex);
                            Date itemDate = parseDate(itemEl.getString("date"));
                            String itemName = itemEl.getString("name");
                            Double itemAmount = itemEl.getDouble("amount");
                            ProfitHistoryItemModel itemModel = new ProfitHistoryItemModel(itemDate, itemName, itemAmount);
                            profitHistoryItems.add(itemModel);
                        }
                    }
                    ProfitHistoryGroupModel groupModel = new ProfitHistoryGroupModel(date, profitHistoryItems);
                    mDataModels.ProfitHistoryData.add(groupModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(profitHistoryViewModel != null)
            {
                profitHistoryViewModel.updateData(mDataModels.ProfitHistoryData);
            }
        }
    }

    public void refreshProfitStatementViewModelData(CalendarDay fromDay, CalendarDay toDay, ProfitStatementViewModel profitStatementViewModel){
        mDataModels.ProfitStatementData  = new ArrayList<>();

        if(fromDay == null && toDay == null){
            Calendar calendar = Calendar.getInstance();
            CalendarDay[] initDays = DateRangePickerFragment.calculateStartEndWeekDate(CalendarDay.from(calendar), calendar);
            fromDay = initDays[0];
            toDay = initDays[1];
        }

        mDataModels.StatementFromDay = fromDay;
        mDataModels.StatementToDay = toDay;

        Map<String,String> params = new HashMap<>();
        params.put("from", new SimpleDateFormat(Const.PARSE_DATE_FORMAT, new Locale(Const.CULTURE)).format(Objects.requireNonNull(fromDay).getDate()));

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetDriverProfitStatement","ProfitStatement", params);

        ANResponse response = request.executeForJSONArray();
        if(validateResponse(response)){
            try {
                JSONArray arrayObj = (JSONArray)response.getResult();
                for (int groupIndex = 0 ; groupIndex < arrayObj.length(); groupIndex++ ) {
                    JSONObject groupEl = (JSONObject) arrayObj.get(groupIndex);
                    String name = groupEl.getString("name");
                    Double amount = groupEl.getDouble("amount");
                    ProfitStatementGroupType type = ProfitStatementGroupType.getValue(groupEl.getInt("type"));

                    List<ProfitStatementItemModel> profitStatementItems = new ArrayList<>();
                    if(groupEl.has("items") && !groupEl.getString("items").equals("null")) {

                        JSONArray items = groupEl.getJSONArray("items");

                        for (int itemIndex = 0; itemIndex < items.length(); itemIndex++) {
                            JSONObject itemEl = (JSONObject) items.get(itemIndex);

                            Date itemDate = parseDate(itemEl.getString("date"));
                            String itemName = itemEl.getString("name");
                            Double itemAmount = itemEl.getDouble("amount");
                            Boolean displayTime = itemEl.getBoolean("displayTime");

                            ProfitStatementItemModel itemModel = new ProfitStatementItemModel(itemDate, itemName, itemAmount, displayTime);
                            profitStatementItems.add(itemModel);
                        }
                    }
                    ProfitStatementGroupModel groupModel = new ProfitStatementGroupModel(name,amount,type, profitStatementItems);
                    mDataModels.ProfitStatementData.add(groupModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(profitStatementViewModel != null)
            {
                profitStatementViewModel.updateData(mDataModels.ProfitStatementData);
            }
        }
    }

    public void refreshDriverInfoViewModelData(DriverInfoViewModel driverInfoViewModel){
        mDataModels.DriverInfoData  = new DriverInfoModel();

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetDriverDetails", "Home");

        ANResponse response = request.executeForJSONObject();
        if(validateResponse(response)){
            try {
                JSONObject obj = (JSONObject)response.getResult();
                mDataModels.DriverInfoData.Name = obj.getString("name");

                mDataModels.DriverInfoData.Email = obj.getString("masterEmail");
                if(mDataModels.DriverInfoData.Email.isEmpty())
                    mDataModels.DriverInfoData.Email = obj.getString("email");

                mDataModels.DriverInfoData.Phone = obj.getString("masterPhone");
                if(mDataModels.DriverInfoData.Phone.isEmpty())
                    mDataModels.DriverInfoData.Phone = obj.getString("phone");

                mDataModels.DriverInfoData.IsPrivateBank = obj.getBoolean("privatBank");
                mDataModels.DriverInfoData.BankName = obj.getString("bankName");
                mDataModels.DriverInfoData.BankCardNumber = obj.getString("cardNumber");
                mDataModels.DriverInfoData.BankCardHolderName = obj.getString("cardholderName");

                mDataModels.DriverInfoData.CarProductionYear = obj.getInt("carProductionYear");
                mDataModels.DriverInfoData.CarBrand = obj.getString("carBrand");
                mDataModels.DriverInfoData.Sex = obj.getInt("sex") > 0;

                mDataModels.DriverInfoData.Birthday = parseDate(obj.getString("birthday"));
                mDataModels.DriverInfoData.PublicOfferAcceptanceDate = parseDate(obj.getString("publicOfferAcceptanceDate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(driverInfoViewModel != null)
            {
                driverInfoViewModel.updateData(mDataModels.DriverInfoData);
            }
        }
    }

    public void refreshUDriveInfoViewModelData(UDriveInfoViewModel uDriveInfoViewModel){
        mDataModels.UDriveInfoData  = new UDriveInfoModel();

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetPartnerContactInfo", "ContactInfo");

        ANResponse response = request.executeForJSONObject();
        if(validateResponse(response)){
            try {
                JSONObject obj = (JSONObject)response.getResult();
                JSONArray phones = obj.getJSONArray("phones");
                JSONArray emails = obj.getJSONArray("emails");
                JSONArray messengers = obj.getJSONArray("messengers");
                JSONArray webSites = obj.getJSONArray("webSites");
                JSONArray addresses = obj.getJSONArray("addresses");

                for (int itemIndex = 0; itemIndex < phones.length(); itemIndex++) {
                    String phone = phones.getString(itemIndex);
                    mDataModels.UDriveInfoData.Phones.add(phone);
                }

                for (int itemIndex = 0; itemIndex < emails.length(); itemIndex++) {
                    String email = emails.getString(itemIndex);
                    mDataModels.UDriveInfoData.Emails.add(email);
                }

                for (int itemIndex = 0; itemIndex < messengers.length(); itemIndex++) {
                    JSONObject messenger = messengers.getJSONObject(itemIndex);
                    String key = messenger.getString("key");
                    String value = messenger.getString("value");
                    mDataModels.UDriveInfoData.Messengers.put(key, value);
                }

                for (int itemIndex = 0; itemIndex < webSites.length(); itemIndex++) {
                    JSONObject webSite = webSites.getJSONObject(itemIndex);
                    String key = webSite.getString("key");
                    String value = webSite.getString("value");
                    mDataModels.UDriveInfoData.WebSites.put(key, value);
                }

                for (int itemIndex = 0; itemIndex < addresses.length(); itemIndex++) {
                    JSONObject addressObj = addresses.getJSONObject(itemIndex);
                    String cityName = addressObj.getString("cityName");
                    String address = addressObj.getString("address");
                    mDataModels.UDriveInfoData.Addresses.add(new UDriveInfoAddressModel(cityName, address));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(uDriveInfoViewModel != null)
            {
                uDriveInfoViewModel.updateData(mDataModels.UDriveInfoData);
            }
        }
    }

    public void tryRedirectToAccountReplenishment(Double amount, OnHttpCodeResultExposed onHttpCodeResultExposed){

        Map<String,String> params = new HashMap<>();
        params.put("amount", amount.toString());

        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/AccountReplenishment",
                "TryRedirectToAccountReplenishment", params);

        request.getAsOkHttpResponseAndJSONObject(new OkHttpResponseAndJSONObjectRequestListener() {
            @Override
            public void onResponse(Response okHttpResponse, JSONObject response) {
                if(okHttpResponse.code() == HTTP_OK_CODE){
                    try {
                        String paymentUrl = response.getString("paymentUrl");
                        Double totalAmount = response.getDouble("totalAmount");
                        Double commissionAmount = response.getDouble("commissionAmount");
                        Map<String, String> data = new HashMap<>();

                        JSONObject pairs = response.getJSONObject("formData");
                        data.put("data", pairs.getString("data"));
                        data.put("signature", pairs.getString("signature"));

                        HttpDataProvider.this.mAccountReplenishmentModel = new AccountReplenishmentModel(paymentUrl, totalAmount, amount, commissionAmount, data);

                        if(onHttpCodeResultExposed != null){
                            onHttpCodeResultExposed.onResultExposed(okHttpResponse.code() == HTTP_OK_CODE, null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.try_process_account_replenishment_error_message));
                    }
                }
                else{
                    String msg = null;
                    try {
                        msg = Objects.requireNonNull(okHttpResponse.body()).string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.try_process_account_replenishment_error_message));
                    }
                    if(msg != null){
                        HttpDataProvider.this.showUIMessage(msg);
                    }
                }
            }

            @Override
            public void onError(ANError error) {
                showError(error);
            }
        });

    }

    public void tryGetPublicOfferContent(OnHttpCodeResultExposed onHttpCodeResultExposed) {
        ANRequest request = createGetRequest("https://backend.uberdrive.com.ua/Mobile/Api/GetDriverPublicOffer", "PublicOffer");

        new Thread(() -> {
            ANResponse response = request.executeForJSONObject();
            if(validateResponse(response)) {
                onHttpCodeResultExposed.onResultExposed(true, response.getResult());
            }
        }).start();
    }


    public void postAccountReplenishment(OnHttpCodeResultExposed onHttpCodeResultExposed){
        if(mAccountReplenishmentModel != null && onHttpCodeResultExposed != null){
            new Thread(() -> {
                ANRequest request = AndroidNetworking.post(mAccountReplenishmentModel.PaymentUrl)
                        .addBodyParameter(mAccountReplenishmentModel.FormData)
                        .setTag("Payment")
                        .build();

                ANResponse response = request.executeForOkHttpResponse();
                if(validateResponse(response)) {
                    onHttpCodeResultExposed.onResultExposed(true, response.getOkHttpResponse());
                }
            }).start();
        }
    }

    public void loginByPhone(String phone, OnHttpCodeResultExposed onHttpCodeResultExposed){
        if(phone.isEmpty()){
            showUIMessage(mApplicationContext.getString(R.string.enter_phone_required));
            return;
        }

        ANRequest request = AndroidNetworking.get("https://backend.uberdrive.com.ua/Mobile/Account/LoginBySms")
                .addQueryParameter("Phone", phone)
                .setTag("loginByPhone")
                .setPriority(Priority.IMMEDIATE)
                .build();

        request.getAsOkHttpResponse(new OkHttpResponseListener() {
            @Override
            public void onResponse(Response okHttpResponse) {
                String msg = null;
                try {
                    msg = Objects.requireNonNull(okHttpResponse.body()).string();
                } catch (IOException e) {
                    e.printStackTrace();
                    HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
                }
                if(msg != null){
                    HttpDataProvider.this.showUIMessage(msg);
                }
                if(onHttpCodeResultExposed != null){
                    onHttpCodeResultExposed.onResultExposed(okHttpResponse.code() == HTTP_OK_CODE, null);
                }
            }

            @Override
            public void onError(ANError error) {
                showError(error);
            }
        });
    }

    public void loginByCode(String code, OnHttpCodeResultExposed onHttpCodeResultExposed){
        if(code.isEmpty()){
            showUIMessage(mApplicationContext.getString(R.string.enter_code_required));
            return;
        }
        ANRequest request = AndroidNetworking.get("https://backend.uberdrive.com.ua/Mobile/Account/LoginBySmsCode")
                .addQueryParameter("Code", code)
                .setTag("loginByCode")
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

                        if(onHttpCodeResultExposed != null){
                            onHttpCodeResultExposed.onResultExposed(okHttpResponse.code() == HTTP_OK_CODE, null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
                    }
                }
                else{
                    String msg = null;
                    try {
                        msg = Objects.requireNonNull(okHttpResponse.body()).string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.login_error_message));
                    }
                    if(msg != null){
                        HttpDataProvider.this.showUIMessage(msg);
                    }
                }
            }

            @Override
            public void onError(ANError error) {
                showError(error);
            }
        });
    }

    private void showError(ANError error){
        if(error.getErrorDetail() != null && !error.getErrorDetail().isEmpty())
        {
            HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.network_error_message));
            return;
        }
        else if(error.getErrorBody() != null && !error.getErrorBody().isEmpty()){
            HttpDataProvider.this.showUIMessage(error.getErrorBody());
            return;
        }
        HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.network_error_message));
    }

    private void showUIMessage(String message){
        if(message != null && !message.isEmpty()){
            Toast.makeText(mApplicationContext, message, Toast.LENGTH_LONG).show();
        }
    }

    private ANRequest createGetRequest(String url, Object tag){
        return createGetRequest(url, tag, null);
    }

    private ANRequest createGetRequest(String url, Object tag, Map<String, String> params){
        if(params != null){
            return AndroidNetworking.get(url)
                    .setTag(tag)
                    .addQueryParameter(params)
                    .addHeaders("Authorization", "Bearer " + mAccessToken)
                    .build();
        }

        return AndroidNetworking.get(url)
                .setTag(tag)
                .addHeaders("Authorization", "Bearer " + mAccessToken)
                .build();
    }

    private Boolean validateResponse(ANResponse response){
        if(response.isSuccess() && response.getOkHttpResponse().code() == HTTP_OK_CODE){
            return true;
        }
        else if(response.getOkHttpResponse() != null && response.getOkHttpResponse().code() == HTTP_UNAUTHORIZED_CODE){
            SharedPreferencesManager manager = new SharedPreferencesManager(mApplicationContext);
            manager.clearAll();
        }
        else{
            HttpDataProvider.this.showUIMessage(mApplicationContext.getString(R.string.network_error_message));
        }
        return false;
    }

    private Date parseDate(String val){
        SimpleDateFormat dateFormat = new SimpleDateFormat(Const.PARSE_DATE_FORMAT, new Locale(Const.CULTURE));
        try {
            return dateFormat.parse(val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class DataModels{
        public ActiveModel ActiveData;
        public HomeModel HomeData;
        public List<ProfitStatementGroupModel> ProfitStatementData;
        public List<ProfitHistoryGroupModel> ProfitHistoryData;
        public DriverInfoModel DriverInfoData;
        public CalendarDay StatementFromDay;
        public CalendarDay StatementToDay;
        public UDriveInfoModel UDriveInfoData;
    }
}
