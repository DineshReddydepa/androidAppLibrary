package com.android.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.library.database.DBManager;

/**
 * Created by admin on 10/8/16.
 */
public class AddBookActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private EditText langEditText;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Record");

        setContentView(R.layout.activity_add_record);

        subjectEditText = (EditText) findViewById(R.id.subject_et);
        descEditText = (EditText) findViewById(R.id.description_et);
        langEditText=(EditText)findViewById(R.id.et_lang);

        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String lang = langEditText.getText().toString();

                dbManager.insert(name, lang,desc);

                Intent main = new Intent(AddBookActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);
                break;
        }
    }

}
