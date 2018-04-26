package ua.com.dquality.udrive.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public abstract class BaseViewModel<TModel> extends ViewModel{

    private MutableLiveData<TModel> mData;

    public MutableLiveData<TModel> getLiveDataModel(){
        if(mData == null)
        {
            mData = new MutableLiveData<>();
            mData.setValue(getDefaultData());
        }

        return mData;
    }

    public void updateData(TModel model){
        if(mData != null) mData.postValue(model == null ? getDefaultData() : model);
    }

    protected  abstract TModel getDefaultData();
}
