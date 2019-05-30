package com.example.exercisetracker;

import android.graphics.Bitmap;

/**
 * This will be a simple class to hold database informaiton for a single workout session.
 * It will be used to store data to the database
 * it will be used to return data form the database
 */
public class WorkoutSession {
    public String name; //the name of the workout
    public String date; //the date of the workout
    public String description; //the description of the workout
    public long id; //workout id
    public String picture; //workout picture
    public String address; //workout address

    /**
     * regular constructor for creating a new database into the table
     * @param name
     * @param date
     * @param description
     * @param picture
     * @param address
     */
    public WorkoutSession(String name, String date, String description, String picture, String address) {
        this.id = -1; //new WorkoutSession will have an id of -1
        this.name = name;
        this.date = date;
        this.description = description;
        this.picture = picture;
        this.address = address;
    }


    /**
     * Constructor for grabbing an exising database from the table
     * @param id
     * @param name
     * @param date
     * @param description
     * @param picture
     * @param address
     */
    public WorkoutSession( long id,String name, String date, String description, String picture, String address) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.id = id;
        this.picture = picture;
        this.address = address;
    }
}



