package com.winnie.views.timepiker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author winnie
 */
public class SystemDatePickerActivity extends AppCompatActivity {

    TextView mTvYear;

    TextView mTvMonthAndDay;

    TextView mTvWeekday;

    DatePicker mDatePicker;

    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_date_picker);
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
        mTvMonthAndDay = findViewById(R.id.tv_month_and_day);
        mTvWeekday = findViewById(R.id.tv_weekday);
        mDatePicker= findViewById(R.id.date_picker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mDatePicker.setOnDateChangedListener((view, year, month, dayOfMonth)
                    -> onTimeChanged(year, month + 1, dayOfMonth));
        }

        if (currentTime == 0) {
            currentTime = System.currentTimeMillis();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        int yearTemp = calendar.get(Calendar.YEAR);
        int monthTemp = calendar.get(Calendar.MONTH);
        int dayOfMonthTemp = calendar.get(Calendar.DAY_OF_MONTH);

        mDatePicker.init(yearTemp, monthTemp, dayOfMonthTemp, (view, year, monthOfYear, dayOfMonth)
                -> onTimeChanged(year, monthOfYear + 1, dayOfMonth));
        onTimeChanged(calendar);
        hideDatePickerHeader(mDatePicker);
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

    private long parseFromDay(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    private void hideDatePickerHeader(DatePicker datePicker) {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView .getChildAt(0);
        if (headerView == null) {
            return;
        }
        //5.0+
        int headerId = getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);

            ViewGroup.LayoutParams layoutParamsRoot = rootView.getLayoutParams();
            layoutParamsRoot.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(layoutParamsRoot);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams layoutParamsAnimator = animator.getLayoutParams();
            layoutParamsAnimator.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(layoutParamsAnimator);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams layoutParamsChild = child.getLayoutParams();
            layoutParamsChild.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(layoutParamsChild );
            return;
        }
        //6.0+
        headerId = getResources().getIdentifier("date_picker_header", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }
}
