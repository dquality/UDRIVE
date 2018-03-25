package ua.com.dquality.udrive.viewmodels.models;

public enum StatusLevel {
    Classic,
    Gold,
    Platinum;
    private static StatusLevel[] vals = values();
    public StatusLevel next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
