package com.example.user.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactsTable
{
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;
    private SQLiteDatabase mContactDB;

    public ContactsTable(Context c)
    {
        mContext = c;
    }

    public ContactsTable open()
    {
        mDatabaseHelper = new DatabaseHelper(mContext);
        mContactDB = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        mDatabaseHelper.close();
    }

    public void save(String pName, String pNumber, String pEmail,int pNumberType)
    {
        ContentValues addNewContact=new ContentValues();
        addNewContact.put(DatabaseHelper.NAME,pName);
        addNewContact.put(DatabaseHelper.NUMBER,pNumber);
        addNewContact.put(DatabaseHelper.EMAIL,pEmail);
        addNewContact.put(DatabaseHelper.NUMBER_TYPE,pNumberType);
        mContactDB.insert(DatabaseHelper.TABLE_NAME, null, addNewContact);
    }

    public Cursor fetch()
    {
        String[] columns = new String[] { DatabaseHelper.NAME, DatabaseHelper.NUMBER, DatabaseHelper.EMAIL };
        Cursor cursor = mContactDB.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, DatabaseHelper.NAME+" ASC");
        return cursor;
    }

    public Cursor checkDBForNumber(String pNumber)
    {
        return mContactDB.query(DatabaseHelper.TABLE_NAME,new String[]{DatabaseHelper.NUMBER},DatabaseHelper.NUMBER+"=?",new String[]{pNumber},null,null,null);
    }

    public void update(String pOldName,String pNewName, String pNumber, String pEmail, int pNumberType)
    {
        delete(pOldName);
        save(pNewName,pNumber,pEmail,pNumberType);
    }

    public void delete(String pName)
    {
        mContactDB.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.NAME + "=?", new String[]{pName});
    }

}
