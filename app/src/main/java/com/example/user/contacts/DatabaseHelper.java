package com.example.user.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    public static final String DB_NAME="Contacts.db";
    public static final String ID="_id";
    public static final String RAW_CONTACT ="contacts_table";
    public static final String CONTACT_DETAILS ="numbers_table";
    public static final String NAME = "Name";
    public static final String CONTACT_ID= "Contact_ID";
    public static final String PIC_URI= "Picture_URI";
    public static final String ADDRESS= "Address";
    public static final String WEBSITE= "Website";
    public static final String NUMBER_EMAIL= "Number_Email";
    public static final String NUMBER_EMAIL_TYPE= "Number_Email_Type";
    public static final String DETAIL_TYPE= "Detail_Type";
    public static final int VERSION=1;



    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE "+ RAW_CONTACT +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ NAME +" TEXT,"+PIC_URI+" TEXT, "+ADDRESS+" TEXT,"+WEBSITE+" TEXT)");
        db.execSQL("CREATE TABLE "+ CONTACT_DETAILS +" ("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+ CONTACT_ID +" INTEGER,"+NUMBER_EMAIL+" TEXT,"+NUMBER_EMAIL_TYPE+" INTEGER,"+DETAIL_TYPE+" INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + RAW_CONTACT);
        onCreate(db);
    }
}
