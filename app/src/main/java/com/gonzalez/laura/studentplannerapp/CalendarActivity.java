package com.gonzalez.laura.studentplannerapp;

import android.os.Bundle;
import android.widget.CalendarView;

public class CalendarActivity extends MainActivity {

    private CalendarView mCalendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);


    }

}
