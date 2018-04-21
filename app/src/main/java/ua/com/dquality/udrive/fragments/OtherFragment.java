package ua.com.dquality.udrive.fragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.AddressesActivity;
import ua.com.dquality.udrive.MainActivity;
import ua.com.dquality.udrive.PublicOfferActivity;
import ua.com.dquality.udrive.QuestionAnswerActivity;
import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.SettingsActivity;
import ua.com.dquality.udrive.SupportActivity;
import ua.com.dquality.udrive.constants.Const;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends Fragment {

    private Intent mDrawerIntent;

    public OtherFragment() {
        // Required empty public constructor
    }

    private NavigationView.OnNavigationItemSelectedListener onDrawerNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MainActivity mActivity = (MainActivity) getActivity();
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_drawer_home:
                    mDrawerIntent = new Intent(mActivity, MainActivity.class);
                    break;
                case R.id.navigation_drawer_settings:
                    mDrawerIntent = new Intent(mActivity, SettingsActivity.class);
                    break;
                case R.id.navigation_drawer_question_answer:
                    mDrawerIntent = new Intent(mActivity, QuestionAnswerActivity.class);
                    break;
                case R.id.navigation_drawer_public_offer:
                    mDrawerIntent = new Intent(mActivity, PublicOfferActivity.class);
                    break;
                case R.id.navigation_drawer_addresses:
                    mDrawerIntent = new Intent(mActivity, AddressesActivity.class);
                    break;
                case R.id.navigation_drawer_support:
                    mDrawerIntent = new Intent(mActivity, SupportActivity.class);
                    break;
                case R.id.navigation_drawer_exit:
                    mActivity.finish();
                    break;
            }

            if (mDrawerIntent != null) {
                mActivity.startActivity(mDrawerIntent);
            }
            return true;
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret= inflater.inflate(R.layout.fragment_other, container, false);

        NavigationView drawerNavigation = ret.findViewById(R.id.navigation_drawer);
        drawerNavigation.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener);

        return ret;
    }
}
