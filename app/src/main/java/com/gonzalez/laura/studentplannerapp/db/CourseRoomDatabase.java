package com.gonzalez.laura.studentplannerapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.gonzalez.laura.studentplannerapp.model.Course;

@Database(entities = {Course.class}, version = 1)
public abstract class CourseRoomDatabase extends RoomDatabase {
    private static CourseRoomDatabase INSTANCE;
    public abstract CourseDao courseDao();

    public static CourseRoomDatabase getDatabase(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    CourseRoomDatabase.class,
                    "course_db").build();

        }
        return INSTANCE;
    }

}
