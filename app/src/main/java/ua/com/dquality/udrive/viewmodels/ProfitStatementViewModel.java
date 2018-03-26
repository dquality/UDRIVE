package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.app.FragmentActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementModel;

public class ProfitStatementViewModel extends ViewModel {

    private MutableLiveData<ProfitStatementModel> mData;

    public MutableLiveData<ProfitStatementModel> getProfitStatementData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(ProfitStatementModel model){
        mData.postValue(model == null ? getDefaultData() : model);
    }

    private ProfitStatementModel getDefaultData(){
        return new ProfitStatementModel();
    }
}
