package ua.com.dquality.udrive;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;

import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.UDriveInfoViewModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoAddressModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoModel;

public class AddressesActivity extends AuthenticateBaseActivity {

    private AppCompatSpinner mCityList;
    private ArrayAdapter<String> mCityArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_addresses);

        mCityList = findViewById(R.id.info_city_list);
        mCityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCityList.setAdapter(mCityArrayAdapter);

        AppCompatButton mFollowCityOnMap = findViewById(R.id.btn_follow_city_on_map);

        UDriveInfoViewModel mUDriveInfoViewModelData = ViewModelProviders.of(this).get(UDriveInfoViewModel.class);
        mUDriveInfoViewModelData.getLiveDataModel().observe(this, uDriveInfoModel -> onChangeAddressesData(uDriveInfoModel));

        mFollowCityOnMap.setOnClickListener(v1 -> {
            Uri gmmIntentUri = Uri.parse(Const.GEO_UNKNOWN_PREFIX + mCityArrayAdapter.getItem(mCityList.getSelectedItemPosition()));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            try {
                startActivity(mapIntent);
            }
            catch (Exception ex){
                if(mapIntent.getPackage() != null && !mapIntent.getPackage().isEmpty())
                    mapIntent.setPackage(null);
                startActivity(mapIntent);
            }
        });

        SetLogo(this);
    }

    private void onChangeAddressesData(UDriveInfoModel uDriveInfoModel){

        if(mCityArrayAdapter != null){
            mCityArrayAdapter.clear();
            for (int i = 0; i<uDriveInfoModel.Addresses.size(); i++) {
                UDriveInfoAddressModel address = uDriveInfoModel.Addresses.get(i);
                mCityArrayAdapter.add(address.CityName + ", " + address.Address);
            }
        }
    }
}
