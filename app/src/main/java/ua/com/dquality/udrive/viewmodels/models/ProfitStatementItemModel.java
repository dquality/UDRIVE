package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;

public class ProfitStatementItemModel {
    public Date Date;
    public String Name;
    public Double Amount;
    public ProfitStatementItemModel(Date date, String name, Double amount){
        Date = date;
        Name = name;
        Amount = amount;
    }
}
