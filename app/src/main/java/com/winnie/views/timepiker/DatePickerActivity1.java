package com.winnie.views.timepiker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author winnie
 */
public class DatePickerActivity1 extends AppCompatActivity {
    TextView mTvYear;

    TextView mTvMonthAndDay;

    TextView mTvWeekday;

    CalendarView mCalenderView;

    ListView mListView;

    private  YearAdapter mAdapter;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker1);
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

        mCalenderView = findViewById(R.id.calender_view);
        mCalenderView.setOnDateChangeListener((view, year, month, dayOfMonth)
                -> onTimeChanged(year, month + 1, dayOfMonth));

        if (currentTime == 0) {
            currentTime = System.currentTimeMillis();
        }
        mCalenderView.setDate(currentTime);
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
            mCalenderView.setDate(currentTime, false, false);
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
        mAdapter.setSelectedYear(year);
        mListView.setVisibility(View.VISIBLE);
        mCalenderView.setVisibility(View.GONE);
    }

    private void changeDayMode(){
        mListView.setVisibility(View.GONE);
        mCalenderView.setVisibility(View.VISIBLE);
    }

    private long parseFromDay(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    private class YearAdapter extends BaseAdapter{
        private int mMinYear;
        private int mCount;
        private int mSelectedYear;
        private Context mContext;

        public YearAdapter(Context context, int minYear, int maxYear) {
            mContext = context;
            mMinYear = minYear;
            mCount = maxYear - minYear;
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Integer getItem(int position) {
            return getYearForPosition(position);
        }

        @Override
        public long getItemId(int position) {
            return getPositionForYear(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_year_item, parent, false);
            }
            int year = getYearForPosition(position);
            TextView textView = convertView.findViewById(R.id.year_text);
            textView.setText(String.valueOf(year));
            if(mSelectedYear == year){
                textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            }else {
                textView.setTextColor(mContext.getResources().getColor(R.color.textPrimary));
            }
            return convertView;
        }

        public int getPositionForYear(int year) {
            return year - mMinYear;
        }

        public int getYearForPosition(int position) {
            return mMinYear + position;
        }

        public void setSelectedYear(int year) {
            mSelectedYear = year;
            mListView.setSelection(getPositionForYear(year) - 3);
            notifyDataSetChanged();
        }
    }
}
