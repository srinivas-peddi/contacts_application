package com.example.user.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String DB_NAME="Contacts.db";
    public static final String TABLE_NAME="contacts_table";
    public static final String NAME = "Name";
    public static final String NUMBER = "Contact_No";
    public static final String EMAIL = "Email_ID";
    public static final String NUMBER_TYPE= "Number_type";
    public static final int VERSION=1;



    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ NAME +" TEXT PRIMARY KEY,"+ NUMBER +" TEXT, "+ EMAIL +" TEXT, "+ NUMBER_TYPE +" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
