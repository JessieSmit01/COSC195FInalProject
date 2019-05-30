package com.example.exercisetracker;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FilePermission;
import java.util.ArrayList;
import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<WorkoutSession> {

    TextView tvDesc, tvName, tvLocation, tvDate;
    ImageView picture;
    List<WorkoutSession> obSessions;

    public WorkoutAdapter(Context context, List<WorkoutSession> objects) {
        super(context, R.layout.workout_view_layout, objects);
        this.obSessions = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final WorkoutSession session = this.getItem(position);
        View locationItemView = convertView;


        if(convertView == null) {
            LayoutInflater layoutInflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            locationItemView = layoutInflator.inflate(R.layout.workout_view_layout, parent, false);
        }

            tvDesc = locationItemView.findViewById(R.id.etDescription);
            tvDesc.setText(session.description);
            tvName = locationItemView.findViewById(R.id.txtName);
            tvName.setText(session.name);
            tvLocation = locationItemView.findViewById(R.id.txtLocation);
            tvLocation.setText(session.address);
            tvDate = locationItemView.findViewById(R.id.txtDate);
            tvDate.setText(session.date);
            picture = locationItemView.findViewById(R.id.picture);
            if(session.picture != null) {
                File obFile = new File(session.picture);
                Bitmap obBitmap = BitmapFactory.decodeFile(obFile.getAbsolutePath());
                picture.setImageBitmap(obBitmap);





        }
        return locationItemView;
    }


}
