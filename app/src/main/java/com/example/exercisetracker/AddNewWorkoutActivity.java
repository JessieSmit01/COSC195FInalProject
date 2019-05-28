package com.example.exercisetracker;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
    double latitude;
    double longitude;
    private String address;
    public ImageView imgView;
    private final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
    File newdir = new File(dir);



    LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout);
        imgView = findViewById(R.id.imgView);
        newdir.mkdirs();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 5, this);
        }


    }






    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnAddPicture:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(this, "Camera access must be enabled in System Settings", Toast.LENGTH_LONG).show();
                    return;
                }
                capturePhoto();

                return;
            case R.id.btnSaveWorkout:
                this.address = getAddress(this, latitude, longitude);
                Toast.makeText(this, address, Toast.LENGTH_LONG).show();
                return;
        }
    }


    /**
     * Capture photo
     */
    private void capturePhoto() {
        String file = "shared/"+DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString()+".jpg";


        File newfile = new File( file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {}

        Uri outputFileUri = FileProvider.getUriForFile(AddNewWorkoutActivity.this, BuildConfig.APPLICATION_ID + ".provider", createImageFile());

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, 1);
    }


    String currentPhotoPath; //Temporary photo path for current photo taken

    /**
     * returns a temporary file that is used to hold a picture.
     * @return
     */
    private File createImageFile() {
        try {
            // Create an image file name. Make it unique by setting it a current timestamp
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();
            return image;
        }
        catch( Exception e)
        {

        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory. decodeFile(this.currentPhotoPath);
            imgView.setImageBitmap(bitmap);
        }
    }


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
     * This method will take in a context, longgitude and latitude and return the location as a String represented by the address of the given latitude and longitude
     * @param context
     * @param LAT
     * @param LON
     * @return
     */
    public static String getAddress(Context context, double LAT, double LON) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LAT, LON, 1); //Get the geolocator location form provided latitude and longitude and add it to the List of Address
            if (addresses != null && addresses.size() > 0) { //check if address is null



                String address = addresses.get(0).getAddressLine(0);

                return address; //return String form of provided Latitude and Longitude

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
