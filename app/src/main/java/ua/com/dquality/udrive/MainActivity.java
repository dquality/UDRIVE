package ua.com.dquality.udrive;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import java.lang.reflect.Field;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import ua.com.dquality.udrive.fragments.UdriveShopFragment;
import ua.com.dquality.udrive.fragments.HomeFragment;
import ua.com.dquality.udrive.fragments.ProfitFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

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
                case R.id.navigation_udrive_shop:
                    mFragment = new UdriveShopFragment();
                    break;
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
            }

            if (mDrawerIntent != null) {
                MainActivity.this.startActivity(mDrawerIntent);
            }

            item.setChecked(true);

            mDrawerLayout.closeDrawers();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDrawerNavigation();

        initBottomNavigation();

        initNetworkClient();
    }


    private void initDrawerNavigation(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        NavigationView drawerNavigation = findViewById(R.id.navigation_drawer);
        drawerNavigation.setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener);
    }

    private void initBottomNavigation(){

        mFragmentManager = getSupportFragmentManager();

        BottomNavigationView bottomNavigation = findViewById(R.id.navigation);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);

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

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
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
        }
    }
}
