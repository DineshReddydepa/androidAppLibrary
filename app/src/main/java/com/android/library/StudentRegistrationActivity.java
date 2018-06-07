package com.android.library;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.library.photofields.PhotoProvider;
import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 9/8/16.
 */
public class StudentRegistrationActivity extends AppCompatActivity {

    CircleImageView ivImage;
    EditText etname,etnumber,etmail,etpassword;
    String name,number,password,mailid;
    int photo;
    Button btsubmit;
    String emailval = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final int CAMERA_RESULT = 1;
    private static final int SELECT_PICTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_reg_activity);

        init();

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(StudentRegistrationActivity.this, ivImage);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.image_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        if (item.getItemId() == R.id.pmCamera) {


                            PackageManager pm = getPackageManager();

                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

                                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                                i.putExtra(MediaStore.EXTRA_OUTPUT, PhotoProvider.CONTENT_URI);

                                startActivityForResult(i, CAMERA_RESULT);

                            } else {

                                Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();

                            }

                        }
                        if (item.getItemId() == R.id.pmGalery) {


                            Intent pickIntent = new Intent();
                            pickIntent.setType("image/*");
                            pickIntent.setAction(Intent.ACTION_GET_CONTENT);

                            String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
                            Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);


                            startActivityForResult(chooserIntent, SELECT_PICTURE);

                        }


                        return true;
                    }
                });

                popup.show();
            }
        });
        btsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fieldsValidate()) {
                    name = etname.getText().toString();
                    number = etnumber.getText().toString();
                    password = etpassword.getText().toString();
                    mailid = etmail.getText().toString();

                    long i = StudentLoginActivity.sqLiteDatabase.insertDetails(name, number, mailid, password);
                    if (i<0) {
                        Toast.makeText(StudentRegistrationActivity.this, "Registration UnSuccessful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StudentRegistrationActivity.this, "Registartion Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(StudentRegistrationActivity.this, StudentLoginActivity.class));
                        finish();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {
            File out = new File(getFilesDir(), "student.jpg");
            if (!out.exists()) {
                Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
            //myDelouge(mBitmap);
            //cropImageView.setImageBitmap(mBitmap);
            imageDialog(mBitmap);
            File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Library/Photos/");

            if (!dir.exists())
                dir.mkdirs();

            File image = new File(Environment.getExternalStorageDirectory().toString() + "/Library/Photos/", "" + Long.toString(System.currentTimeMillis()) + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(image);
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
                fos.flush();
                fos.close();
                Uri uri = Uri.fromFile(image);
//                myDelouge(null,BitmapFactory.decodeFile(uri.toString()));

                //ivPhoto.setImageURI(uri);
            } catch (Exception e) {

                Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {

            if (data == null) {
                //Display an error
                Toast.makeText(this, "data null", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                imageDialog(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void imageDialog(Bitmap bitmap) {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        dialog.setContentView(R.layout.dialog);

        final Button bOk = (Button) dialog.findViewById(R.id.bOk);
        final Button bCancle = (Button) dialog.findViewById(R.id.bCancle);
        final CropImageView ivCroped = (CropImageView) dialog.findViewById(R.id.cropImageView);
        ivCroped.setAspectRatio(1, 1);
        ivCroped.setFixedAspectRatio(true);


        ivCroped.setImageBitmap(bitmap);
       /* if(bitmap!=null)
            ivCroped.setImageBitmap(bitmap);
        else
            ivCroped.setImageBitmap(BitmapFactory.decodeFile(uri.toString()));
*/
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

                Bitmap bitmapNew = Bitmap.createScaledBitmap(ivCroped.getCroppedImage(), 512, 512, false);

                ivImage.setImageBitmap(bitmapNew);

            }
        });

        bCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();

            }
        });


        dialog.show();
    }


    private boolean fieldsValidate() {
        if(etname.getText().toString().length() < 3) {
            etname.setError("Please enter atleast 3 characters.");
            etname.requestFocus();
            return false;
        }
        else if (etmail.getText().length() == 0) {
            etmail.setError("Enter your Email ID.");
            etmail.requestFocus();
            return false;
        }else if(!etmail.getText().toString().matches(emailval)){
            etmail.setError("Please enter valid Email ID.");
            etmail.setText("");
            etmail.requestFocus();
            return false;
        } else if(etpassword.getText().length()==0){
            etpassword.setError("Please type password");
            etpassword.requestFocus();
            return false;
        }else if(etnumber.getText().length()==0){
            etnumber.setError("Please type the Contact No.");
            etnumber.requestFocus();
            return false;
        }
        return true;
    }


    private void init() {
        btsubmit=(Button)findViewById(R.id.btsubmit);
        ivImage=(CircleImageView)findViewById(R.id.ivImage);
        etname=(EditText)findViewById(R.id.etname);
        etnumber=(EditText)findViewById(R.id.etnumber);
        etmail=(EditText)findViewById(R.id.etmailid);
        etpassword=(EditText)findViewById(R.id.etpassword);

    }

}
