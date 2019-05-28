package com.example.exercisetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class WorkoutDPHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "workouts.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "workouts";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String DESCRIP = "description";
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
                DESCRIP + " text not null, " +
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
        cvs.put(PICTURE, getBytes(session.picture));
        cvs.put(LOCATION, session.address);



        // execute insert, which returns the auto increment value
        long autoid = sqlDB.insert(TABLE_NAME, null, cvs);

        // update the id of the purcahse with the new auto id
        session.id = autoid;
        return autoid;
    }


    public Cursor getAllWorkouts()
    {
        // you may want to return a List of FuelPurchase items instead
        // list of columns to select and return
        String[] sFields = new String [] {ID, NAME,DATE, DESCRIP, PICTURE, LOCATION};
        return sqlDB.query(TABLE_NAME, sFields, null, null, null, null, null);
    }



    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

}
