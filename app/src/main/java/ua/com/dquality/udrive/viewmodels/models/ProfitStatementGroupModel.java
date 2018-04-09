package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;
import java.util.List;

public class ProfitStatementGroupModel {
    public Date Date;
    public String Name;
    public Double Amount;
    public ProfitStatementGroupType Type;
    public List<ProfitStatementItemModel> Items;
    public ProfitStatementGroupModel(Date date, String name, Double amount, List<ProfitStatementItemModel> items){
        this(date, name, amount, ProfitStatementGroupType.Header1, items);
    }
    public ProfitStatementGroupModel(Date date, String name, Double amount, ProfitStatementGroupType type,  List<ProfitStatementItemModel> items){
        Date = date;
        Name = name;
        Amount = amount;
        Type = type;
        Items = items;
    }
}
