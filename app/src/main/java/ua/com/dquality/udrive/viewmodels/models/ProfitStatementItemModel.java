package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;

public class ProfitStatementItemModel {
    public Date Date;
    public String Name;
    public Double Amount;
    public Boolean DisplayTime;
    public ProfitStatementItemModel(Date date, String name, Double amount, Boolean displayTime){
        Date = date;
        Name = name;
        Amount = amount;
        DisplayTime = displayTime;
    }
}
