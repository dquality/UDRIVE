package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.AccountReplenishmentModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;

public class AccountReplenishmentViewModel extends BaseViewModel<AccountReplenishmentModel> {

    @Override
    protected AccountReplenishmentModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getAccountReplenishmentModel();
    }
}
