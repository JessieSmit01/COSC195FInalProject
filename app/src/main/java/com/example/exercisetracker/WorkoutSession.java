package com.example.exercisetracker;

import android.graphics.Bitmap;

public class WorkoutSession {
    public String name;
    public long date;
    public String description;
    public int id;
    public Bitmap picture;
    public double longitude;
    public double latitude;

    public WorkoutSession(String name, long date, String description, Bitmap picture, double latitude, double longitude) {
        this.id = -1;
        this.name = name;
        this.date = date;
        this.description = description;
        this.picture = picture;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public WorkoutSession( int id,String name, long date, String description, Bitmap picture, double latitude, double longitude) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.id = id;
        this.picture = picture;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}



