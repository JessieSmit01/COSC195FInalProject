package com.example.exercisetracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class ViewWorkouts extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    ListView lv;
    ArrayList<WorkoutSession> sessionList;
    WorkoutAdapter adapter;
    WorkoutDPHelper db = new WorkoutDPHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        sessionList = new ArrayList<>();

        getWorkouts();
        adapter = new WorkoutAdapter(this, sessionList);

        lv = findViewById(R.id.list123);
        lv.setOnItemClickListener(this);

        lv.setAdapter(adapter);




    }

    public void getWorkouts()
    {
        db.open();
        Cursor cursor =db.getAllWorkouts();
        cursor.moveToFirst();
        WorkoutSession obSession = new WorkoutSession(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        sessionList.add(obSession);
        while(cursor.moveToNext())
        {
            obSession = new WorkoutSession(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            sessionList.add(obSession);
        }
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int s = 1 + 1;

    }
}
