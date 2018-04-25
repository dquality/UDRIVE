package ua.com.dquality.udrive.viewmodels.models;

import java.util.Date;

public class DriverInfoModel {
    public String Name;
    public String Email;
    public String Phone;
    public Boolean IsPrivateBank;
    public String BankName;
    public String BankCardNumber;
    public String BankCardHolderName;
    public int CarProductionYear;
    public String CarBrand;
    public Boolean Sex;
    public Date Birthday;
    public Date PublicOfferAcceptanceDate;
    public DriverInfoModel(){}
    public DriverInfoModel(String name, String email, String phone,
             Boolean isPrivateBank,
             String bankName,
             String bankCardNumber,
             String bankCardHolderName,
             int carProductionYear,
             String carBrand,
             Boolean sex,
             Date birthday,
             Date publicOfferAcceptanceDate) {
        Name = name;
        Email = email;
        Phone = phone;
        IsPrivateBank = isPrivateBank;
        BankName = bankName;
        BankCardNumber = bankCardNumber;
        BankCardHolderName = bankCardHolderName;
        CarProductionYear = carProductionYear;
        CarBrand = carBrand;
        Sex = sex;
        Birthday = birthday;
        PublicOfferAcceptanceDate = publicOfferAcceptanceDate;
    }
}
