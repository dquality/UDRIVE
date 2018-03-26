package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryItemType;

public class ProfitHistoryViewModel extends ViewModel {

    private MutableLiveData<List<ProfitHistoryGroupModel>> mData;

    public MutableLiveData<List<ProfitHistoryGroupModel>> getProfitHistoryData(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(List<ProfitHistoryGroupModel> model){
        mData.postValue(model == null ? getDefaultData() : model);
    }

    private List<ProfitHistoryGroupModel> getDefaultData(){
        // For testing purpouse only
        List<ProfitHistoryGroupModel> data = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);

        ProfitHistoryGroupModel group1 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Комиссия сервиса", -51.41 ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 150.50 ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 12d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group1.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 250.00 ));


        ProfitHistoryGroupModel group2 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 15d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 3250.00 ));
        cal.add(Calendar.MINUTE, 15);
        group2.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));

        ProfitHistoryGroupModel group3 = new ProfitHistoryGroupModel(cal.getTime(), new ArrayList<ProfitHistoryItemModel>());
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Комиссия сервиса", -350.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 100.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Пополнение баланса", 1250.00 ));
        cal.add(Calendar.MINUTE, 15);
        group3.Items.add(new ProfitHistoryItemModel(cal.getTime(), "Ucoins за поездку", 10d, ProfitHistoryItemType.Bonus ));

        data.add(group1);
        data.add(group2);
        data.add(group3);

        return data;
    }
}
