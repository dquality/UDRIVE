package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class HomeViewModel extends BaseViewModel<HomeModel> {

    @Override
    protected HomeModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().HomeData;
    }

    public StatusLevel getCurrentLevel(){
        StatusLevel lvl =  getLiveDataModel().getValue().Level;
        return lvl == null ? StatusLevel.Undefined : lvl;
    }
}
