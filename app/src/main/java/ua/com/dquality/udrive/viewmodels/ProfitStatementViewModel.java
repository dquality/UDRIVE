package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementGroupModel;

public class ProfitStatementViewModel extends BaseViewModel<List<ProfitStatementGroupModel>> {

    @Override
    protected List<ProfitStatementGroupModel> getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().ProfitStatementData;
    }

    public CalendarDay getStatementFromDay(){
        return UDriveApplication.getHttpDataProvider().getDataModels().StatementFromDay;
    }

    public CalendarDay getStatementToDay(){
        return UDriveApplication.getHttpDataProvider().getDataModels().StatementToDay;
    }
}
