package ua.com.dquality.udrive.helpers;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.lang.reflect.Field;

import ua.com.dquality.udrive.R;

/**
 * Created by IPFAM on 1/31/2018.
 */

public final class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static View extendView(BottomNavigationView view, LayoutInflater layoutInflater) {
        View shopBadge = null;
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//TODO Uncomment Shop
//                if(item.getItemData().getItemId() == R.id.navigation_udrive_shop){
//                    shopBadge = layoutInflater
//                            .inflate(R.layout.bottom_navigation_shop_badge, menuView, false);
//
//                    item.addView(shopBadge);
//                }
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
        return shopBadge;
    }
}
