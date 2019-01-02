package com.winnie.views.timepiker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.winnie.views.timepiker.adapter.YearAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author winnie
 */
public class CalendarDatePickerActivity extends AppCompatActivity {
    TextView mTvYear;

    TextView mTvMonthAndDay;

    TextView mTvWeekday;

    CalendarView mCalendarView;

    ListView mListView;

    private YearAdapter mAdapter;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_date_picker);
        initData();
        initView();
    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        currentTime = bundle.getLong("EXTRA_CURRENT_DATE_KEY");
    }

    private void initView() {
        mTvYear = findViewById(R.id.tv_year);
        mTvYear.setOnClickListener(v -> changeYearMode());

        mTvMonthAndDay = findViewById(R.id.tv_month_and_day);
        mTvMonthAndDay.setOnClickListener(v -> changeDayMode());

        mTvWeekday = findViewById(R.id.tv_weekday);
        mTvWeekday.setOnClickListener(v -> changeDayMode());

        mCalendarView = findViewById(R.id.calender_view);
        mCalendarView.setOnDateChangeListener((view, year, month, dayOfMonth)
                -> onTimeChanged(year, month + 1, dayOfMonth));

        if (currentTime == 0) {
            currentTime = System.currentTimeMillis();
        }
        mCalendarView.setDate(currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        onTimeChanged(calendar);

        mListView = findViewById(R.id.year_list_view);
        mAdapter = new YearAdapter(this, 1900, 2100);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            int year = mAdapter.getYearForPosition(position);
            calendar.setTimeInMillis(currentTime);
            calendar.set(Calendar.YEAR, year);
            currentTime = calendar.getTimeInMillis();
            mCalendarView.setDate(currentTime, false, false);
            onTimeChanged(calendar);
            changeDayMode();
        });
    }

    private void onTimeChanged(int year, int month, int dayOfMonth) {
        String timeString = year + "-" + month + "-" + dayOfMonth;
        currentTime = parseFromDay(timeString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        onTimeChanged(calendar);
    }

    private void onTimeChanged(Calendar calendar) {
        String dayOfWeek;
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                dayOfWeek = "周日";
                break;
            case Calendar.MONDAY:
                dayOfWeek = "周一";
                break;
            case Calendar.TUESDAY:
                dayOfWeek = "周二";
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case Calendar.THURSDAY:
                dayOfWeek = "周四";
                break;
            case Calendar.FRIDAY:
                dayOfWeek = "周五";
                break;
            case Calendar.SATURDAY:
            default:
                dayOfWeek = "周六";
                break;
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mTvWeekday.setText(dayOfWeek);
        mTvYear.setText(String.valueOf(year));
        mTvMonthAndDay.setText(String.format("%1$s月%2$s日", month + 1, dayOfMonth));
    }


    private void onViewClicked() {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putLong("EXTRA_SELECT_DATE_KEY", currentTime);
        data.putExtras(bundle);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void changeYearMode(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        int year = calendar.get(Calendar.YEAR);
        int position = mAdapter.setSelectedYear(year);
        mListView.setSelection(position - 3);
        mListView.setVisibility(View.VISIBLE);
        mCalendarView.setVisibility(View.GONE);
    }

    private void changeDayMode(){
        mListView.setVisibility(View.GONE);
        mCalendarView.setVisibility(View.VISIBLE);
    }

    private long parseFromDay(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }
}
