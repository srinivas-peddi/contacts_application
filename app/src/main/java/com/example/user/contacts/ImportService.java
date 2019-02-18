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

    private int mProgress;

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

    public class ImportServiceBinder extends Binder
    {
        public ImportService getService()
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
                    ContactPOJO contactPOJO= new ContactPOJO(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),"","",0);
                    if(!mTempContacts.contains(contactPOJO) && !ContactsHomeScreen.mContacts.contains(contactPOJO))
                    {
                        Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",new String[]{""+id}, null);
                        if(phoneCursor!=null && phoneCursor.moveToFirst())
                        {
                            contactPOJO.setmContactNumber(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            contactPOJO.setNumberType(phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)));

                        }
                        phoneCursor.close();
                        Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",new String[]{""+id},null);
                        if(emailCursor!=null && emailCursor.moveToFirst())
                        {
                            contactPOJO.setmEMailId(emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
                        }
                        mTempContacts.add(contactPOJO);
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
            for(ContactPOJO i:mTempContacts)
            {
                contactsTable.save(i.getContactName(),i.getContactNumber(),i.getEMailId(),i.getNumberType());
            }
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
