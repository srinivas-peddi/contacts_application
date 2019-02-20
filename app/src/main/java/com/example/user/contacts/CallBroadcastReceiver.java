package com.example.user.contacts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallBroadcastReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
        {
            Log.v("CALL BROADCAST OUTGOING",intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER));
        }
        else
        {
            String state=intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            if(intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
            {
                ContactsTable contactsTable=new ContactsTable(context);
                contactsTable.open();
                Cursor cursor=contactsTable.checkDBForNumber(intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER));
                if(state.equals("IDLE"))
                {
                    if(cursor.getCount()==0)
                    {
                        Intent intent1= new Intent(context,SaveDialog.class);
                        intent1.putExtra("contactNumber",intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER));
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);
                    }
                }
                contactsTable.close();
            }
        }
    }
}
