package com.android.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by admin on 9/8/16.
 */
public class StudentRegistrationDataBase {

    StudentRegData studentRegDataHelper;

    public StudentRegistrationDataBase(Context context){
        studentRegDataHelper=new StudentRegData(context);
        
    }

    public long insertDetails(String name,String contact,String mail,String password)
    {
        SQLiteDatabase db=studentRegDataHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(StudentRegData.NAME,name);
        contentValues.put(StudentRegData.CONTACTNo,contact);
        contentValues.put(StudentRegData.MAILID,mail);
        contentValues.put(StudentRegData.PASSWORD,password);
        long id=db.insert(StudentRegData.TABLE_NAME,null,contentValues);
        return id;
    }
    public String getAllData(){
        SQLiteDatabase db=studentRegDataHelper.getWritableDatabase();

        String[] colums={ StudentRegData.UID, StudentRegData.NAME, StudentRegData.CONTACTNo, StudentRegData.MAILID, StudentRegData.PASSWORD };
        Cursor cursor=db.query(StudentRegData.TABLE_NAME, colums, null, null, null, null, null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            int cid=cursor.getInt(0);
            String name=cursor.getString(1);
            String contact=cursor.getString(2);
            String mail=cursor.getString(3);
            String password=cursor.getString(4);
            buffer.append( cid + " " + name + " " + contact + " " + mail + " " + password + "\n");
        }
        return buffer.toString();
    }
    public String getData(String mailid){
        SQLiteDatabase db=studentRegDataHelper.getWritableDatabase();

        String[] colums={StudentRegData.MAILID,StudentRegData.PASSWORD};
        Cursor cursor=db.query(StudentRegData.TABLE_NAME,colums,StudentRegData.MAILID+" = '"+mailid+"'",null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(StudentRegData.MAILID);
            int index2=cursor.getColumnIndex(StudentRegData.PASSWORD);
            String name=cursor.getString(index1);
            String password=cursor.getString(index2);
            buffer.append(name+" "+password + "\n");
        }
        return buffer.toString();
    }
    public String getPaasword(String mailid){
        SQLiteDatabase db=studentRegDataHelper.getWritableDatabase();

        String[] colums={StudentRegData.PASSWORD};
        Cursor cursor=db.query(StudentRegData.TABLE_NAME,colums,StudentRegData.MAILID+" = '"+mailid+"'",null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            int index2=cursor.getColumnIndex(StudentRegData.PASSWORD);
            String password=cursor.getString(index2);
            buffer.append(password);
        }
        return buffer.toString();
    }

    public String getMailId(String mail){

        SQLiteDatabase db=studentRegDataHelper.getWritableDatabase();

        String[] colums={ StudentRegData.MAILID};
        Cursor cursor=db.query(StudentRegData.TABLE_NAME,colums,StudentRegData.MAILID+" = '"+mail+"'",null,null,null,null);
        StringBuffer buffer=new StringBuffer();
        while (cursor.moveToNext()){
            int index1=cursor.getColumnIndex(StudentRegData.MAILID);
            String mailid=cursor.getString(index1);
            buffer.append(mailid);
        }
        return buffer.toString();
    }

    static class StudentRegData extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME="StudentRegistration";
        private static final String TABLE_NAME="STUDENTDATA";
        private static final int DATABASE_VERSION=1;
        private static final String UID = "id";
        private static final String NAME = "Name";
        private static final String CONTACTNo = "Contact";
        private static final String MAILID = "Mailid";
        private static final String PASSWORD = "Password";

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT, " + CONTACTNo + " TEXT," + MAILID + " TEXT, " + PASSWORD + " TEXT);";
        private static final String DROP_TABLE="DROP TABLE IF EXISTS"+TABLE_NAME;
        private Context context;

        public StudentRegData(Context context) {
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
            this.context=context;
            // Message.message(context,"Constructor Called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                //  Message.message(context,"OnCreate Called");
            }catch (SQLException e){
                Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                // Message.message(context,"OnUpGrade Called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (SQLException e){
                Toast.makeText(context,""+e,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
