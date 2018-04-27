package ua.com.dquality.udrive.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.com.dquality.udrive.AccountInfoActivity;
import ua.com.dquality.udrive.AddressesActivity;
import ua.com.dquality.udrive.LoginActivity;
import ua.com.dquality.udrive.MainActivity;
import ua.com.dquality.udrive.PublicOfferActivity;
import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.SupportActivity;
import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.helpers.SharedPreferencesManager;
import ua.com.dquality.udrive.viewmodels.DriverInfoViewModel;
import ua.com.dquality.udrive.viewmodels.models.DriverInfoModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherFragment extends HomeBaseFragment {

    private Intent mDrawerIntent;
    private TextView mHeaderAccountName;
    private TextView mHeaderAccountStatus;
    private ImageView mLevelImageView;

    public OtherFragment() {
    }

    private final NavigationView.OnNavigationItemSelectedListener onDrawerNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MainActivity mActivity = (MainActivity) getActivity();
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_drawer_home:
                    mDrawerIntent = new Intent(mActivity, AccountInfoActivity.class);
                    break;
//                case R.id.navigation_drawer_settings:
//                    mDrawerIntent = new Intent(mActivity, SettingsActivity.class);
//                    break;
                case R.id.navigation_drawer_question_answer:
                    //mDrawerIntent = new Intent(mActivity, QuestionAnswerActivity.class);
                    mDrawerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Const.URL_QUESTION_ANSWER));
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
                    SharedPreferencesManager manager = new SharedPreferencesManager(mActivity.getApplicationContext());
                    manager.clearAll();
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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

        View parentView = inflater.inflate(R.layout.fragment_other, container, false);

        NavigationView drawerNavigation = parentView.findViewById(R.id.navigation_drawer);
        drawerNavigation.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener);

        View headerView = drawerNavigation.getHeaderView(0);
        mHeaderAccountName = headerView.findViewById(R.id.header_account_name);
        mHeaderAccountStatus = headerView.findViewById(R.id.header_account_status);
        mLevelImageView = headerView.findViewById(R.id.header_account_status_image);

        onCreateViewBase();

        DriverInfoViewModel mDriverViewModelData = ViewModelProviders.of(getActivity()).get(DriverInfoViewModel.class);

        mDriverViewModelData.getLiveDataModel().observe(this, driverInfoModel -> onChangeDriverData(driverInfoModel));

        return parentView;
    }

    @Override
    protected void onChangeHomeData(HomeModel hModel) {
        super.onChangeHomeData(hModel);
        if(mHeaderAccountStatus != null){
            StatusLevel lvl = mViewModelData.getCurrentLevel();
            mHeaderAccountStatus.setText(getStatusLevel(lvl));
            int drawable = getStatusLevelDrawable(lvl);
            if(drawable != -1 && mLevelImageView != null){
                mLevelImageView.setBackgroundResource(drawable);
            }
        }
    }

    private void onChangeDriverData(DriverInfoModel dModel){
        if(mHeaderAccountName != null){
            mHeaderAccountName.setText(dModel.Name);
        }
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
}
