package ua.com.dquality.udrive.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.arch.core.executor.ArchTaskExecutor;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.androidnetworking.AndroidNetworking;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class MainDataProvider {
    private FragmentActivity mFragmentActivity;
    private OkHttpClient mOkHttpClient;

    public MainDataProvider(FragmentActivity fragmentActivity){
        this.mFragmentActivity = fragmentActivity;
        initNetworkClient();
    }

    public void changePeriod(CalendarDay fromDay, CalendarDay toDay) {
        refreshProfitStatementViewModelData();
    }

    public void refreshAllData(){
       refreshActiveViewModelData();

       refreshHomeViewModelData();

       refreshProfitHistoryViewModelData();

       refreshProfitStatementViewModelData();
    }

    public void refreshActiveViewModelData(){
        ActiveViewModel  activeViewModel = ViewModelProviders.of(mFragmentActivity).get(ActiveViewModel.class);
        ActiveModel activeModel  = new ActiveModel();
        activeModel.IsActive = true;
        activeViewModel.updateData(activeModel);
    }

    public void refreshHomeViewModelData(){
        HomeViewModel  homeViewModel = ViewModelProviders.of(mFragmentActivity).get(HomeViewModel.class);
        HomeModel homeModel = new HomeModel();

        homeModel.Level = StatusLevel.Gold;
        homeModel.NextLevelPercentage = 45;
        homeModel.UcoinsCount = 128;
        homeModel.Barcode = "3356 4673 7990 5332";

        homeModel.PrevMonthTripsCount = 100;
        homeModel.TodayTripsCount = 15;
        homeModel.RemainsTripsCount = 800;
        homeModel.BalanceAmount = -895;

        homeViewModel.updateData(homeModel);
    }

    public void refreshProfitHistoryViewModelData(){

    }

    public void refreshProfitStatementViewModelData(){

    }


    private void initNetworkClient(){
        mOkHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(mFragmentActivity, mOkHttpClient);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
    }

//    public String GetCode(){
//        mOkHttpClient.newCall()
//        builder.
//    }
}
