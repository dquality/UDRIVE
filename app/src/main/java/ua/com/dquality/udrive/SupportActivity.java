package ua.com.dquality.udrive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.viewmodels.UDriveInfoViewModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoAddressModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoModel;

public class SupportActivity extends AuthenticateBaseActivity {

    private UDriveInfoViewModel mUDriveInfoViewModelData;
    private TextView mInfoPhone1Title;
    private TextView mInfoPhone2Title;
    private TextView mInfoPhone3Title;

    private AppCompatButton mInfoPhone1Btn;
    private AppCompatButton mInfoPhone2Btn;
    private AppCompatButton mInfoPhone3Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_support);

        mInfoPhone1Title = findViewById(R.id.info_phone1_title);
        mInfoPhone2Title = findViewById(R.id.info_phone2_title);
        mInfoPhone3Title = findViewById(R.id.info_phone3_title);

        mInfoPhone1Btn = findViewById(R.id.info_phone1_btn);
        mInfoPhone1Btn.setOnClickListener(v -> {
            tryCallToPhone(0);
        });
        mInfoPhone2Btn = findViewById(R.id.info_phone2_btn);
        mInfoPhone2Btn.setOnClickListener(v -> {
            tryCallToPhone(1);
        });
        mInfoPhone3Btn = findViewById(R.id.info_phone3_btn);
        mInfoPhone3Btn.setOnClickListener(v -> {
            tryCallToPhone(2);
        });

        ImageView facebook = findViewById(R.id.info_facebook_mess);
        facebook.setOnClickListener(v -> {
            tryOpenMessenger(Const.FACEBOOK_MESS);
        });
        ImageView viber = findViewById(R.id.info_viber_mess);
        viber.setOnClickListener(v -> {
            tryOpenMessenger(Const.VIBER_MESS);
        });
        ImageView telegram = findViewById(R.id.info_telegram_mess);
        telegram.setOnClickListener(v -> {
            tryOpenMessenger(Const.TELEGRAM_MESS);
        });

        mUDriveInfoViewModelData = ViewModelProviders.of(this).get(UDriveInfoViewModel.class);
        mUDriveInfoViewModelData.getLiveDataModel().observe(this, uDriveInfoModel -> onChangeUdriveInfoData(uDriveInfoModel));

        SetLogo(this);
    }
    private void tryCallToPhone(int phoneIndex){
        if(getPhones().size() >= (phoneIndex + 1)){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getPhones().get(phoneIndex), null));
            startActivity(intent);
        }
    }

    private void tryOpenMessenger(String messenger){
        if(getMessengers().containsKey(messenger)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getMessengers().get(messenger)));
            startActivity(intent);
        }
    }

    private List<String> getPhones(){
        return mUDriveInfoViewModelData.getLiveDataModel().getValue().Phones;
    }

    private Map<String,String> getMessengers(){
        return mUDriveInfoViewModelData.getLiveDataModel().getValue().Messengers;
    }

    private void onChangeUdriveInfoData(UDriveInfoModel uDriveInfoModel){

        if(mInfoPhone1Title!= null && uDriveInfoModel.Phones.size() >= 1)
        {
            mInfoPhone1Btn.setVisibility(View.VISIBLE);
            mInfoPhone1Title.setVisibility(View.VISIBLE);
            mInfoPhone1Title.setText(uDriveInfoModel.Phones.get(0));
        }
        else{
            mInfoPhone1Btn.setVisibility(View.INVISIBLE);
            mInfoPhone1Title.setVisibility(View.INVISIBLE);
        }

        if(mInfoPhone2Title!= null && uDriveInfoModel.Phones.size() >= 2)
        {
            mInfoPhone2Btn.setVisibility(View.VISIBLE);
            mInfoPhone2Title.setVisibility(View.VISIBLE);
            mInfoPhone2Title.setText(uDriveInfoModel.Phones.get(1));
        }
        else{
            mInfoPhone2Btn.setVisibility(View.INVISIBLE);
            mInfoPhone2Title.setVisibility(View.INVISIBLE);
        }

        if(mInfoPhone3Title!= null && uDriveInfoModel.Phones.size() >= 3)
        {
            mInfoPhone3Btn.setVisibility(View.VISIBLE);
            mInfoPhone3Title.setVisibility(View.VISIBLE);
            mInfoPhone3Title.setText(uDriveInfoModel.Phones.get(2));
        }
        else{
            mInfoPhone3Btn.setVisibility(View.INVISIBLE);
            mInfoPhone3Title.setVisibility(View.INVISIBLE);
        }
    }
}
