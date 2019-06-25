package com.jessie.exercisetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class will be a helper class and provide communication between activities
 * and the database
 */
public class WorkoutDPHelper extends SQLiteOpenHelper {
    //Table variables
    public static final String DB_NAME = "workouts.db"; //database name
    public static final int DB_VERSION = 1; //database version
    public static final String TABLE_NAME = "workouts"; //table name
    public static final String ID = "_id"; //column 1
    public static final String NAME = "name"; //column2
    public static final String DATE = "date"; //column 3
    public static final String DESCRIP = "description";//column 4
    public static final String LOCATION = "Address";//column 5
    public static final String PICTURE = "picture";//column6

    public SQLiteDatabase sqlDB; //reference to SQLite database


    /**
     * WorkoutDBHelper constructor
     * @param context
     */
    public WorkoutDPHelper(Context context)
    {
        // context is required ot know where to create the db file
        super(context, DB_NAME, null, DB_VERSION);

    }

    /**
     * This checks for a change in the db version. If db version or db doesnt exist, create the Workouts table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create an sql command which creates the table
        String sCreate = "CREATE TABLE " +
                TABLE_NAME + "(" +
                ID + " integer primary key autoincrement, " +
                NAME + " text, " +
                DATE + " text, " +
                DESCRIP + " text, " +
                LOCATION + " text, " +
                PICTURE + " text);";
        db.execSQL(sCreate); //execute the command to create the table

    }

    /**
     * This method will be called when a database version has been upgraded
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);//delete the table
        onCreate(db); //call onCreate()
    }

    /**
     * This method will open the database so that we can read and write from it
     * @throws SQLException
     */
    public void open() throws SQLException
    {
        sqlDB = this.getWritableDatabase();
    }

    /**
     * This method closes the database so that no errors occur inside the database
     */
    public void close()
    {
        sqlDB.close();
    }


    /**
     * This method will take in a new WorkoutSession and add it to the database
     * @param
     * @return
     */
    public long createWorkoutSession(WorkoutSession session)
    {

        // a container to store each column and value
        ContentValues cvs = new ContentValues();

        // add an item for each column and value
        cvs.put(NAME, session.name);
        cvs.put(DATE, session.date);
        cvs.put(DESCRIP, session.description);
        cvs.put(PICTURE, session.picture);
        cvs.put(LOCATION, session.address);
        cvs.put(LOCATION, session.address);



        // execute insert, which returns the auto increment value
        long autoid = sqlDB.insert(TABLE_NAME, null, cvs);

        // update the id of the WorkoutSession with the new auto id
        session.id = autoid;
        return autoid;
    }


    /**
     * This method will grab all wokrouts from the Workouts table and return them within a Cursor
     * @return
     */
    public Cursor getAllWorkouts()
    {
        // list of columns to select and return
        String[] sFields = new String [] {ID, NAME,DATE, DESCRIP, PICTURE, LOCATION};
        return sqlDB.query(TABLE_NAME, sFields, null, null, null, null, null);
    }

    /**
     * This method takes in a Workout and deletes the Workouts from the table
     * @param
     * @return
     */
    public boolean deleteCourse(WorkoutSession session)
    {
        return sqlDB.delete(TABLE_NAME, ID + " = " +  "'" + session.id + "'", null) > 0;

    }





}
