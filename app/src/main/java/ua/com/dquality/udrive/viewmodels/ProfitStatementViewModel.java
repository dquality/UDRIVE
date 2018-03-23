package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfitStatementViewModel extends ViewModel {

    private MutableLiveData<ProfitStatementModel> mData;

    public MutableLiveData<ProfitStatementModel> getProfitStatementData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(new ProfitStatementModel());
        }

        return mData;
    }

    public void changePeriod(CalendarDay fromDay, CalendarDay toDay){
        mData.setValue(new ProfitStatementModel());
    }
}
