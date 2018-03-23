package ua.com.dquality.udrive.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import ua.com.dquality.udrive.R;

import static com.prolificinteractive.materialcalendarview.MaterialCalendarView.SELECTION_MODE_RANGE;

public class DateRangePickerFragment extends DialogFragment {

    private CalendarDay mStartDate;
    private CalendarDay mEndDate;
    private Calendar mCalendar;
    private com.prolificinteractive.materialcalendarview.MaterialCalendarView mCalendarView;
    private CalendarDay initialSingleWeekDay;

    private DateRangePickerFragment.OnDateRangeSetListener mDateRangeSetListener;

    public void initDateRangePickerFragment(DateRangePickerFragment.OnDateRangeSetListener listener, CalendarDay singleWeekDay){
        mDateRangeSetListener = listener;
        initialSingleWeekDay = singleWeekDay;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Activity activity = getActivity();

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View viewContent = layoutInflater.inflate(R.layout.statement_week_veiw, null);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(mDateRangeSetListener !=null)
                                    mDateRangeSetListener.onDateRangeSet(mCalendarView, mStartDate, mEndDate);
                            }
                        }
                )
                .setNegativeButton("Cancel", null)
                .setView(viewContent)
                .create();

        mCalendar = Calendar.getInstance();

        mCalendarView = viewContent.findViewById(R.id.statement_week_view);

        mCalendar.setFirstDayOfWeek(mCalendarView.getFirstDayOfWeek());

        mCalendarView.setSelectionMode(SELECTION_MODE_RANGE);
        mCalendarView.setVerticalScrollBarEnabled(true);
        mCalendarView.setHorizontalScrollBarEnabled(true);


        CalendarDay initialDay = initialSingleWeekDay == null ? CalendarDay.from(mCalendar): initialSingleWeekDay;
        mCalendarView.setCurrentDate(initialDay);
        calculateStartEndWeekDate(initialDay);
        mCalendarView.selectRange(mStartDate, mEndDate);


        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                calculateStartEndWeekDate(date);
                widget.clearSelection();
                if(selected){
                    widget.selectRange(mStartDate, mEndDate);
                }
            }
        });

        return dialog;
    }

    private void calculateStartEndWeekDate(CalendarDay date){
        mCalendar.set(date.getYear(), date.getMonth(), date.getDay());

        mCalendar.get(Calendar.DAY_OF_WEEK);

        int firstDayOfWeek = mCalendar.getFirstDayOfWeek();
        mCalendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        mStartDate = CalendarDay.from(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
        mCalendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + 6);
        mEndDate = CalendarDay.from(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));
    }

    public interface OnDateRangeSetListener {
        /**
         * @param widget the picker associated with the dialog
         * @param startDate date from
         * @param endDate date to
         */
        void onDateRangeSet(MaterialCalendarView widget, CalendarDay startDate, CalendarDay endDate);
    }
}
