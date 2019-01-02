package com.winnie.views.timepiker.decorator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.winnie.views.timepiker.R;

/**
 * @author : winnie
 * @date : 2019/1/2
 * @desc
 */
public class SelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public SelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.sl_date_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}
