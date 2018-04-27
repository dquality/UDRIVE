package ua.com.dquality.udrive.viewmodels.models;

public enum StatusLevel {
    Undefined,
    Classic,
    Gold,
    Platinum;
    private static final StatusLevel[] vals = values();
    public StatusLevel next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
