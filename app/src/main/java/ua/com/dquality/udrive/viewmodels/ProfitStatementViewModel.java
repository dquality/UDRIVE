package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import ua.com.dquality.udrive.viewmodels.models.ProfitStatementModel;

public class ProfitStatementViewModel extends ViewModel {

    private MutableLiveData<ProfitStatementModel> mData;

    public MutableLiveData<ProfitStatementModel> getProfitStatementData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getData());
        }

        return mData;
    }

    public void changePeriod(CalendarDay fromDay, CalendarDay toDay){
        mData.setValue(getData());
    }

    private ProfitStatementModel getData(){
        return new ProfitStatementModel();
    }
}
