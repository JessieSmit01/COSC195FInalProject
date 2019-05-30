package com.example.exercisetracker;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.List;

/**
 * This class extends ArrayAdapter. It will be used for communication between the viewWorkouts activity an arraylist of WorkoutSessions
 * grabben from the database
 */
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
        final WorkoutSession session = this.getItem(position); //get the current item and instantiate it as a WorkoutSession
        View locationItemView = convertView;


        //check for null
        if(convertView == null) {
            LayoutInflater layoutInflator = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            locationItemView = layoutInflator.inflate(R.layout.workout_view_layout, parent, false);
        }
            //set each view in the layout to have the proper data corresponding to the WorkoutSession in the array
            tvDesc = locationItemView.findViewById(R.id.etDescription);
            tvDesc.setText(session.description);
            tvName = locationItemView.findViewById(R.id.txtName);
            tvName.setText(session.name);
            tvLocation = locationItemView.findViewById(R.id.txtLocation);
            tvLocation.setText(session.address);
            tvDate = locationItemView.findViewById(R.id.txtDate);
            tvDate.setText(session.date);
            picture = locationItemView.findViewById(R.id.picture);
            if(session.picture != null) { //get the image string saved to the database and set the image view source to the saved path
                File obFile = new File(session.picture);
                Bitmap obBitmap = BitmapFactory.decodeFile(obFile.getAbsolutePath()); //decode the file into a bitmap
                picture.setImageBitmap(obBitmap); //set the Imageview to the bitmap





        }
        return locationItemView;
    }


}
