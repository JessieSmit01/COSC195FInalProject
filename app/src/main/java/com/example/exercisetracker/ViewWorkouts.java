package com.example.exercisetracker;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewWorkouts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
        lv = findViewById(R.id.ListView);
        lv.setAdapter(adapter);
        lv.setOnItemSelectedListener(this);
    }

    public void getWorkouts()
    {
        db.open();
        Cursor cursor =db.getAllWorkouts();
        cursor.moveToFirst();
        for(int j = 0; j < cursor.getCount(); j++)
        {
            WorkoutSession obSession = new WorkoutSession(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), db.getImage(cursor.getBlob(4)), cursor.getString(5));
            sessionList.add(obSession);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
