package com.example.user.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String DB_NAME="Contacts.db";
    public static final String ID="Id";
    public static final String TABLE_NAME="contacts_table";
    public static final String NAME = "Name";
    public static final String NUMBER = "Contact_No";
    public static final String EMAIL = "Email_ID";
    public static final String NUMBER_TYPE= "Number_type";
    public static final String PIC_URI= "Picture_URI";
    public static final String ADDRESS= "Address";
    public static final String WEBSITE= "Website";
    public static final int VERSION=1;



    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ NAME +" TEXT,"+ NUMBER +" TEXT, "+ EMAIL +" TEXT, "+ NUMBER_TYPE +" INTEGER, "+PIC_URI+" TEXT, "+ADDRESS+" TEXT,"+WEBSITE+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
