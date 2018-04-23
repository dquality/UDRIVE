package ua.com.dquality.udrive.viewmodels.models;

import java.util.Map;

public class AccountReplenishmentModel {
    public String PaymentUrl;
    public Double Amount;
    public Double TotalAmount;
    public Double CommissionAmount;
    public Map<String, String> FormData;

    public AccountReplenishmentModel(String paymentUrl, Double totalAmount, Double amount, Double commissionAmount, Map<String, String> formData){
        PaymentUrl = paymentUrl;
        Amount = amount;
        TotalAmount = totalAmount;
        CommissionAmount = commissionAmount;
        FormData = formData;
    }
}
