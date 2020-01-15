package com.gonzalez.laura.studentplannerapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gonzalez.laura.studentplannerapp.model.Course;

public class CourseActivity extends MainActivity {

    EditText edtCourse;
    EditText edtRoom;
    EditText edtDay;
    EditText edtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        edtCourse = findViewById(R.id.edt_course);
        edtRoom = findViewById(R.id.edt_room);
        edtDay = findViewById(R.id.edt_day);
        edtTime = findViewById(R.id.edt_time);

        findViewById(R.id.btn_add_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCourse();
            }
        });

    }

    private void updateCourse() {
        String courseName = edtCourse.getText().toString();

        if(courseName.trim().length() == 0){
            edtCourse.setError(getString(R.string.error_course_name_empty));
        }
        else{
            String room = edtRoom.getText().toString();
            String day = edtDay.getText().toString();
            String time = edtTime.getText().toString();

            Course course = new Course(courseName, room, day, time, 0);

            Intent intent = new Intent();

            intent.putExtra("course", course);

            setResult(RESULT_OK, intent);

            finish();
        }


    }
}

