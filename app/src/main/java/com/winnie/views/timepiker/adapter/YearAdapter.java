package com.winnie.views.timepiker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winnie.views.timepiker.R;

/**
 * @author : winnie
 * @date : 2019/1/2
 * @desc
 */
public class YearAdapter extends BaseAdapter {
    private int mMinYear;
    private int mCount;
    private int mSelectedYear;
    private Context mContext;

    public YearAdapter(Context context, int minYear, int maxYear) {
        mContext = context;
        mMinYear = minYear;
        mCount = maxYear - minYear + 1;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_year_item, parent, false);
        }
        int year = getYearForPosition(position);
        TextView textView = convertView.findViewById(R.id.year_text);
        textView.setText(String.valueOf(year));
        if (mSelectedYear == year) {
            textView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
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

    public int setSelectedYear(int year) {
        mSelectedYear = year;
        notifyDataSetChanged();
        return getPositionForYear(year);
    }
}
