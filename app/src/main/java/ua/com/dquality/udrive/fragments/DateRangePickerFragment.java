package ua.com.dquality.udrive.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

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
        View viewContent = layoutInflater.inflate(R.layout.statement_date_range_picker_veiw, null);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setPositiveButton(R.string.OK,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(mDateRangeSetListener !=null)
                                    mDateRangeSetListener.onDateRangeSet(mCalendarView, mStartDate, mEndDate);
                            }
                        }
                )
                .setNegativeButton(R.string.Cancel, null)
                .setView(viewContent)
                .create();

        mCalendar = Calendar.getInstance();

        mCalendarView = viewContent.findViewById(R.id.statement_week_view);

        mCalendar.setFirstDayOfWeek(mCalendarView.getFirstDayOfWeek());

        mCalendarView.setSelectionMode(SELECTION_MODE_RANGE);

        CalendarDay initialDay = initialSingleWeekDay == null ? CalendarDay.from(mCalendar): initialSingleWeekDay;
        mCalendarView.setCurrentDate(initialDay);
        CalendarDay[] initRet = calculateStartEndWeekDate(initialDay, mCalendar);
        mStartDate = initRet[0];
        mEndDate = initRet[1];
        mCalendarView.selectRange(mStartDate, mEndDate);


        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                CalendarDay[] ret = calculateStartEndWeekDate(date, mCalendar);
                mStartDate = ret[0];
                mEndDate = ret[1];
                widget.clearSelection();
                if(selected){
                    widget.selectRange(mStartDate, mEndDate);
                }
            }
        });

        return dialog;
    }

    public static CalendarDay[] calculateStartEndWeekDate(CalendarDay date, Calendar calendar){
        CalendarDay[] ret = new CalendarDay[2];
        calendar.set(date.getYear(), date.getMonth(), date.getDay());

        calendar.get(Calendar.DAY_OF_WEEK);

        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        ret[0] = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + 6);
        ret[1] = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        return ret;
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
