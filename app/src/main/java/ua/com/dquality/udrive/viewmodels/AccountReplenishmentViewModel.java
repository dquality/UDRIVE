package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;

public class AccountReplenishmentViewModel extends ViewModel {

    private MutableLiveData<AccountReplenishmentModel> mData;

    public MutableLiveData<AccountReplenishmentModel> getActiveData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(AccountReplenishmentModel model){
        getActiveData().postValue(model == null ? getDefaultData() : model);
    }
    private AccountReplenishmentModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getAccountReplenishmentModel();
    }
}
