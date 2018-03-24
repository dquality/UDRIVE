package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.viewmodels.models.HomeModel;

/**
 * Created by IPFAM on 3/25/2018.
 */

public class HomeViewModel extends ViewModel {


    private MutableLiveData<HomeModel> mData;

    public MutableLiveData<HomeModel> getProfitHistoryData() {
        if (mData == null) {
            mData = new MutableLiveData<>();
            popuplateData();
        }
        return mData;
    }

    private void popuplateData(){

    }
}
