package com.gonzalez.laura.studentplannerapp;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gonzalez.laura.studentplannerapp.model.Course;

import java.util.List;

public class CourseAdapter extends
        RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    public Context context;

    // declaring fields and initializing

    private List<Course> mCourses;

    public CourseAdapter(Context Context, List<Course> Courses){
        context = Context;
        mCourses = Courses;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTxtName, mTxtRoom, mTxtDay, mTxtTime, mTxtGrade;
        // private ImageButton btnDelete;

        public MyViewHolder(@NonNull LinearLayout layout){
            super(layout);
            mTxtName = layout.findViewById(R.id.txt_course);
            mTxtRoom = layout.findViewById(R.id.txt_room);
            mTxtDay = layout.findViewById(R.id.txt_day);
            mTxtTime = layout.findViewById(R.id.txt_time);
            mTxtGrade = layout.findViewById(R.id.txt_grade);
        }

    }

    // creates the view holder for the recycler display and returns it

    @NonNull
    @Override
    public CourseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_courses, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(layout);

        return viewHolder;
    }

    // displays the list of scores in recycler view

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.MyViewHolder myViewHolder, int i){
        Course course = mCourses.get(i);

        myViewHolder.mTxtName.setText(course.getName());
        myViewHolder.mTxtRoom.setText("Room #" + course.getRoom());
        myViewHolder.mTxtDay.setText("Date: " + course.getDate());
        myViewHolder.mTxtTime.setText("Time: " + course.getTime());
        myViewHolder.mTxtGrade.setText(Float.toString(course.getGrade()) + "%");
    }

    @Override
    public int getItemCount(){
        return mCourses.size();
    }

    public Course getCourseAtPosition (int position) {
        return mCourses.get(position);
    }


}


