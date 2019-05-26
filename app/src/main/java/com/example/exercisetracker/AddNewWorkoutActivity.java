package com.example.exercisetracker;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewWorkoutActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView imgView;
    public Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_workout);
        imgView = findViewById(R.id.imgView);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btnAddPicture:
               takePicture(v);

                return;
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File("file://" +mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }



    public void takePicture(View view) {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        intent.setDataAndType(file, "jpg");

        startActivityForResult(intent, 100);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.imgView.setImageURI(data.getData());
    }

    private void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }

}
