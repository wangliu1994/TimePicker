package com.winnie.views.timepiker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.winnie.views.timepiker.adapter.YearAdapter;
import com.winnie.views.timepiker.decorator.RedWeekendsDecorator;
import com.winnie.views.timepiker.decorator.SelectorDecorator;

import org.threeten.bp.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author : winnie
 * @date : 2019/1/2
 * @desc
 */
public class CustomDatePikerActivity extends AppCompatActivity {
    TextView mTvYear;

    TextView mTvMonthAndDay;

    TextView mTvWeekday;

    MaterialCalendarView mCalendarView;

    ListView mListView;

    private YearAdapter mAdapter;
    private long mCurrentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_date_picker);

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
        mCurrentTime = bundle.getLong("EXTRA_CURRENT_DATE_KEY");
    }

    private void initView() {
        mTvYear = findViewById(R.id.tv_year);
        mTvYear.setOnClickListener(v -> changeYearMode());

        mTvMonthAndDay = findViewById(R.id.tv_month_and_day);
        mTvMonthAndDay.setOnClickListener(v -> changeDayMode());

        mTvWeekday = findViewById(R.id.tv_weekday);
        mTvWeekday.setOnClickListener(v -> changeDayMode());

        mCalendarView = findViewById(R.id.calender_view);
        mCalendarView.addDecorators(
                new SelectorDecorator(this),
                new RedWeekendsDecorator());
        mCalendarView.setSelectedDate(LocalDate.now().minusMonths(2));
        mCalendarView.setOnDateChangedListener((materialCalendarView, calendarDay, selected) -> {
            onTimeChanged(calendarDay);
        });

        if (mCurrentTime == 0) {
            mCurrentTime = System.currentTimeMillis();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCurrentTime);
        initTime(calendar);

        mListView = findViewById(R.id.year_list_view);
        mAdapter = new YearAdapter(this, 1900, 2100);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            int year = mAdapter.getYearForPosition(position);
            calendar.setTimeInMillis(mCurrentTime);
            calendar.set(Calendar.YEAR, year);
            initTime(calendar);
            changeDayMode();
        });
    }

    private void initTime(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) +1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        CalendarDay calendarDay = CalendarDay.from(LocalDate.of(year, month, dayOfMonth));
        onTimeChanged(calendarDay);
    }

    private void onTimeChanged(CalendarDay calendarDay) {
        int year = calendarDay.getYear();
        int month = calendarDay.getMonth() + 1;
        int dayOfMonth = calendarDay.getDay();
        String timeString = year + "-" + month + "-" + dayOfMonth;
        mCurrentTime = parseFromDay(timeString);

        String dayOfWeek;
        switch (calendarDay.getDate().getDayOfWeek()) {
            case SUNDAY:
                dayOfWeek = "周日";
                break;
            case MONDAY:
                dayOfWeek = "周一";
                break;
            case TUESDAY:
                dayOfWeek = "周二";
                break;
            case WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case THURSDAY:
                dayOfWeek = "周四";
                break;
            case FRIDAY:
                dayOfWeek = "周五";
                break;
            case SATURDAY:
            default:
                dayOfWeek = "周六";
                break;
        }
        mTvWeekday.setText(dayOfWeek);
        mTvYear.setText(String.valueOf(year));
        mTvMonthAndDay.setText(String.format("%1$s月%2$s日", month, dayOfMonth));
        mCalendarView.setSelectedDate(calendarDay);
        mCalendarView.setCurrentDate(calendarDay, false);
    }

    private void onViewClicked() {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
        bundle.putLong("EXTRA_SELECT_DATE_KEY", mCurrentTime);
        data.putExtras(bundle);
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    private void changeYearMode() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCurrentTime);
        int year = calendar.get(Calendar.YEAR);
        int position = mAdapter.setSelectedYear(year);
        mListView.setSelection(position - 3);
        mListView.setVisibility(View.VISIBLE);
        mCalendarView.setVisibility(View.GONE);
    }

    private void changeDayMode() {
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
