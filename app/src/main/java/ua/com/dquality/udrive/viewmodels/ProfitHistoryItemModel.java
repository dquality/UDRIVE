package ua.com.dquality.udrive.viewmodels;

import android.util.TypedValue;

import java.util.Date;

/**
 * Created by IPFAM on 3/18/2018.
 */

public class ProfitHistoryItemModel {
    public Date Date;
    public String Name;
    public Double Amount;
    public ProfitHistoryItemType Type;
    public ProfitHistoryItemModel(Date date, String name, Double amount){
        this(date, name, amount, ProfitHistoryItemType.Amount);
    }
    public ProfitHistoryItemModel(Date date, String name, Double amount, ProfitHistoryItemType type){
        Date = date;
        Name = name;
        Amount = amount;
        Type = type;
    }
}