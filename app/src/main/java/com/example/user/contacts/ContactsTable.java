package com.example.user.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void saveSingleContact(String pName,ArrayList<String> pNumber, ArrayList<String> pEmail, ArrayList<Integer> pNumberType, String pPicUri, String pAddress, String pWebsite)
    {
        try
        {
            mContactDB.beginTransaction();
            ContentValues addNewContact=new ContentValues();
            addNewContact.put(DatabaseHelper.NAME,pName);
            addNewContact.put(DatabaseHelper.PIC_URI,pPicUri);
            addNewContact.put(DatabaseHelper.ADDRESS,pAddress);
            addNewContact.put(DatabaseHelper.WEBSITE,pWebsite);
            Log.v("DB"," "+mContactDB.insert(DatabaseHelper.RAW_CONTACT, null, addNewContact));
            addNewContact.clear();
            int id=getID(pName);
            for(int i=0;i<pNumber.size();i++)
            {
                addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                addNewContact.put(DatabaseHelper.NUMBER,pNumber.get(i));
                addNewContact.put(DatabaseHelper.NUMBER_TYPE,pNumberType.get(i));
                mContactDB.insert(DatabaseHelper.CONTACT_DETAILS,null,addNewContact);
                addNewContact.clear();
            }
            for(String email:pEmail)
            {
                addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                addNewContact.put(DatabaseHelper.EMAIL,email);
                mContactDB.insert(DatabaseHelper.EMAIL_DETAILS,null,addNewContact);
                addNewContact.clear();
            }
            mContactDB.setTransactionSuccessful();
        }
        finally
        {
            mContactDB.endTransaction();
        }
    }

    public void save(ArrayList<ContactPOJO> pContacts)
    {

        try
        {
            mContactDB.beginTransaction();
            ContentValues addNewContact=new ContentValues();
            for(ContactPOJO contact:pContacts)
            {
                String name=contact.getContactName();
                addNewContact.put(DatabaseHelper.NAME,name);
                addNewContact.put(DatabaseHelper.PIC_URI,contact.getPictureUri());
                addNewContact.put(DatabaseHelper.ADDRESS,contact.getAddress());
                addNewContact.put(DatabaseHelper.WEBSITE,contact.getWebsite());
                mContactDB.insert(DatabaseHelper.RAW_CONTACT, null, addNewContact);
                addNewContact.clear();
                ArrayList<String> contactNumbers=contact.getContactNumber();
                ArrayList<Integer> numberType=contact.getNumberType();
                int id=getID(name);
                if(contactNumbers!=null)
                {
                    for(int i=0;i<contact.getContactNumber().size();i++)
                    {
                        addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL,contactNumbers.get(i));
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL_TYPE,numberType.get(i));
                        addNewContact.put(DatabaseHelper.DETAIL_TYPE,0);
                        mContactDB.insert(DatabaseHelper.CONTACT_DETAILS,null,addNewContact);
                        addNewContact.clear();
                    }
                }
                if(contact.getEMailId()!=null)
                {
                    for(String email:contact.getEMailId())
                    {
                        addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL,email);
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL_TYPE,email);
                        addNewContact.put(DatabaseHelper.DETAIL_TYPE,1);
                        mContactDB.insert(DatabaseHelper.CONTACT_DETAILS,null,addNewContact);
                        addNewContact.clear();
                    }
                }
            }
            mContactDB.setTransactionSuccessful();
        }
        finally
        {
            mContactDB.endTransaction();
        }
    }

    private int getID(String pName)
    {
        Cursor cursor=mContactDB.query(DatabaseHelper.RAW_CONTACT,new String[]{DatabaseHelper.ID},DatabaseHelper.NAME+"=?",new String[]{pName},null,null,null);
        if(cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return 0;
    }

    public Cursor fetch()
    {
        String[] columnsToBeRetrieved = new String[] {DatabaseHelper.ID, DatabaseHelper.NAME, DatabaseHelper.PIC_URI, DatabaseHelper.ADDRESS, DatabaseHelper.WEBSITE};
        Cursor cursor = mContactDB.query(DatabaseHelper.RAW_CONTACT, columnsToBeRetrieved, null, null, null, null, null);
        return cursor;

    }

    public Cursor fetchNumbers(String pName)
    {
        return mContactDB.query(DatabaseHelper.CONTACT_DETAILS,new String[]{DatabaseHelper.NUMBER,DatabaseHelper.NUMBER_TYPE},DatabaseHelper.CONTACT_ID+" =?",new String[]{""+getID(pName)},null,null,null);
    }

    public  Cursor fetchEmailIDs(String pName)
    {
        return mContactDB.query(DatabaseHelper.EMAIL_DETAILS,new String[]{DatabaseHelper.EMAIL},DatabaseHelper.CONTACT_ID+" =?",new String[]{""+getID(pName)},null,null,null);
    }

    public boolean checkDBForNumber(String pNumber)
    {
        return mContactDB.query(DatabaseHelper.CONTACT_DETAILS,new String[]{DatabaseHelper.NUMBER},DatabaseHelper.NUMBER+"=?",new String[]{pNumber},null,null,null)==null;
    }

    public void update(String pOldName,String pNewName, ArrayList<String> pNumber, ArrayList<String> pEmail, ArrayList<Integer> pNumberType, String pPicUri,String pAddress, String pWebsite)
    {

        int id=getID(pOldName);
        ContentValues addNewContact=new ContentValues();
        addNewContact.put(DatabaseHelper.NAME,pNewName);
        addNewContact.put(DatabaseHelper.PIC_URI,pPicUri);
        addNewContact.put(DatabaseHelper.ADDRESS,pAddress);
        addNewContact.put(DatabaseHelper.WEBSITE,pWebsite);
        mContactDB.update(DatabaseHelper.RAW_CONTACT,addNewContact,DatabaseHelper.ID,new String[]{""+id});
        for(int i=0;i<pNumber.size();i++)
        {
            addNewContact.put(DatabaseHelper.NUMBER,pNumber.get(i));
            addNewContact.put(DatabaseHelper.NUMBER_TYPE,pNumberType.get(i));
            mContactDB.insert(DatabaseHelper.CONTACT_DETAILS,null,addNewContact);
            addNewContact.clear();
        }
        for(String email:pEmail)
        {
            addNewContact.put(DatabaseHelper.EMAIL,email);
            mContactDB.insert(DatabaseHelper.EMAIL_DETAILS,null,addNewContact);
            addNewContact.clear();
        }
    }

    public void delete(String pName)
    {
        String id[]={""+getID(pName)};
        mContactDB.delete(DatabaseHelper.RAW_CONTACT, DatabaseHelper.NAME + "=?", id);
        mContactDB.delete(DatabaseHelper.CONTACT_DETAILS,DatabaseHelper.CONTACT_ID+" =? ",id);
        mContactDB.delete(DatabaseHelper.EMAIL_DETAILS,DatabaseHelper.CONTACT_ID+" =? ",id);
    }

}
