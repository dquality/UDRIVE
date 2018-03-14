package ua.com.dquality.udrive.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ua.com.dquality.udrive.fragments.ProfitHistoryFragment;
import ua.com.dquality.udrive.fragments.ProfitStatementFragment;


public class ProfitPageAdapter extends FragmentStatePagerAdapter {

    public ProfitPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProfitHistoryFragment();
            case 1:
                return new ProfitStatementFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
