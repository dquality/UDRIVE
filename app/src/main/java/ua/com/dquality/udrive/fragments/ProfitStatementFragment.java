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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ua.com.dquality.udrive.R;
import ua.com.dquality.udrive.helpers.Const;
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
    private CalendarDay mInitialDay;

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

        Calendar calendar = Calendar.getInstance();

        CalendarDay[] initDays = DateRangePickerFragment.calculateStartEndWeekDate(CalendarDay.from(calendar), calendar);
        setDateRange(initDays[0], initDays[1]);
        return ret;
    }

    private void setDateRange( CalendarDay startDate, CalendarDay endDate){
        String fromDateString = new SimpleDateFormat("dd MMMM yyyy", new Locale(Const.CULTURE)).format(startDate.getDate());
        String toDateString = new SimpleDateFormat("dd MMMM yyyy", new Locale(Const.CULTURE)).format(endDate.getDate());
        mInitialDay = startDate;
        mPickDateButton.setText(fromDateString + "    -    " + toDateString);
        mViewModelData.changePeriod(startDate, endDate);
    }

    @Override
    public void onDateRangeSet(MaterialCalendarView view, CalendarDay startDate, CalendarDay endDate) {
        setDateRange(startDate, endDate);
    }

    @Override
    public void onClick(View v) {
        DateRangePickerFragment dateRangeFragment = new DateRangePickerFragment();
        dateRangeFragment.initDateRangePickerFragment(this, mInitialDay);
        dateRangeFragment.show(getActivity().getSupportFragmentManager(), "statementPeriodDatePicker");
    }
}
