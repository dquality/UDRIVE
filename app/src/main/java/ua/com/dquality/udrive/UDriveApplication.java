package ua.com.dquality.udrive;

import android.app.Application;

import ua.com.dquality.udrive.data.HttpDataProvider;


public class UDriveApplication extends Application {
    private static HttpDataProvider HttpDataProvider;

    public static HttpDataProvider getHttpDataProvider() {
        return HttpDataProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HttpDataProvider = new HttpDataProvider(this);
    }
}
