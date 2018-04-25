package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<HomeModel> mData;

    public MutableLiveData<HomeModel> getHomeData() {
        if (mData == null) {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }
        return mData;
    }

    public void updateData(HomeModel model){
        if(mData != null) mData.postValue(model == null ? getDefaultData() : model);
    }

    private HomeModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().HomeData;
    }
}
