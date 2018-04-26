package ua.com.dquality.udrive.viewmodels;

import ua.com.dquality.udrive.UDriveApplication;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;
import ua.com.dquality.udrive.viewmodels.models.UDriveInfoModel;

public class UDriveInfoViewModel extends BaseViewModel<UDriveInfoModel> {
    @Override
    protected UDriveInfoModel getDefaultData(){
        return UDriveApplication.getHttpDataProvider().getDataModels().UDriveInfoData;
    }
}
