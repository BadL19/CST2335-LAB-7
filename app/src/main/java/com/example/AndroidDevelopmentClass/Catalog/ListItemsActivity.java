package com.example.AndroidDevelopmentClass.Catalog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ListItemsActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "ListItemsActivity";

    /*
     todo: this is to take a picture and save it
    */
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;


    /*
    todo: for the photo application part
     */
    static final int REQUEST_IMAGE_CAPTURE = 1;

    /*
    todo: google: android developers: take a photo with the camera app
    The android way of delegating actions to other applications is to invoke an Intent that
    describes what you want done.
    This process involves 3 pieces:
        the Intent itself
        a call to start the external application
        and some code to handle the image data when the focus returns to your activity.

    */


    /*
    todo: for the photo application part
    */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    /*
    todo: this is to take a picture and save it
    this method creates a collision-resistant file name and a path name

     */
    private File createImageFile() throws IOException {
        //create an image file
        //the Date() object is today's date, is formatted to the string pattern of yyyyMMdd_HHmmss
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + "_";
        File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /*prefix*/
                ".jpg",        /*suffix*/
                storageDirectory  /*directory*/
        );
        //Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    /*
    This method is an updated method to dispatchTakePictureIntent in order to save the file.
     */
//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //Ensure that there is a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            //create the file where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "Error occurred while creating the picture file", Toast.LENGTH_LONG).show();
//            }
//
//            if (photoFile != null) {
//                Uri photoURL = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURL);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list_items);
        Log.i(ACTIVITY_NAME, "onCreate()");

    /*
    todo: for the photo application part
    */
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageBtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        Switch aSwitch = (Switch) findViewById(R.id.swtchbtn);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    giveToast(getString(R.string.onSwchMsg), 1);
                    Log.i("SWITCH BUTTON", "Enabled");
                } else {
                    giveToast(getString(R.string.offSwchMsg), 0);
                    Log.i("SWITCH BUTTON", "Disabled");
                }

            }
        });


        CheckBox checkBox = (CheckBox) findViewById(R.id.chkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListItemsActivity.this);
                alertDialogBuilder.setMessage(R.string.dialog_msessage)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(getString(R.string.responseKey), getString(R.string.responseValue));
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //todo: do nothing I guess!
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }//end onCheckedChanged handler
        });//end setOnCheckedChangeListener listener

    }//end onCreate method

    /**
     * @param s    {@link CharSequence} or {@link String} to be displayed in {@link Toast} msg.
     * @param code is the ON/OFF code, 1 for long toast duration, 0 for short duration.
     */
    private void giveToast(String s, int code) {
        if (code == 1) {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart()");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(ACTIVITY_NAME, "onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy()");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap) extras.get("data");
//            GridLayout gridLayout = (GridLayout) findViewById(R.id.content_list_items);


//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT
//            );

//            ImageView imageView = new ImageView(getApplicationContext());
//            imageView.setLayoutParams(params);
//            gridLayout.addView(imageView);
//            imageView.setImageBitmap(imagebitmap);

            ImageView imageView = (ImageView) findViewById(R.id.imageBtn);
            imageView.setImageBitmap(imagebitmap);
        }


    }
}
