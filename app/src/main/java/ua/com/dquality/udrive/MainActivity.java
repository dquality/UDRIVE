package ua.com.dquality.udrive;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import ua.com.dquality.udrive.adapters.ExpandableProfitStatementAdapter;
import ua.com.dquality.udrive.fragments.OtherFragment;
import ua.com.dquality.udrive.fragments.UdriveShopFragment;
import ua.com.dquality.udrive.fragments.HomeFragment;
import ua.com.dquality.udrive.fragments.ProfitFragment;
import ua.com.dquality.udrive.helpers.BottomNavigationViewHelper;
import ua.com.dquality.udrive.viewmodels.ActiveViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;
import ua.com.dquality.udrive.viewmodels.models.ActiveModel;
import ua.com.dquality.udrive.viewmodels.models.ProfitStatementModel;

public class MainActivity extends AuthenticateBaseActivity {

    private ActiveViewModel mViewModelData;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private View mShopBadge;

    private AppCompatButton mStatus;

    private Intent mDrawerIntent;

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

    private NavigationView.OnNavigationItemSelectedListener onDrawerNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_drawer_home:
                    mDrawerIntent = new Intent(MainActivity.this, MainActivity.class);
                    break;
                case R.id.navigation_drawer_settings:
                    mDrawerIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    break;
                case R.id.navigation_drawer_question_answer:
                    mDrawerIntent = new Intent(MainActivity.this, QuestionAnswerActivity.class);
                    break;
                case R.id.navigation_drawer_public_offer:
                    mDrawerIntent = new Intent(MainActivity.this, PublicOfferActivity.class);
                    break;
                case R.id.navigation_drawer_addresses:
                    mDrawerIntent = new Intent(MainActivity.this, AddressesActivity.class);
                    break;
                case R.id.navigation_drawer_support:
                    mDrawerIntent = new Intent(MainActivity.this, SupportActivity.class);
                    break;
                case R.id.navigation_drawer_exit:
                    finish();
                    break;
            }

            if (mDrawerIntent != null) {
                MainActivity.this.startActivity(mDrawerIntent);
            }

            mDrawerLayout.closeDrawers();
            return true;
        }
    };

    private View.OnClickListener onStatusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            if(!mViewModelData.getActiveData().getValue().IsActive){
                Dialog dialog = new Dialog(MainActivity.this, R.style.NotActiveStatusPopupStyle);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                Window w = dialog.getWindow();
                w.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                w.setGravity(Gravity.TOP);

                View dialogView = dialog.getLayoutInflater().inflate(R.layout.top_up_balance_veiw, null);

                AppCompatButton accrualButton =  dialogView.findViewById(R.id.dialog_accrual_button);
                accrualButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, R.string.accrual_button_title,Toast.LENGTH_SHORT).show();
                    }
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
        if(mLoggedInUserId == null || mLoggedInUserId.isEmpty()){
            return;
        }
        setContentView(R.layout.activity_main);

        mViewModelData = ViewModelProviders.of(this).get(ActiveViewModel.class);

        mViewModelData.getActiveData().observe(this, new Observer<ActiveModel>() {
            @Override
            public void onChanged(@Nullable ActiveModel activeModel) {
                setStatus(activeModel.IsActive);
            }
        });

        initStatus();

        initDrawerNavigation();

        initBottomNavigation(savedInstanceState);
    }

    private void initStatus(){
        mStatus = findViewById(R.id.status_button);
        mStatus.setOnClickListener(onStatusClickListener);
        setStatus(true);
    }

    public void setStatus(boolean active){
        if(mStatus != null){
            mStatus.setText(active ? R.string.active : R.string.notactive);
            mStatus.setBackgroundResource(active ? R.drawable.selector_status_active_background : R.drawable.selector_status_notactive_background);
        }
    }

    private void initDrawerNavigation(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStatus = findViewById(R.id.status_button);
        mStatus.setOnClickListener(onStatusClickListener);
        setStatus(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        NavigationView drawerNavigation = findViewById(R.id.navigation_drawer);
        drawerNavigation.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener);
    }

    private void initBottomNavigation(Bundle savedInstanceState){

        mFragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigation = findViewById(R.id.navigation);

        mShopBadge = BottomNavigationViewHelper.extendView(bottomNavigation, getLayoutInflater());

        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if(savedInstanceState == null){
            bottomNavigation.setSelectedItemId(bottomNavigation.getSelectedItemId());
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
