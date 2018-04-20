package ua.com.dquality.udrive.viewmodels.models;

public enum ProfitStatementGroupType {
    Header1,
    Header2;
    public static ProfitStatementGroupType getValue(int val)
    {
        ProfitStatementGroupType[] values = ProfitStatementGroupType.values();
        for(int i = 0; i < values.length; i++)
        {
            if(values[i].ordinal() == val)
                return values[i];
        }
        return ProfitStatementGroupType.Header1;
    }
}
