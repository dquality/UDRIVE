package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<HomeModel> mData;

    public MutableLiveData<HomeModel> getHomeData() {
        if (mData == null) {
            mData = new MutableLiveData<>();
            mData.setValue(getData());
        }
        return mData;
    }

    public void refreshData(){
        //TODO uncomment
        //mData.postValue(getData());

        HomeModel model = new HomeModel();

        model.Level = StatusLevel.Gold;
        model.NextLevelPercentage = 45;
        model.UcoinsCount = 128;
        model.Barcode = "3356 4673 7990 5332";

        model.PrevMonthTripsCount = 100;
        model.TodayTripsCount = 15;
        model.RemainsTripsCount = 800;
        model.BalanceAmount = -895;

        mData.postValue(model);
    }

    private HomeModel getData(){
        HomeModel model = new HomeModel();

        model.Level = StatusLevel.Classic;
        model.NextLevelPercentage = 20;
        model.UcoinsCount = 444;
        model.Barcode = "3356 4673 7990 5332";

        model.PrevMonthTripsCount = 1450;
        model.TodayTripsCount = 1;
        model.RemainsTripsCount = 80;
        model.BalanceAmount = 16245.4;

        return model;
    }
}
