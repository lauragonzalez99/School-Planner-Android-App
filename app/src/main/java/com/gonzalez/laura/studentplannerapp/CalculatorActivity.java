package com.gonzalez.laura.studentplannerapp;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gonzalez.laura.studentplannerapp.db.CourseDao;
import com.gonzalez.laura.studentplannerapp.db.CourseRoomDatabase;
import com.gonzalez.laura.studentplannerapp.model.Course;

import java.util.List;

public class CalculatorActivity extends MainActivity {

    private int gradePosition = 2;
    private ImageButton btnAddGrade;
    private Button btnCalculate;
    private TableLayout tableLayout;
    private TextView txtAverage;
    Spinner spnCourses;
    public Course selectedCourse;
    public float finalGrade = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        btnAddGrade = findViewById(R.id.add_grade_button);
        btnCalculate = findViewById(R.id.calculate_button);
        tableLayout = findViewById(R.id.grades);
        txtAverage = findViewById(R.id.txt_average);
        spnCourses = findViewById(R.id.spn_courses);

        LoadTask myTask = new LoadTask();
        myTask.execute();

        btnAddGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revealRow(v);
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Close numpad keyboard input when calculate button is pressed.
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                //initialize array that will hold possible rows that may need to be ignored
                int[] ignore = new int[10];
                //bool error check that will check if any errors appeared, initialized as no error
                boolean errorCheck = false;
                //initialize counter that will check if at least 2 of the user entries rows are valid
                int twoTrue = 0;
                //loops through the edit texts visible to the user
                for( int i = 0; i < gradePosition; i++){

                    TableRow row1 = (TableRow)tableLayout.getChildAt(i);

                    EditText edt_grade = (EditText)row1.getChildAt(0);
                    String grade = edt_grade.getText().toString();
                    EditText edt_weight = (EditText)row1.getChildAt(1);
                    String weight = edt_weight.getText().toString();

                    edt_grade.setError(null);
                    edt_weight.setError(null);

                    //if BOTH edit text grade AND weight are empty, then program
                    //will ignore these edit texts when calculating grades
                    if(grade.trim().length() == 0 && weight.trim().length() == 0 ){
                        ignore[i] = 1;
                    }
                    //if only the grade is not filled in, puts error on edit grade edit text
                    else if(grade.trim().equals("") || grade.trim().length() == 0){
                        edt_grade.setError(getString(R.string.grade_error));
                        errorCheck = true;
                    }
                    //if only the weight is not filled in, puts error on edit text weight
                    else if(weight.trim().equals("") || weight.trim().length() == 0 ){
                        edt_weight.setError(getString(R.string.weight_error));
                        errorCheck = true;
                    }
                    //elseif both grade and weight edit text values are valid, then adds 1 to two true
                    //later checks before calling calculate grades
                    //that at least 2 of the rows are valid (two true >= 2)
                    else{
                        try{
                            float grade_ = Float.parseFloat(grade);
                            Float.parseFloat(weight);

                            if(grade_ <= 100){
                                twoTrue++;
                            }
                            else{
                                edt_grade.setError(getString(R.string.error_invalid_grade));
                                errorCheck = true;
                            }

                        }
                        catch(Exception e){ }
                    }
                }
                //checks that there are no errors and that there are at least 2 valid user grade entries
                //if so, calls calculates grades
                if(!errorCheck && twoTrue >= 2){
                    calculateGrades(ignore);
                }
                //else if
                else if(twoTrue < 2){
                    Toast.makeText(getApplicationContext(), R.string.toast_2_grades,
                            Toast.LENGTH_LONG).show();

                    for( int i = 0; i < gradePosition; i++){

                        TableRow row1 = (TableRow)tableLayout.getChildAt(i);
                        EditText edt_grade = (EditText)row1.getChildAt(0);
                        String grade = edt_grade.getText().toString();
                        EditText edt_weight = (EditText)row1.getChildAt(1);
                        String weight = edt_weight.getText().toString();

                        //if the grade is not filled in, puts error on edit grade edit text
                        if(grade.trim().equals("") || grade.trim().length() == 0){
                            edt_grade.setError(getString(R.string.grade_error));
                        }
                        //if the weight is not filled in, puts error on edit text weight
                        if(weight.trim().equals("") || weight.trim().length() == 0 ){
                            edt_weight.setError(getString(R.string.weight_error));
                        }
                    }
                }
            }
        });

    }

    private void calculateGrades(int[] ignore) {

        float[][] CalculateGrades = new float[2][gradePosition];

        for( int i = 0; i < gradePosition; i++){
            if(ignore[i] == 1){
                continue;
            }
            TableRow row1 = (TableRow)tableLayout.getChildAt(i);
            EditText edt_grade = (EditText)row1.getChildAt(0);
            String grade = edt_grade.getText().toString();

            EditText edt_weight = (EditText)row1.getChildAt(1);
            String weight = edt_weight.getText().toString();

            CalculateGrades[0][i] = Float.parseFloat(grade);
            CalculateGrades[1][i] = Float.parseFloat(weight);
        }

        finalGrade = 0;
        float avgGrade;
        int checkWeight = 0;
        for( int i = 0; i < gradePosition; i++){
            if(ignore[i] == 1){
                continue;
            }
            avgGrade = CalculateGrades[0][i] * (CalculateGrades[1][i] / 100);
            checkWeight += CalculateGrades[1][i];
            finalGrade += avgGrade;
        }
        if(checkWeight > 100){
            txtAverage.setText(R.string.over_weight);
        }
        else{
            txtAverage.setText(getResources().getString(R.string.overall_average, finalGrade));
        }


        if (selectedCourse == null ) {
            Toast.makeText(getApplicationContext(), R.string.toast_no_course, Toast.LENGTH_LONG).show();
        }
        else if(checkWeight <=100){
            //update course grade in SQL
            long selectedId = selectedCourse.getDbId();

            UpdateTask updateTask = new UpdateTask();
            updateTask.execute(selectedId);
        }
    }

    public void revealRow(View view1) {
        View view = (View) findViewById(R.id.grades);
        if (gradePosition >= 10) {
            Toast.makeText(getApplicationContext(), R.string.toast_no_more_grades, Toast.LENGTH_LONG).show();
        }
        else if (gradePosition < 10) {
            gradePosition += 1;
            if (gradePosition == 3) {
                TableRow row = (TableRow) view.findViewById(R.id.grade3);
                row.setVisibility(View.VISIBLE);
            }
            else if (gradePosition == 4) {
                TableRow row = (TableRow) view.findViewById(R.id.grade4);
                row.setVisibility(View.VISIBLE);
            }
            else if (gradePosition == 5) {
                TableRow row = (TableRow) view.findViewById(R.id.grade5);
                row.setVisibility(View.VISIBLE);
            }
            else if (gradePosition == 6) {
                TableRow row = (TableRow) view.findViewById(R.id.grade6);
                row.setVisibility(View.VISIBLE);
            }
            else if (gradePosition == 7) {
                TableRow row = (TableRow) view.findViewById(R.id.grade7);
                row.setVisibility(View.VISIBLE);
            }
            else if (gradePosition == 8) {
                TableRow row = (TableRow) view.findViewById(R.id.grade8);
                row.setVisibility(View.VISIBLE);
            }

            else if (gradePosition == 9) {
                TableRow row = (TableRow) view.findViewById(R.id.grade9);
                row.setVisibility(View.VISIBLE);
            }

            else if (gradePosition == 10) {
                TableRow row = (TableRow) view.findViewById(R.id.grade10);
                row.setVisibility(View.VISIBLE);
            }
        }
    }

    private class LoadTask extends AsyncTask<Void, Void, Course[]> {
        @Override
        protected Course[] doInBackground(Void...voids) {
            CourseRoomDatabase db = CourseRoomDatabase.getDatabase(getApplicationContext());
            CourseDao dao = db.courseDao();
            List<Course> courses = dao.getCourses();

            Course[] courseArr = new Course[courses.size()];
            courseArr = courses.toArray(courseArr);

            return courseArr;
        }

        @Override
        protected void onPostExecute(Course[] courseArr) {
            ArrayAdapter<Course> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, courseArr);
            adapter.setDropDownViewResource(R.layout.spinner_item);

            SpinnerListener listener = new SpinnerListener();
            spnCourses.setOnItemSelectedListener(listener);
            spnCourses.setAdapter(adapter);
        }
    }

    private class UpdateTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long...params) {
            CourseRoomDatabase db = CourseRoomDatabase.getDatabase(getApplicationContext());
            CourseDao dao = db.courseDao();
            dao.updateGrade(params[0], finalGrade);
            return null;
        }
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Course course = (Course) adapterView.getAdapter().getItem(position);
            selectedCourse = course;

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

            Toast.makeText(getApplicationContext(), "No course selected!", Toast.LENGTH_LONG).show();
        }
    }
}

