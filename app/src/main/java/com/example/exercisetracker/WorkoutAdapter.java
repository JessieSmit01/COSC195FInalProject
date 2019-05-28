package com.example.exercisetracker;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<WorkoutSession> {

    public WorkoutAdapter(Context context, List<WorkoutSession> objects) {
        super(context, R.layout.workout_view_layout, objects);
    }
}
