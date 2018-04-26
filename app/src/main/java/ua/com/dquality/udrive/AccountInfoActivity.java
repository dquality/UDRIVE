package ua.com.dquality.udrive;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.viewmodels.DriverInfoViewModel;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class AccountInfoActivity extends AuthenticateBaseActivity {
    private HomeViewModel mHomeViewModelData;
    private DriverInfoViewModel mDriverViewModelData;

    private TextView mInfoAccountStatus;
    private ImageView mLevelImageView;
    private TextView mInfoAccountName;
    private TextView mInfoCar;
    private TextView mInfoCarYear;
    private TextView mInfoEmail;
    private TextView mInfoPhone;
    private TextView mInfoBirthday;
    private TextView mInfoOfferAcceptanceDate;
    private TextView mInfoBank;
    private TextView mInfoBankCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_account_info);

        mInfoAccountStatus= findViewById(R.id.info_account_status);
        mLevelImageView= findViewById(R.id.info_account_status_image);
        mInfoAccountName = findViewById(R.id.info_account_name);
        mInfoCar= findViewById(R.id.info_car_value);
        mInfoCarYear= findViewById(R.id.info_car_year_value);
        mInfoEmail= findViewById(R.id.info_email_value);
        mInfoPhone= findViewById(R.id.info_phone_value);
        mInfoBirthday= findViewById(R.id.info_birthday_value);
        mInfoOfferAcceptanceDate = findViewById(R.id.info_offer_acceptance_date_value);
        mInfoBank = findViewById(R.id.info_bank_value);
        mInfoBankCard = findViewById(R.id.info_bank_card_value);

        mHomeViewModelData = ViewModelProviders.of(this).get(HomeViewModel.class);

        mHomeViewModelData.getLiveDataModel().observe(this, homeModel -> onChangeHomeData(homeModel));

        mDriverViewModelData = ViewModelProviders.of(this).get(DriverInfoViewModel.class);

        mDriverViewModelData.getLiveDataModel().observe(this, driverInfoModel -> onChangeDriverData(driverInfoModel));

        SetLogo(this);
    }

    private int getStatusLevelDrawable(StatusLevel lvl) {
        switch (lvl) {
            case Classic:
                return R.drawable.selector_oval_classic_background;
            case Gold:
                return R.drawable.selector_oval_gold_background;
            case Platinum:
                return R.drawable.selector_oval_platinum_background;
        }
        return -1;
    }

    private String getStatusLevel(StatusLevel lvl) {
        switch (lvl) {
            case Classic:
                return getString(R.string.title_status_classic);
            case Gold:
                return getString(R.string.title_status_gold);
            case Platinum:
                return getString(R.string.title_status_platinum);
            default:
                return getString(R.string.title_status_undefined);
        }
    }

    private void onChangeHomeData(HomeModel hModel){
        if(mInfoAccountStatus != null){
            StatusLevel lvl = mHomeViewModelData.getCurrentLevel();
            mInfoAccountStatus.setText(getStatusLevel(lvl));
            int drawable = getStatusLevelDrawable(lvl);
            if(drawable != -1 && mLevelImageView != null){
                mLevelImageView.setBackgroundResource(drawable);
            }
        }
    }

    private void onChangeDriverData(DriverInfoModel dModel){
        if(mInfoAccountName != null){
            mInfoAccountName.setText(dModel.Name);
        }

        if(mInfoCar != null){
            mInfoCar.setText(dModel.CarBrand);
        }

        if(mInfoCarYear != null){
            mInfoCarYear.setText(String.valueOf(dModel.CarProductionYear));
        }

        if(mInfoEmail != null){
            mInfoEmail.setText(dModel.Email);
        }

        if(mInfoPhone != null){
            mInfoPhone.setText(dModel.Phone);
        }

        if(mInfoOfferAcceptanceDate != null){
            mInfoOfferAcceptanceDate.setText(new SimpleDateFormat(Const.LONG_DATE_FROMAT, new Locale(Const.CULTURE)).format(dModel.PublicOfferAcceptanceDate));
        }

        if(mInfoBirthday != null){
            mInfoBirthday.setText(new SimpleDateFormat(Const.LONG_DATE_FROMAT, new Locale(Const.CULTURE)).format(dModel.Birthday));
        }

        if(mInfoBank != null){
            mInfoBank.setText(dModel.BankName);
        }

        if(mInfoBankCard != null){
            String formattedNumber = "";
            char[] charArray = dModel.BankCardNumber.toCharArray();
            for (int i=0; i< charArray.length; i++) {
                if((i % 4) == 0){
                    formattedNumber+= " ";
                }
                formattedNumber+= String.valueOf(charArray[i]);
            }
            mInfoBankCard.setText(formattedNumber);
        }
    }
}
