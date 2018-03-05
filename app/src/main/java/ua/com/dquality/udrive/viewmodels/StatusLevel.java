package ua.com.dquality.udrive.viewmodels;

/**
 * Created by IPFAM on 3/4/2018.
 */

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
