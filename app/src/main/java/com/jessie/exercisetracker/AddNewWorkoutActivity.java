package com.jessie.exercisetracker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.MonthDay;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This class will control the backend processing for the "add workout" page.
 * A user is able to enter information into the layout and once a "save" button is tapped, it will use WorkoutDBHelper to save the information into a database
 */
public class AddNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    double latitude; //the current longitude of the user
    double longitude; //the current latitude of the user
    private String address; //the current address of the user
    private EditText name; //the name field in the layout
    private CalendarView date; //the calendar in the layout
    public ImageView imgView; //the imageview in the layout
    public EditText description; //the description field in the layout
    private final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/"; //this is the directory from which the application will save/delete pictures
    File newdir = new File(dir); //create a file from the directory path above
    WorkoutDPHelper db;
    String currentPhotoPath; //Temporary photo path for current photo taken
    File newFile;

    public String stringDate; //this will represent the selected date from the calendarview


    public static final File PICTURES_PATH = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "ExerciseTracker");



    LocationManager locationManager; //need to access location



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout);
        imgView = findViewById(R.id.imgView);
        newdir.mkdirs(); //make the directory
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); //get the lcoation services



        this.name = findViewById(R.id.etName);
        this.date = findViewById(R.id.txtDate);
        this.description = findViewById(R.id.description);

        date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //this will change stringDate to be the currently chosen date from the calendarView

            /**
             * When the date changes in the calendar view, set stringDate to the newly selected date
             * @param view
             * @param year
             * @param month
             * @param dayOfMonth
             */
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                stringDate =String.valueOf(MonthDay.of(month + 1, dayOfMonth).toString().substring(2) + "-" + year);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) //check if permission is granted for location services
                == PackageManager.PERMISSION_GRANTED)
        {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 5, this); //request location updates as per these settings
        }

        db = new WorkoutDPHelper(this); //instantiate db as a new WorkoutDBHelper


    }


    /**
     * This method will handle button clicks. It will check for the button id and perform actions based on button clicked
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnAddPicture:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) //Check for permission denied for camera, read and write from storage.
                        == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) //check if write storage permissions are granted, if not, ask the user to grant permission
                        == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) //check if read storage permissions are granted, if not, ask the user to grant permission
                        == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(this, "Camera, and Storage access must be enabled in System Settings to use this functionality", Toast.LENGTH_LONG).show(); //inform the user by toast to enable camera permission
                    return;
                }

                if(this.currentPhotoPath != null)
                {
                    File obFile = new File(this.currentPhotoPath);

                        obFile.delete();


                }

                capturePhoto();

                return;
            case R.id.btnSaveWorkout:
                this.address = getAddress(this, latitude, longitude); //get the address of the user

                saveToDB();

                this.currentPhotoPath = null;

                return;
        }
    }


    /**
     * This method is responsible for creating a file to save a picture within, it then creates a new camera intent to access the camera
     */
    private void capturePhoto() {

        String file = "shared/"+DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg"; //set the new image file path as a string


        File newfile = new File( file); //create a new file object with a path from the string above
        try {
            newfile.createNewFile();  //create the new file
        } catch (IOException e) {}

        Uri outputFileUri = FileProvider.getUriForFile(AddNewWorkoutActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());//get the uri from the file being created

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //set up the new intent to access the camera
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri); //pass in the uri for the picture to save

        startActivityForResult(cameraIntent, 1); //start the activity
    }




    /**
     * returns a file that is used to hold a picture.
     * @return
     */
    private File createImageFile() {
        try {
            // Create a new image file. appending the current time to the file name will make it unique
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_" + ".jpg";
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //get the path to the Environments pictures directory


            File obFile = new File(path + File.separator, "ExerciseTracker"); //This will be the complete path for the new picture
            if(!obFile.exists()) { //check if obfile exists. if it doesnt exist make the new directory
                obFile.mkdir();
            }

            File file = new File(obFile,  imageFileName); // add the image file into obFile's directory

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = file.getAbsolutePath();
            newFile = file;
            return file;
        }
        catch( Exception e)
        {

        }
        return null; //problems occured
    }


    /**
     * This will be called after the camera intent has been completed. The photo returned will be saved to the storage location
     * set the imageview within this layout to the picture taken
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(this.currentPhotoPath);
            File obFile = newFile;

            imgView.setImageURI(Uri.fromFile(obFile));

        }
    }


    /**
     * This method will be called to update the current latitude and longitude whenever a phones location is moved
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) { //When location is changes update the current latitude and longitude
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /**
     * This method will take in a context, longitude and latitude and return the location as a String represented by the address of the given latitude and longitude
     * @param context
     * @param latitude
     * @param lon
     * @return
     */
    public static String getAddress(Context context, double latitude, double lon) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault()); //need to use a geocoder
            List<Address> lstAddr = geocoder.getFromLocation(latitude, lon, 1); //Get the geolocator location from LAT and LON and add it to the list
            if (lstAddr != null && lstAddr.size() > 0) { //check if address is null
                String address = lstAddr.get(0).getAddressLine(0); //get the address string from the addresses list
                return address; //return String form of provided Latitude and Longitude
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Location Unavailable"; //return default string if location is unavailable
    }


    /**
     * This method will take in the information form the layout fields and create a new WorkoutSession object out of the data
     */
    public void saveToDB()
    {
        //create new workoutsession
        WorkoutSession session = new WorkoutSession(this.name.getText().toString(), this.stringDate, this.description.getText().toString(), this.currentPhotoPath, getAddress(this, latitude, longitude));
        if(session.name.equals("") || session.description.equals("")) //check to make sure name and description fields are filled out
        {
            Toast.makeText(this, "Name and Description cannot be left blank", Toast.LENGTH_SHORT).show(); //inform the user to fill out required fields
            return;
        }
        db.open();
        db.createWorkoutSession(session); //add the workoutSession to the database table


        Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show(); //inform the user that the workout has been saved
    }

}
