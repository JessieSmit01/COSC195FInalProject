package com.jessie.exercisetracker;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

/**
 * This class will handle the back-end processing for the viewWorkouts layout
 */
public class ViewWorkouts extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv; //the listview within the layout
    ArrayList<WorkoutSession> sessionList; //arraylist to hold workoutSession
    WorkoutAdapter adapter; //adapter for communication between listview and the sessionList
    WorkoutDPHelper db = new WorkoutDPHelper(this);
    public WorkoutSession obSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        sessionList = new ArrayList<>();

        getWorkouts();
        if(this.sessionList.size() > 0) //check if there are any sessions in the database. if there are, instantiate a workoutadapter.
        {
            adapter = new WorkoutAdapter(this, sessionList);

            lv = findViewById(R.id.lstWorkouts);


            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this); //
        }

        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("You have not saved any workouts");
            builder.setMessage("Please save a new workout so that you can view it here.");
            builder.setNeutralButton("Okay",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goToHomeScreen();

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }


    }

    public void goToHomeScreen()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    /**
     * This method will take the data from the db table and store them into sessionList as WorkoutSessions
     */
    public void getWorkouts()
    {
        db.open();
        Cursor cursor =db.getAllWorkouts(); //get the data form the database
        if(cursor.isAfterLast()) //check for empty table then just return
        {
            return;
        }
        cursor.moveToFirst(); //move to first position
        WorkoutSession obSession = new WorkoutSession(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)); //create workout session from first row in db
        sessionList.add(obSession); //add to sessionList
        while(cursor.moveToNext()) //move to next row each loop
        {
            obSession = new WorkoutSession(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)); //create anew WorkoutSession from the new row in the table
            sessionList.add(obSession); //add to sessionList
        }
    }


    /**
     * Called when a listview item is clicked on
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

       this.obSession = sessionList.get(position); //get the session from the listview position in the array
       // Toast.makeText(this, "Workout Selected", Toast.LENGTH_SHORT).show(); //inform the user that the workout has been clicked

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Workout Selected");
        builder.setMessage("Would you like to delete this workout?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        confirmDelete();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


        public void confirmDelete()
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Are you sure you want to delete this workout?");
            builder.setMessage("All workout data and picture will be permanently deleted");
            builder.setPositiveButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteWorkout();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }


        public void deleteWorkout()
        {
            db.deleteCourse(obSession); //delete the course from database

            if (obSession.picture != null) { //delete the picture from the phone's internal storage
                File file = new File(obSession.picture);
                boolean deleted = file.delete(); //delete the file
            }

            sessionList.remove(obSession); //remove the selected item from the ArrayList of workout sessions
            Toast.makeText(this, "Workout Deleted", Toast.LENGTH_SHORT).show(); //inform the user that the workout has been deleted
            adapter.notifyDataSetChanged(); //refresh the listview
            obSession = null; //reset obSession back to null
        }







}
