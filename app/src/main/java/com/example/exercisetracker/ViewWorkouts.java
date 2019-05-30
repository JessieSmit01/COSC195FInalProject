package com.example.exercisetracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class ViewWorkouts extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lv;
    ArrayList<WorkoutSession> sessionList;
    WorkoutAdapter adapter;
    WorkoutDPHelper db = new WorkoutDPHelper(this);
    public WorkoutSession obSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        sessionList = new ArrayList<>();
        createNotificationChannel();
        getWorkouts();
        if(this.sessionList.size() > 0)
        {
            adapter = new WorkoutAdapter(this, sessionList);

            lv = findViewById(R.id.lstWorkouts);


            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);
        }

        findViewById(R.id.btnDelete).setOnClickListener(this);





    }

    public void getWorkouts()
    {
        db.open();
        Cursor cursor =db.getAllWorkouts();
        if(cursor.isAfterLast())
        {
            return;
        }
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        this.obSession = sessionList.get(position);
        Toast.makeText(this, "Workout Selected", Toast.LENGTH_SHORT).show();

    }


        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.btnDelete) {

                if(this.obSession == null)
                {
                    Toast.makeText(this, "No Workout Selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.deleteCourse(obSession);

                if (obSession.picture != null) {
                    File file = new File(obSession.picture);
                    boolean deleted = file.delete();
                }

                sessionList.remove(obSession);
                Toast.makeText(this, "Workout Deleted", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "del")
                        .setSmallIcon(R.drawable.dumbell)
                        .setContentTitle("Workout successfully deleted")
                        .setContentText("You have successfully deleted your workout")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


                notificationManager.notify(1, builder.build());
            }
        }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "delete";
            String description = "deleted";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("del", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }




}
