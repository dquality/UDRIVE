package ua.com.dquality.udrive.viewmodels.models;

import java.util.Map;

public class AccountReplenishmentModel {
    public Double TotalAmount;
    public Double CommissionAmount;
    public Map<String, String> FormData;

    public AccountReplenishmentModel(Double totalAmount, Double commissionAmount, Map<String, String> formData){
        TotalAmount = totalAmount;
        CommissionAmount = commissionAmount;
        FormData = formData;
    }
}
