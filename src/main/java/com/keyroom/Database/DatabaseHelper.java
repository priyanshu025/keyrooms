package com.keyroom.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    //intialize database name and table name

    private static final String DATABASE_NAME="database_name";
    private static final String TABLE_NAME="table_name";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        String createTable= "create table "+ TABLE_NAME +
                "(id INTEGER PRIMARY KEY, txt TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }


    public boolean addText(String text){
        //get writeable database
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        //cerate contentValues
        ContentValues contentValues =new ContentValues();
        contentValues.put("txt",text);

        //Add value into database
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return  true;

    }

    public ArrayList getAllText(){
        // get readable database
        SQLiteDatabase sqLiteDatabase=  this.getReadableDatabase();
        ArrayList<String> arrayList=new ArrayList<String>();

        //create cursor to select values

        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            arrayList.add(cursor.getString(cursor.getColumnIndex("txt")));
            cursor.moveToNext();

        }
        cursor.close();
        return arrayList;

    }
}
