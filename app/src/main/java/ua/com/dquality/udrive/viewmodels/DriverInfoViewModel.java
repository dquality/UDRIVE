package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;

public class DriverInfoViewModel extends BaseViewModel<DriverInfoModel> {

    @Override
    protected DriverInfoModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().DriverInfoData;
    }
}
