package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;

public class ProfitStatementItemModel {
    public final Date Date;
    public final String Name;
    public final Double Amount;
    public final Boolean DisplayTime;
    public ProfitStatementItemModel(Date date, String name, Double amount, Boolean displayTime){
        Date = date;
        Name = name;
        Amount = amount;
        DisplayTime = displayTime;
    }
}
