package com.example.exercisetracker;

import android.graphics.Bitmap;

public class WorkoutSession {
    public String name;
    public String date;
    public String description;
    public long id;
    public String picture;
    public String address;

    public WorkoutSession(String name, String date, String description, String picture, String address) {
        this.id = -1;
        this.name = name;
        this.date = date;
        this.description = description;
        this.picture = picture;
        this.address = address;
    }

    public WorkoutSession( long id,String name, String date, String description, String picture, String address) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.id = id;
        this.picture = picture;
        this.address = address;
    }
}



