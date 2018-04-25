package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class ActiveViewModel extends ViewModel {

    private MutableLiveData<ActiveModel> mData;

    public MutableLiveData<ActiveModel> getActiveData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(ActiveModel model){
        if(mData != null) mData.postValue(model == null ? getDefaultData() : model);
    }
    private ActiveModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().ActiveData;
    }
}
