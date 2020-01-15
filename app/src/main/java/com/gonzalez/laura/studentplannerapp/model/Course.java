package com.gonzalez.laura.studentplannerapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity (tableName = "course_table")
public class Course implements Parcelable {

    private String name;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "_id")
    private long dbId;

    private String room;
    private String date;
    private String time;
    private float grade;


    public Course(String name, String room, String date, String time, float grade, long dbId) {
        this.name = name;
        this.room = room;
        this.date = date;
        this.time = time;
        this.grade = grade;
        this.dbId = dbId;
    }

    @Ignore
    public Course(String name, String room, String date, String time, float grade) {
        this.name = name;
        this.room = room;
        this.date = date;
        this.time = time;
        this.grade = grade;
    }

    protected Course(Parcel in) {
        dbId = in.readLong();
        name = in.readString();
        room = in.readString();
        date = in.readString();
        time = in.readString();
        grade = in.readInt();
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public float getGrade() {
        return grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDbId() {
        return dbId;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(name);
        dest.writeString(room);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeFloat(grade);
    }

    @Override
    public String toString(){
        return name;

    }

}

