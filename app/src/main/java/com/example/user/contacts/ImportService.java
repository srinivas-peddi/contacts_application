package com.example.user.contacts;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.Collections;

public class ImportService extends IntentService
{
    private static final String TAG = "ImportService";

    boolean mIsCancelled;
    boolean mIsCompleted=false;

    ImportServiceBinder mImportServiceBinder = new ImportServiceBinder();

    ContactsHomeScreen mContactsHomeScreenObj;

    ArrayList<ContactPOJO> mTempContacts = new ArrayList<>();

    public ImportService()
    {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        importContacts();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mImportServiceBinder;
    }

    public void setUpdateObj(ContactsHomeScreen pUpdateObj)
    {
        mContactsHomeScreenObj =pUpdateObj;
    }

    class ImportServiceBinder extends Binder
    {
        ImportService getService()
        {
            return ImportService.this;
        }
    }

    private void importContacts()
    {
        mIsCancelled=false;
        mIsCompleted=false;
        int progress=0;
        ContentResolver contentResolver= getContentResolver();
        Cursor cursor= contentResolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        int totalCount=cursor.getCount();
        if(cursor.moveToFirst())
        {
            do
            {
                if(!mIsCancelled)
                {
                    int id=cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    ContactPOJO contactPOJO= new ContactPOJO(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),"","",0,"","","");
                    if(!mTempContacts.contains(contactPOJO) && !ContactsHomeScreen.mContacts.contains(contactPOJO))
                    {
                        Cursor detailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE},ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",new String[]{""+id}, null);
                        if(detailCursor!=null && detailCursor.moveToFirst())
                        {
                            contactPOJO.setmContactNumber(detailCursor.getString(detailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            contactPOJO.setNumberType(detailCursor.getInt(detailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));
                        }
                        detailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Email.DATA},ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",new String[]{""+id},null);
                        if(detailCursor!=null && detailCursor.moveToFirst())
                        {
                            contactPOJO.setmEMailId(detailCursor.getString(detailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                        }

                        if(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))!=null)
                        {
                            contactPOJO.setPictureUri(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
                        }
                        detailCursor = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS},ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID +"= ?",new String[]{""+id},null);
                        if(detailCursor!=null && detailCursor.moveToFirst())
                        {
                            contactPOJO.setAddress(detailCursor.getString(detailCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)));
                        }
                        detailCursor= contentResolver.query(ContactsContract.Data.CONTENT_URI,new String[]{ContactsContract.CommonDataKinds.Website.URL},ContactsContract.Data.CONTACT_ID + "="+ id+" AND " + ContactsContract.Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE + "'",null,null,null);
                        if(detailCursor!=null && detailCursor.moveToFirst())
                        {
                            contactPOJO.setWebsite(detailCursor.getString(detailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Website.URL)));
                        }
                        mTempContacts.add(contactPOJO);
                        detailCursor.close();
                    }
                    progress++;
                }
                else
                {
                    break;
                }
                mContactsHomeScreenObj.setProgressAndImportContacts(progress*100/totalCount);
            }while(cursor.moveToNext());
        }
        cursor.close();
        if(!mIsCancelled)
        {
            ContactsHomeScreen.mContacts.addAll(mTempContacts);
            Collections.sort(ContactsHomeScreen.mContacts);
            ContactsTable contactsTable=new ContactsTable(getApplicationContext());
            contactsTable.open();
            contactsTable.save(mTempContacts);
            contactsTable.close();
            mIsCompleted=true;
            mContactsHomeScreenObj.setProgressAndImportContacts(progress*100/totalCount);
        }
        mTempContacts.clear();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        mIsCancelled=false;
        return super.onUnbind(intent);
    }
}
