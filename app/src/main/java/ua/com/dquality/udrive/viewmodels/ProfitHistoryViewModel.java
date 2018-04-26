package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemType;

public class ProfitHistoryViewModel extends BaseViewModel<List<ProfitHistoryGroupModel>> {

    @Override
    protected List<ProfitHistoryGroupModel> getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().ProfitHistoryData;
    }
}
