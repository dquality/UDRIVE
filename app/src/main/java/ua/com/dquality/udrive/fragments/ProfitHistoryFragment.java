package ua.com.dquality.udrive.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.List;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.adapters.ExpandableProfitHistoryAdapter;
import ua.com.dquality.udrive.viewmodels.models.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfitHistoryFragment extends Fragment {

    private ProfitHistoryViewModel mViewModelData;
    private ExpandableProfitHistoryAdapter mExpandableListAdapter;
    private ExpandableListView mExpandableListView;

    public ProfitHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_profit_history, container, false);

        mExpandableListView = ret.findViewById(R.id.profit_history_list);


        mViewModelData = ViewModelProviders.of(getActivity()).get(ProfitHistoryViewModel.class);

        mExpandableListAdapter = new ExpandableProfitHistoryAdapter(getContext());
        mExpandableListView.setAdapter(mExpandableListAdapter);

        mViewModelData.getProfitHistoryData().observe(this, new Observer<List<ProfitHistoryGroupModel>>() {
            @Override
            public void onChanged(@Nullable List<ProfitHistoryGroupModel> profitHistoryGroupModels) {
                mExpandableListAdapter.setGroupItemData(profitHistoryGroupModels);
                for(int i=0; i < mExpandableListAdapter.getGroupCount(); i++)
                {
                    mExpandableListView.expandGroup(i);
                }
            }
        });

        return ret;
    }

}
