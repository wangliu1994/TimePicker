package com.winnie.views.timepiker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
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

    public void pickDate(View view) {
        startActivity(new Intent(this, DatePickerActivity.class));
    }

    public void pickDate1(View view) {
        startActivity(new Intent(this, DatePickerActivity1.class));
    }

    public void pickDateDialog(View view) {
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Toast.makeText(MainActivity.this, year + "year " + (monthOfYear + 1) + "month " + dayOfMonth + "day", Toast.LENGTH_SHORT).show();
            }
        }, 2013, 7, 20);
        datePicker.show();
    }

    public void pickTimeDialog(View view) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(MainActivity.this, hourOfDay + "hour " + minute+ "minute ", Toast.LENGTH_SHORT).show();
            }
        }, 12, 20, true);
        dialog.show();
    }
}
