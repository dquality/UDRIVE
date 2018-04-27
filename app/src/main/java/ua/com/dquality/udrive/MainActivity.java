package ua.com.dquality.udrive;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ua.com.dquality.udrive.constants.Const;
import ua.com.dquality.udrive.fragments.OtherFragment;
import ua.com.dquality.udrive.fragments.HomeFragment;
import ua.com.dquality.udrive.fragments.ProfitFragment;
import ua.com.dquality.udrive.helpers.BottomNavigationViewHelper;
import ua.com.dquality.udrive.viewmodels.ActiveViewModel;

public class MainActivity extends AuthenticateBaseActivity {

    private ActiveViewModel mViewModelData;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private View mShopBadge;
    private BottomNavigationView mBottomNavigation;

    private AppCompatButton mStatus;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_home:
                    mFragment = new HomeFragment();
                    break;
                case R.id.navigation_profit:
                    mFragment = new ProfitFragment();
                    break;
                case R.id.navigation_other:
                    mFragment = new OtherFragment();
                    break;
//TODO Uncomment Shop
//                case R.id.navigation_udrive_shop:
//                    mFragment = new UdriveShopFragment();
//                    mShopBadge.setVisibility(View.INVISIBLE);
//                    break;
            }
            final FragmentTransaction transaction = mFragmentManager.beginTransaction();
            transaction.replace(R.id.frameContainer, mFragment).commit();
            return true;
        }
    };

    private View.OnClickListener onStatusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if(!mViewModelData.getLiveDataModel().getValue().IsActive){
                Dialog dialog = new Dialog(MainActivity.this, R.style.NotActiveStatusPopupStyle);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window w = dialog.getWindow();
                w.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                w.setGravity(Gravity.TOP);

                View dialogView = dialog.getLayoutInflater().inflate(R.layout.top_up_balance_veiw, null);

                AppCompatButton accrualButton =  dialogView.findViewById(R.id.dialog_accrual_button);
                accrualButton.setOnClickListener(v1 -> {
                    showAccountReplenishmentDialog();
                    dialog.cancel();
                });

                dialog.setContentView(dialogView);
                dialog.setCancelable(true);
                dialog.show();
            }
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!mIsLoggedIn){
            return;
        }
        setContentView(R.layout.activity_main);

        mViewModelData = ViewModelProviders.of(this).get(ActiveViewModel.class);

        mViewModelData.getLiveDataModel().observe(this, activeModel -> setStatus(activeModel.IsActive));

        initStatus();

        initToolBar();

        initBottomNavigation(savedInstanceState);

        SetLogo(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getBooleanExtra(Const.RELOAD_DATA, false)){
            hideAccountReplenishmentDialog();
            if(mFragment instanceof HomeFragment){
                ((HomeFragment)mFragment).onRefreshShow();
            }
            else
                UDriveApplication.getHttpDataProvider().refreshAllData(this, null);
        }
    }

    private void initStatus(){
        mStatus = findViewById(R.id.status_button);
        mStatus.setOnClickListener(onStatusClickListener);
        setStatus(true);
    }

    private void setStatus(boolean active){
        if(mStatus != null){
            mStatus.setText(active ? R.string.active : R.string.notactive);
            mStatus.setBackgroundResource(active ? R.drawable.selector_status_active_background : R.drawable.selector_status_notactive_background);
        }
    }

    private void initToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initBottomNavigation(Bundle savedInstanceState){

        mFragmentManager = getSupportFragmentManager();

        mBottomNavigation = findViewById(R.id.navigation);

        mShopBadge = BottomNavigationViewHelper.extendView(mBottomNavigation, getLayoutInflater());

        mBottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if(savedInstanceState == null){
            mBottomNavigation.setSelectedItemId(mBottomNavigation.getSelectedItemId());
        }
    }
}
