package com.gonzalez.laura.studentplannerapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gonzalez.laura.studentplannerapp.model.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert
    long insert(Course course);

    @Query("SELECT * FROM course_table")
    List<Course> getCourses();

    @Query("UPDATE course_table SET grade = :new_grade WHERE _id = :tid")
    int updateGrade(long tid, float new_grade);

    @Delete
    void deleteCourse(Course course);

}

