package com.gonzalez.laura.studentplannerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gonzalez.laura.studentplannerapp.db.CourseDao;
import com.gonzalez.laura.studentplannerapp.db.CourseRoomDatabase;
import com.gonzalez.laura.studentplannerapp.model.Course;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    TextView txtDate;
    TextView courseTitle;
    Button calendarButton;
    private RecyclerView mrecyclerView;
    private static final int ADD_COURSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDate = findViewById(R.id.txt_date);
        courseTitle = findViewById(R.id.course_title);
        mrecyclerView = findViewById(R.id.recycler_view);
        calendarButton = findViewById(R.id.btn_calendar);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCalendar();
            }
        });

        mrecyclerView.addItemDecoration(new DividerItemDecoration(mrecyclerView.getContext(),
                DividerItemDecoration.VERTICAL));

        RecyclerView.LayoutManager manager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mrecyclerView.setLayoutManager(manager);

        loadCourses();
        getCurrentDate();

    }

    private void loadCourses() {
        RetrieveTask myTask = new RetrieveTask();
        myTask.execute();
    }

    private void addCourse() {
        Intent intent = new Intent(getApplicationContext(), CourseActivity.class);
        startActivityForResult(intent, ADD_COURSE);
    }

    private void loadCalendar(){
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == ADD_COURSE && resultCode == RESULT_OK){
            Course course = data.getParcelableExtra("course");

            Log.e("Name: ", course.getName());

            InsertTask myTask = new InsertTask();
            myTask.execute(course);
        }
    }

    private class InsertTask extends AsyncTask<Course, Void, Long> {

        @Override
        protected Long doInBackground(Course... courses) {
            CourseRoomDatabase db = CourseRoomDatabase.getDatabase(getApplicationContext());

            CourseDao dao = db.courseDao();

            long id = dao.insert(courses[0]);

            return id;
        }

        @Override
        protected void onPostExecute(Long aLong) {

            loadCourses();
        }
    }


    private class RetrieveTask extends AsyncTask<Void, Void, List<Course>> {

        @Override
        protected List<Course> doInBackground(Void... voids) {
            CourseRoomDatabase db = CourseRoomDatabase.getDatabase(getApplicationContext());
            CourseDao dao = db.courseDao();

            List<Course> courses = dao.getCourses();
            return courses;
        }

        @Override
        protected void onPostExecute (List<Course> courses){
            CourseAdapter adapter = new CourseAdapter(getApplicationContext(), courses);
            mrecyclerView.setAdapter(adapter);

            if (!courses.isEmpty()) {
                courseTitle.setText(R.string.course_title);

                ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();

                        Log.e("Adapter position", Integer.toString(position));
                        deleteCourse(position);

                    }
                });

                helper.attachToRecyclerView(mrecyclerView);

            }
            else {
                courseTitle.setText(R.string.no_courses);
            }


        }

    }

    private class deleteTask extends AsyncTask<Integer, Void, List<Course>> {
        @Override
        protected List<Course> doInBackground(Integer... course) {
            CourseRoomDatabase db = CourseRoomDatabase.getDatabase(getApplicationContext());
            CourseDao dao = db.courseDao();
            List<Course> courses = dao.getCourses();

            CourseAdapter adapter = new CourseAdapter(getApplicationContext(), courses);
            Course myCourse = adapter.getCourseAtPosition(course[0]);

            dao.deleteCourse(myCourse);
            List<Course> updatedCourses = dao.getCourses();

            return updatedCourses;
        }

        @Override
        protected void onPostExecute(List<Course> courses) {
            CourseAdapter adapter = new CourseAdapter(getApplicationContext(), courses);
            mrecyclerView.setAdapter(adapter);
            loadCourses();
        }
    }

    @Override
    protected void onRestart() {
        loadCourses();
        super.onRestart();
    }

    public void deleteCourse(int position) {
        deleteTask delete = new deleteTask();
        delete.execute(position);
    }

    private void getCurrentDate(){
        String currentDateString = DateFormat.getDateInstance().format(new Date());
        txtDate.setText(currentDateString);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.add_course_activity){
            addCourse();
            return true;
        }
        else if (item.getItemId() == R.id.calculator_activity) {

            Intent intent = new Intent(getApplicationContext(), CalculatorActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.about_activity){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == R.id.main_activity){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

}

