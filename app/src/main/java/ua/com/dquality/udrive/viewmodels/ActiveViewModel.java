package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class ActiveViewModel extends BaseViewModel<ActiveModel> {

    @Override
    protected ActiveModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().ActiveData;
    }
}
