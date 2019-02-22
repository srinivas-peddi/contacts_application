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

    public void saveSingleContact(String pName,ArrayList<ContactDetails> pDetailList, String pPicUri, String pAddress, String pWebsite)
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
            for(int i=0;i<pDetailList.size();i++)
            {
                addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                addNewContact.put(DatabaseHelper.NUMBER_EMAIL,pDetailList.get(i).getmNumberOrEmail());
                addNewContact.put(DatabaseHelper.NUMBER_EMAIL_TYPE,pDetailList.get(i).getmNumberOrEmailType());
                addNewContact.put(DatabaseHelper.DETAIL_TYPE,pDetailList.get(i).getmDetailType());
                mContactDB.insert(DatabaseHelper.CONTACT_DETAILS,null,addNewContact);
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
                ArrayList<ContactDetails> details= contact.getmDetails();
                int id=getID(name);
                if(details!=null)
                {
                    for(int i=0;i<details.size();i++)
                    {
                        addNewContact.put(DatabaseHelper.CONTACT_ID,id);
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL,details.get(i).getmNumberOrEmail());
                        addNewContact.put(DatabaseHelper.NUMBER_EMAIL_TYPE,details.get(i).getmNumberOrEmailType());
                        addNewContact.put(DatabaseHelper.DETAIL_TYPE,details.get(i).getmDetailType());
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

    public  Cursor fetchDetails(String pName)
    {
        return mContactDB.query(DatabaseHelper.CONTACT_DETAILS,new String[]{DatabaseHelper.ID,DatabaseHelper.NUMBER_EMAIL,DatabaseHelper.NUMBER_EMAIL_TYPE,DatabaseHelper.DETAIL_TYPE},DatabaseHelper.CONTACT_ID+"=?",new String[]{""+getID(pName)},null,null,DatabaseHelper.DETAIL_TYPE+"ASC");
    }

    public boolean checkDBForNumber(String pNumber)
    {
        return mContactDB.query(DatabaseHelper.CONTACT_DETAILS,new String[]{DatabaseHelper.NUMBER_EMAIL},DatabaseHelper.NUMBER_EMAIL+"=?",new String[]{pNumber},null,null,null)==null;
    }

    public void update(String pOldName,ArrayList<Integer> pChangedIds, ContactPOJO contact)
    {
        int id=getID(pOldName);
        ContentValues updateContactValues=new ContentValues();
        updateContactValues.put(DatabaseHelper.NAME,contact.getContactName());
        updateContactValues.put(DatabaseHelper.PIC_URI,contact.getPictureUri());
        updateContactValues.put(DatabaseHelper.ADDRESS,contact.getAddress());
        updateContactValues.put(DatabaseHelper.WEBSITE,contact.getWebsite());
        mContactDB.update(DatabaseHelper.RAW_CONTACT,updateContactValues,DatabaseHelper.ID,new String[]{""+id});
        updateContactValues.clear();
        for(int changedId:pChangedIds)
        {
            ContactDetails detail=contact.getmDetails().get(changedId);
            if(!detail.getmNumberOrEmail().equals(""))
            {
                updateContactValues.put(DatabaseHelper.NUMBER_EMAIL,detail.getmNumberOrEmail());
                updateContactValues.put(DatabaseHelper.NUMBER_EMAIL_TYPE,detail.getmNumberOrEmailType());
                updateContactValues.put(DatabaseHelper.DETAIL_TYPE,detail.getmDetailType());
                mContactDB.update(DatabaseHelper.CONTACT_DETAILS,updateContactValues,DatabaseHelper.ID,new String[]{""+changedId});
            }
            else
            {
                mContactDB.delete(DatabaseHelper.CONTACT_DETAILS,DatabaseHelper.ID+" =? ",new String[]{""+detail.getmId()});
            }
        }
    }

    public void delete(String pName)
    {
        String id[]={""+getID(pName)};
        mContactDB.delete(DatabaseHelper.RAW_CONTACT, DatabaseHelper.NAME + "=?", id);
        mContactDB.delete(DatabaseHelper.CONTACT_DETAILS,DatabaseHelper.CONTACT_ID+" =? ",id);
    }

}
