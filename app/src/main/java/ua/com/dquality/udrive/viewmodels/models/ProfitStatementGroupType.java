package ua.com.dquality.udrive.viewmodels.models;

public enum ProfitStatementGroupType {
    Header1,
    Header2;
    public static ProfitStatementGroupType getValue(int val)
    {
        ProfitStatementGroupType[] values = ProfitStatementGroupType.values();
        for (ProfitStatementGroupType value : values) {
            if (value.ordinal() == val)
                return value;
        }
        return ProfitStatementGroupType.Header1;
    }
}
