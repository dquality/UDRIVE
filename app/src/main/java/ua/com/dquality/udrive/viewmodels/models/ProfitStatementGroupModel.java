package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;
import java.util.List;

public class ProfitStatementGroupModel {
    public String Name;
    public Double Amount;
    public ProfitStatementGroupType Type;
    public List<ProfitStatementItemModel> Items;
    public ProfitStatementGroupModel(String name, Double amount, List<ProfitStatementItemModel> items){
        this(name, amount, ProfitStatementGroupType.Header1, items);
    }
    public ProfitStatementGroupModel(String name, Double amount, ProfitStatementGroupType type,  List<ProfitStatementItemModel> items){
        Name = name;
        Amount = amount;
        Type = type;
        Items = items;
    }
}
