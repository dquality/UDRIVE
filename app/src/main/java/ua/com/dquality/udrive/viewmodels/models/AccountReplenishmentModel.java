package ua.com.dquality.udrive.viewmodels.models;

import java.util.Map;

public class AccountReplenishmentModel {
    public String PaymentUrl;
    public Double TotalAmount;
    public Double CommissionAmount;
    public Map<String, String> FormData;

    public AccountReplenishmentModel(String paymentUrl, Double totalAmount, Double commissionAmount, Map<String, String> formData){
        PaymentUrl = paymentUrl;
        TotalAmount = totalAmount;
        CommissionAmount = commissionAmount;
        FormData = formData;
    }
}
