package ua.com.dquality.udrive.fragments;


import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.helpers.ExpandableProfitStatementAdapter;
import ua.com.dquality.udrive.viewmodels.ProfitStatementModel;
import ua.com.dquality.udrive.viewmodels.ProfitStatementViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfitStatementFragment extends Fragment implements DateRangePickerFragment.OnDateRangeSetListener, View.OnClickListener {


    private ProfitStatementViewModel mViewModelData;
    private ExpandableProfitStatementAdapter mExpandableListAdapter;
    private ExpandableListView mExpandableListView;
    private AppCompatButton mPickDateButton;

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

        mPickDateButton = ret.findViewById(R.id.profit_statement_pick_date_button);
        mPickDateButton.setOnClickListener(this);

        return ret;
    }

    @Override
    public void onDateRangeSet(MaterialCalendarView view, CalendarDay startDate, CalendarDay endDate) {
        //mPickDateButton.setText(String.format("Year: %d  Month: %d Day: %d",  year, month, dayOfMonth));

    }

    @Override
    public void onClick(View v) {
        DateRangePickerFragment newFragment = new DateRangePickerFragment();
        newFragment.initDateRangePickerFragment(this, null);
        newFragment.show(getActivity().getSupportFragmentManager(), "statementPeriodDatePicker");
    }
}
