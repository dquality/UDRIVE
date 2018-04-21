package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;

public class DriverInfoViewModel extends ViewModel {

    private MutableLiveData<DriverInfoModel> mData;

    public MutableLiveData<DriverInfoModel> getActiveData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(DriverInfoModel model){
        if(mData != null) mData.postValue(model == null ? getDefaultData() : model);
    }
    private DriverInfoModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().DriverInfoData;
    }
}
