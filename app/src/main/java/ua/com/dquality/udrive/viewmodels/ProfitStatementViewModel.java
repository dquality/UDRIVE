package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;

public class ProfitStatementViewModel extends ViewModel {

    private MutableLiveData<List<ProfitStatementGroupModel>> mData;

    public MutableLiveData<List<ProfitStatementGroupModel>> getProfitStatementData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(List<ProfitStatementGroupModel> model){
        getProfitStatementData().postValue(model == null ? getDefaultData() : model);
    }

    private List<ProfitStatementGroupModel> getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDatas().ProfitStatementData;
    }
}
