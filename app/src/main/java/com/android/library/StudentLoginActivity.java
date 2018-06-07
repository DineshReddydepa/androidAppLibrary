package com.android.library;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by admin on 9/8/16.
 */
public class StudentLoginActivity extends AppCompatActivity {

    Button btsubmit,btreg,admin;
    EditText etmail,etpass;
    String mail,password;
    String emailval = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    static StudentRegistrationDataBase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login_dialog);
        sqLiteDatabase=new StudentRegistrationDataBase(this);

        initfilds();
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInDialog();
            }
        });
        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentLoginActivity.this, StudentRegistrationActivity.class));
            }
        });

        btsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    mail = etmail.getText().toString();


                    password = etpass.getText().toString();

                    String mId = StudentLoginActivity.sqLiteDatabase.getMailId(mail);

                    String pwd = StudentLoginActivity.sqLiteDatabase.getPaasword(mId);

                    if (mail.equals(""+mId)) {
                        if (password.equals(""+pwd)) {
                            startActivity(new Intent(StudentLoginActivity.this, StudentViewBookActivity.class));
                            finish();
                        } else {
                            Toast.makeText(StudentLoginActivity.this, "Incorrect Password ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StudentLoginActivity.this, "Incorrect MailId ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void logInDialog() {
        final Dialog dialog = new Dialog(StudentLoginActivity.this);
        dialog.setContentView(R.layout.admin_login_dialog);

        final EditText et_name = (EditText) dialog.findViewById(R.id.et_name);
        final EditText et_password = (EditText) dialog.findViewById(R.id.et_password);

        Button submit = (Button) dialog.findViewById(R.id.btn_sumbit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=et_name.getText().toString();
                String password=et_password.getText().toString();
                if (name.equals("admin")){
                    if (password.equals("admin")){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Password ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Incorrect Name", Toast.LENGTH_SHORT).show();

                }
            }
        });
        dialog.show();

        ImageButton cancel = (ImageButton) dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void initfilds() {
        etmail=(EditText)findViewById(R.id.etmail);
        etpass=(EditText)findViewById(R.id.etpassworda);
        btsubmit=(Button)findViewById(R.id.btnsumbits);
        btreg=(Button)findViewById(R.id.btnreg);
        admin=(Button)findViewById(R.id.admin);
    }

    private boolean validateFields() {
        if(!etmail.getText().toString().matches(emailval)){
            etmail.setError("Please enter valid Email ID");
            etmail.setText("");
            etmail.requestFocus();
            return false;
        } else if(etpass.getText().length()==0){
            etpass.setError("Please type password");
            etpass.requestFocus();
            return false;
        }
        return true;
    }
}
