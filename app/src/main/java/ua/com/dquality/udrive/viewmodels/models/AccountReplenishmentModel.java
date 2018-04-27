package ua.com.dquality.udrive.viewmodels.models;

import java.util.Map;

public class AccountReplenishmentModel {
    public final String PaymentUrl;
    public final Double Amount;
    public final Double TotalAmount;
    public final Double CommissionAmount;
    public final Map<String, String> FormData;

    public AccountReplenishmentModel(String paymentUrl, Double totalAmount, Double amount, Double commissionAmount, Map<String, String> formData){
        PaymentUrl = paymentUrl;
        Amount = amount;
        TotalAmount = totalAmount;
        CommissionAmount = commissionAmount;
        FormData = formData;
    }
}
