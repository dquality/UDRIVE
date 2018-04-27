package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;
import java.util.List;

public class ProfitHistoryGroupModel {
    public final Date Date;
    public final List<ProfitHistoryItemModel> Items;
    public ProfitHistoryGroupModel(Date date, List<ProfitHistoryItemModel> items){
        Date = date;
        Items = items;
    }
}
