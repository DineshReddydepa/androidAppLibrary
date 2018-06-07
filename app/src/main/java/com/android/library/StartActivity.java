package com.android.library;

import android.app.Dialog;
import android.app.FragmentTransaction;
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
public class StartActivity extends AppCompatActivity {

    Button bt_admin,bt_student,bt_reg;
    static StudentRegistrationDataBase sqLiteDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        sqLiteDatabase=new StudentRegistrationDataBase(this);
        init();
        bt_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInDialog();
            }
        });


        bt_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, StudentLoginActivity.class));
            }
        });


    }


    private void logInDialog() {
        final Dialog dialog = new Dialog(StartActivity.this);
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



    private void init() {
        bt_admin=(Button)findViewById(R.id.bt_admin);
        bt_student=(Button)findViewById(R.id.bt_studentlogin);

    }
}
