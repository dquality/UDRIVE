package ua.com.dquality.udrive.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.viewmodels.HomeViewModel;
import ua.com.dquality.udrive.viewmodels.models.HomeModel;
import ua.com.dquality.udrive.viewmodels.models.StatusLevel;

public class HomeBaseFragment extends Fragment {

    HomeViewModel mViewModelData;

    public HomeBaseFragment() {
    }

    void onCreateViewBase(View parentView){
        mViewModelData = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        mViewModelData.getLiveDataModel().observe(this, homeModel -> onChangeHomeData(homeModel));
    }

    HomeModel getDataModel(){
        return mViewModelData.getLiveDataModel().getValue();
    }

    void onChangeHomeData(HomeModel hModel){

    }

    String getStatusLevel(StatusLevel lvl) {
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

    int getStatusLevelColor(StatusLevel lvl){
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
