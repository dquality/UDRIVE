package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class ActiveViewModel extends ViewModel {

    private MutableLiveData<ActiveModel> mData;

    public MutableLiveData<ActiveModel> getActiveData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getData());
        }

        return mData;
    }

    public void refreshData(){
        ActiveModel data = getData();
        data.IsActive = true;
        mData.postValue(data);
    }
    private ActiveModel getData(){
        return new ActiveModel();
    }
}
