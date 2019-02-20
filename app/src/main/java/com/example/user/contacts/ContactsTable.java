package com.example.user.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ContactsTable
{
    private Context mContext;
    private SQLiteDatabase mContactDB;
    private DatabaseHelper mDatabaseHelper;


    public ContactsTable(Context c)
    {
        mContext = c;
    }

    ContactsTable open()
    {
        mDatabaseHelper = new DatabaseHelper(mContext);
        mContactDB = mDatabaseHelper.getWritableDatabase();
        return this;
    }

    void close()
    {
        mDatabaseHelper.close();
    }

    public void saveSingleContact(String pName,String pNumber, String pEmail, int pNumberType, String pPicUri, String pAddress, String pWebsite)
    {
            ContentValues addNewContact=new ContentValues();
            addNewContact.put(DatabaseHelper.NAME,pName);
            addNewContact.put(DatabaseHelper.NUMBER,pNumber);
            addNewContact.put(DatabaseHelper.EMAIL,pEmail);
            addNewContact.put(DatabaseHelper.NUMBER_TYPE,pNumberType);
            addNewContact.put(DatabaseHelper.PIC_URI,pPicUri);
            addNewContact.put(DatabaseHelper.ADDRESS,pAddress);
            addNewContact.put(DatabaseHelper.WEBSITE,pWebsite);
            mContactDB.insert(DatabaseHelper.TABLE_NAME, null, addNewContact);
    }

    public void save(ArrayList<ContactPOJO> pContacts)
    {

        try
        {
            mContactDB.beginTransaction();
            ContentValues addNewContact=new ContentValues();
            for(ContactPOJO i:pContacts)
            {
                addNewContact.put(DatabaseHelper.NAME,i.getContactName());
                addNewContact.put(DatabaseHelper.NUMBER,i.getContactNumber());
                addNewContact.put(DatabaseHelper.EMAIL,i.getEMailId());
                addNewContact.put(DatabaseHelper.NUMBER_TYPE,i.getNumberType());
                addNewContact.put(DatabaseHelper.PIC_URI,i.getPictureUri());
                addNewContact.put(DatabaseHelper.ADDRESS,i.getAddress());
                addNewContact.put(DatabaseHelper.WEBSITE,i.getWebsite());
                mContactDB.insert(DatabaseHelper.TABLE_NAME, null, addNewContact);
                addNewContact.clear();
            }
            mContactDB.setTransactionSuccessful();
        }
        finally
        {
            mContactDB.endTransaction();
        }
    }

    public Cursor fetch()
    {
        String[] columns = new String[] { DatabaseHelper.NAME, DatabaseHelper.NUMBER, DatabaseHelper.EMAIL, DatabaseHelper.NUMBER_TYPE, DatabaseHelper.PIC_URI};
        Cursor cursor = mContactDB.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor checkDBForNumber(String pNumber)
    {
        return mContactDB.query(DatabaseHelper.TABLE_NAME,new String[]{DatabaseHelper.NUMBER},DatabaseHelper.NUMBER+"=?",new String[]{pNumber},null,null,null);
    }

    public void update(String pOldName,String pNewName, String pNumber, String pEmail, int pNumberType, String pPicUri,String pAddress, String pWebsite)
    {
        delete(pOldName);
        saveSingleContact(pNewName,pNumber,pEmail,pNumberType,pPicUri,pAddress,pWebsite);
    }

    public void delete(String pName)
    {
        mContactDB.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.NAME + "=?", new String[]{pName});
    }

}
