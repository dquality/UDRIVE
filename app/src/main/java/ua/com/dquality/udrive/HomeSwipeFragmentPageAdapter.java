package ua.com.dquality.udrive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class HomeSwipeFragmentPageAdapter extends FragmentStatePagerAdapter {
    private static final int count  = 2;
    public HomeSwipeFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new DashboardFragment();
            case 1:
                return new CardFragment();
            default:
                throw new IndexOutOfBoundsException(String.format("Index was out of range:%s", count));

        }
//        Fragment fragment = new DashboardFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(DashboardFragment.ARG_OBJECT, i + 1);
//        fragment.setArguments(args);
//        return fragment;
    }

    @Override
    public int getCount() {
        return count;
    }
}
