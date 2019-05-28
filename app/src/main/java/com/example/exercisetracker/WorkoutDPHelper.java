package com.example.exercisetracker;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDPHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "workouts.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "workouts";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String LOCATION = "Address";
    public static final String PICTURE = "picture";

    public SQLiteDatabase sqlDB;


    public WorkoutDPHelper(Context context)
    {
        // context is required ot know where to create the db file
        super(context, DB_NAME, null, DB_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sCreate = "CREATE TABLE " +
                TABLE_NAME + "(" +
                ID + " integer primary key autoincrement, " +
                NAME + " text not null, " +
                DATE + " text not null, " +
                LOCATION + " text not null, " +
                PICTURE + " BLOB);";
        db.execSQL(sCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
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
}
