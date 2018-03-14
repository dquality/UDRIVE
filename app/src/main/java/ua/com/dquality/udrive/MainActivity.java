package ua.com.dquality.udrive;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import ua.com.dquality.udrive.fragments.OtherFragment;
import ua.com.dquality.udrive.fragments.UdriveShopFragment;
import ua.com.dquality.udrive.fragments.HomeFragment;
import ua.com.dquality.udrive.fragments.ProfitFragment;
import ua.com.dquality.udrive.helpers.BottomNavigationViewHelper;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private View mShopBadge;


    private boolean mStatusDefault = false;
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
            setStatus(!mStatusDefault);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initStatus();

        initDrawerNavigation();

        initBottomNavigation();

        initNetworkClient();
    }

    private void initStatus(){
        mStatus = findViewById(R.id.status_button);
        mStatus.setOnClickListener(onStatusClickListener);
        setStatus(true);
    }

    public void setStatus(boolean active){
        if(mStatus != null){
            mStatusDefault = active;
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

    private void initBottomNavigation(){

        mFragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigation = findViewById(R.id.navigation);

        mShopBadge = BottomNavigationViewHelper.extendView(bottomNavigation, getLayoutInflater());

        bottomNavigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        bottomNavigation.setSelectedItemId(R.id.navigation_home);
    }

    private void initNetworkClient(){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
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
