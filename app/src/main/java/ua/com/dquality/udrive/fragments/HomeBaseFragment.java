package ua.com.dquality.udrive.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class HomeBaseFragment extends Fragment {

    protected HomeViewModel mViewModelData;

    public HomeBaseFragment() {
    }

    protected void onCreateViewBase(View parentView){
        mViewModelData = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        mViewModelData.getHomeData().observe(this, homeModel -> onChangeHomeData(homeModel));
    }

    protected HomeModel getDataModel(){
        return mViewModelData.getHomeData().getValue();
    }

    protected void onChangeHomeData(HomeModel hModel){

    }

    protected String getStatusLevel(StatusLevel lvl) {
        switch (lvl) {
            case Classic:
                return getString(R.string.title_status_classic);
            case Gold:
                return getString(R.string.title_status_gold);
            case Platinum:
                return getString(R.string.title_status_platinum);
            default:
                return getString(R.string.title_status_undefined);
        }
    }

    protected int getStatusLevelColor(StatusLevel lvl){
        Context ctx  = getContext();
        switch (lvl){
            case Classic:
                return ContextCompat.getColor(ctx, R.color.colorTextTitleClassic);
            case Gold:
                return ContextCompat.getColor(ctx, R.color.colorTextTitleGold);
            case Platinum:
                return ContextCompat.getColor(ctx, R.color.colorTextTitlePlatinum);
        }
        return -1;
    }
}
