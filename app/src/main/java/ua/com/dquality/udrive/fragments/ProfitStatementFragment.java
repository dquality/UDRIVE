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
import ua.com.dquality.udrive.helpers.ExpandableProfitHistoryAdapter;
import ua.com.dquality.udrive.helpers.ExpandableProfitStatementAdapter;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryGroupModel;
import ua.com.dquality.udrive.viewmodels.ProfitHistoryViewModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfitStatementFragment extends Fragment {


    private ProfitStatementViewModel mViewModelData;
    private ExpandableProfitStatementAdapter mExpandableListAdapter;
    private ExpandableListView mExpandableListView;

    public ProfitStatementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_profit_statement, container, false);

        mExpandableListView = ret.findViewById(R.id.profit_statement_list);

        mViewModelData = ViewModelProviders.of(getActivity()).get(ProfitStatementViewModel.class);

        mExpandableListAdapter = new ExpandableProfitStatementAdapter(getContext());
        mExpandableListView.setAdapter(mExpandableListAdapter);

        mViewModelData.getProfitStatementData().observe(this, new Observer<ProfitStatementModel>() {
            @Override
            public void onChanged(@Nullable ProfitStatementModel profitStatementModel) {
                mExpandableListAdapter.setGroupItemData(profitStatementModel);
            }
        });

        return ret;
    }


}
