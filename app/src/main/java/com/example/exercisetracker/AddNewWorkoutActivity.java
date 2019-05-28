package com.example.exercisetracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView imgView;
    public Uri file;
    private final String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/Folder/";
    File newdir = new File(dir);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout);
        imgView = findViewById(R.id.imgView);
        newdir.mkdirs();


    }

    private static final int REQUEST_CAPTURE_IMAGE = 100; //represent the request code for img capture

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnAddPicture:
                capturarPhoto();

                return;
        }
    }


    /**
     * Capture photo
     */
    private void capturarPhoto() {
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


}
