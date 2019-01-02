package com.winnie.views.timepiker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * @author winnie
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void pickTime(View view) {
        startActivity(new Intent(this, TimePickerActivity.class));
    }

    public void pickSystemDate(View view) {
        startActivity(new Intent(this, SystemDatePickerActivity.class));
    }

    public void pickCalendarDate(View view) {
        startActivity(new Intent(this, CalendarDatePickerActivity.class));
    }

    public void pickCustomDate(View view) {
        startActivity(new Intent(this, CustomDatePikerActivity.class));
    }

    public void pickDateDialog(View view) {
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view1, year, monthOfYear, dayOfMonth)
                        -> Toast.makeText(MainActivity.this,
                        year + "year " + (monthOfYear + 1) + "month "
                                + dayOfMonth + "day", Toast.LENGTH_SHORT).show(),
                2013, 7, 20);
        datePicker.show();
    }

    public void pickTimeDialog(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this,
                (view1, hourOfDay, minute)
                        -> Toast.makeText(MainActivity.this, hourOfDay
                        + "hour " + minute+ "minute ", Toast.LENGTH_SHORT).show(),
                12, 20, true);
        dialog.show();
    }
}
