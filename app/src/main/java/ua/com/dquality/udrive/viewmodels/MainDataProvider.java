package ua.com.dquality.udrive.viewmodels;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jacksonandroidnetworking.JacksonParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by IPFAM on 3/25/2018.
 */

public class MainDataProvider {
    private FragmentActivity mFragmentActivity;
    private OkHttpClient mOkHttpClient;

    public MainDataProvider(FragmentActivity fragmentActivity){
        this.mFragmentActivity = fragmentActivity;
        initNetworkClient();
    }

    public void refreshData(){
        ActiveViewModel  activeModel = ViewModelProviders.of(mFragmentActivity).get(ActiveViewModel.class);
        activeModel.refreshData();

        HomeViewModel  homeModel = ViewModelProviders.of(mFragmentActivity).get(HomeViewModel.class);
        homeModel.refreshData();
    }

    private void initNetworkClient(){
        mOkHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(mFragmentActivity, mOkHttpClient);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
    }
}
