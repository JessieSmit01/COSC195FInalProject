package com.jessie.exercisetracker;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 * This class will be used to control the back-and processing of the main activity layout
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int CAM_REQ_CODE = 1;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-6532407563341229~5254988732");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) //check if camera permissions are granted, if not, ask the user to grant permission
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, CAM_REQ_CODE);
        }

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int errorCode) {
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        });


        //Create a notification channel
        createNotificationChannel();
        //create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "del")
                .setSmallIcon(R.drawable.dumbell) //set notification picture
                .setContentTitle("Did you workout today?")
                .setContentText("Don't forget to add a new workout session!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }


    /**
     * Handle button clicks
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnAdd:
                Intent intent = new Intent(this, AddNewWorkoutActivity.class); //change to the add workout screen
                startActivity(intent);
                break;
            case R.id.btnView:
                Intent i = new Intent(this, ViewWorkouts.class); //change to the view workout screen
                startActivity(i);
                break;
        }
    }

    /**
     * This method will be used to create a notification channel
     */
    private void createNotificationChannel() {
        //check if this phone is running build version code O. This version and above requires a
        //notification channel for notifications
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "delete";
            String description = "deleted";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("del", name, importance);
            channel.setDescription(description);
            // Create the channel within the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
