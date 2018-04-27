package ua.com.dquality.udrive.viewmodels.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UDriveInfoModel {
    public final List<String> Phones;
    public final List<String> Emails;
    public final Map<String, String> Messengers;
    public final Map<String, String> WebSites;
    public final List<UDriveInfoAddressModel> Addresses;
    public UDriveInfoModel() {
        Phones = new ArrayList<>();
        Emails = new ArrayList<>();
        Messengers = new HashMap<>();
        WebSites= new HashMap<>();
        Addresses = new ArrayList<>();
    }
}

