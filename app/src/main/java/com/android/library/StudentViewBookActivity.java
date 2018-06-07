package com.android.library;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.library.database.DBManager;
import com.android.library.database.DatabaseHelper;

/**
 * Created by admin on 10/8/16.
 */
public class StudentViewBookActivity extends AppCompatActivity {

    Button books;
    private DBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECT, DatabaseHelper.LANG, DatabaseHelper.DESC };

    final int[] to = new int[] { R.id.id, R.id.tv_titlea, R.id.tv_language, R.id.tv_desc };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_books_activity);

        books=(Button)findViewById(R.id.bt_book);
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager = new DBManager(StudentViewBookActivity.this);
                dbManager.open();
                Cursor cursor = dbManager.fetch();


                listView = (ListView) findViewById(R.id.list_view);
                listView.setEmptyView(findViewById(R.id.empty));
                adapter = new SimpleCursorAdapter(StudentViewBookActivity.this, R.layout.activity_view_record, cursor, from, to, 0);
                adapter.notifyDataSetChanged();

                listView.setAdapter(adapter);
            }
        });
    }
}
