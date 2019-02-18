package com.example.user.contacts;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsHomeScreen extends AppCompatActivity
{

    private static final String TAG = "ContactsHomeScreen";

    private static final int DISPLAY_EDIT_DELETE_CONTACT = 30;

    private static final int PERMISSION_FOR_READ_CONTACTS=100;
    private static final int ADD_NEW_CONTACT=10;
    static ArrayList<ContactPOJO> mContacts;
    CustomAdapter mCustomAdapter;
    ContactsTable mContactsTable;
    int mPosition;
    ListView mListView;
    ProgressBar mProgressBar;
    DBReadAsyncTask mReadDB;
    ImportService mImportService;
    AlertDialog.Builder mAlertDialogBuilder;
    AlertDialog mAlertDialog;
    ProgressBar mProgressBarForImport;
    Button mCancelButton;
    TextView mProgressText;
    boolean mIsBound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        mContacts = new ArrayList<>();
        mReadDB = new DBReadAsyncTask();
        mContactsTable = new ContactsTable(this).open();
        mListView= findViewById(R.id.contact_list);
        mProgressBar= findViewById(R.id.progress_circular);
        mReadDB.execute(mContactsTable);
        displayContacts();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Intent importIntent = new Intent(getApplicationContext(),ImportService.class);
        bindService(importIntent,mServiceConnection,Context.BIND_AUTO_CREATE);
    }

    class DBReadAsyncTask extends AsyncTask<ContactsTable,Void,Void>
    {
        @Override
        protected void onPreExecute()
        {
            mListView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            mProgressBar.setVisibility(View.GONE);
            Collections.sort(mContacts);
            mListView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(ContactsTable... contactsTables)
        {
            ContactsTable contactsTable=contactsTables[0];
            Cursor cursor= contactsTable.fetch();
            if(cursor.moveToFirst())
            {
                mContacts.add(new ContactPOJO(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4)));
            }
            else
            {
                return null;
            }
            while(cursor.moveToNext())
            {
                mContacts.add(new ContactPOJO(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getString(4)));
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_actionbar_menu, menu);
        return true;
    }

    void displayContacts()
    {
        mCustomAdapter =new CustomAdapter(this,R.layout.list_view, mContacts);
        final ListView listView= findViewById(R.id.contact_list);
        listView.setAdapter(mCustomAdapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Object o= listView.getItemAtPosition(position);
                ContactPOJO contactPOJO= (ContactPOJO) o;
                if(view.getId()==R.id.call_email_button)
                {
                    if(!contactPOJO.getContactNumber().equals(""))
                    {
                        Intent intent=new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+ contactPOJO.getContactNumber()));
                        if(intent.resolveActivity(getPackageManager())!=null)
                        {
                            startActivity(new Intent(intent));
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"No Dialer found to Dial Number",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Intent intent=new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"+ contactPOJO.getEMailId()));
                        if(intent.resolveActivity(getPackageManager())!=null)
                        {
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),"No Mail Application found To Send Mail",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    mPosition=position;
                    startActivityForResult(new Intent(getBaseContext(),DisplayContact.class).putExtra("contactobj",contactPOJO).putExtra("position",position), DISPLAY_EDIT_DELETE_CONTACT);
                }
            }
        });
    }

    public void addNewContact()
    {
        startActivityForResult(new Intent(this,AddContact.class),ADD_NEW_CONTACT);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add_contact:
                addNewContact();
                break;

            case R.id.import_contacts:
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS))
                    {
                        Toast.makeText(this,"Contacts Permission is required to Import Contacts",Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},PERMISSION_FOR_READ_CONTACTS);
                    }
                    else
                    {
                        AlertDialog.Builder permissionAlert = new AlertDialog.Builder(this);
                        permissionAlert.setTitle("Contacts Permission");
                        permissionAlert.setMessage("Please Provide the Permission:");
                        permissionAlert.setPositiveButton("Give Permission", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent appSettingsIntent= new Intent();
                                appSettingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                appSettingsIntent.setData(Uri.fromParts("package",getPackageName(),null));
                                startActivity(appSettingsIntent);
                            }
                        });

                        permissionAlert.setNegativeButton("Cancel Import", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Toast.makeText(getBaseContext(),"Import Cancelled",Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog permissionDialog= permissionAlert.create();
                        permissionDialog.show();
                    }
                }
                else
                {
                    startImportService();
                }
                break;
        }
        return true;
    }

    void startImportService()
    {
        mImportService.setUpdateObj(this);
        Intent importIntent= new Intent(this,ImportService.class);
        mAlertDialogBuilder = new AlertDialog.Builder(this);
        View dialogBuilder= getLayoutInflater().inflate(R.layout.custom_dialog,null);
        mAlertDialogBuilder.setView(dialogBuilder);
        mAlertDialogBuilder.setTitle("Importing Contacts");
        mProgressBarForImport = dialogBuilder.findViewById(R.id.progress_horizontal);
        mCancelButton = dialogBuilder.findViewById(R.id.cancel_dialog_button);
        mProgressText = dialogBuilder.findViewById(R.id.progress_text);
        mCancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mImportService.mIsCancelled=true;
                mAlertDialog.hide();
                mListView.setVisibility(View.VISIBLE);
            }
        });
        mAlertDialog = mAlertDialogBuilder.create();
        mListView.setVisibility(View.GONE);
        mAlertDialog.show();
        startService(importIntent);
    }

    void setProgressAndImportContacts(final int pProgress)
    {
        if(!mImportService.mIsCompleted)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    mProgressBarForImport.setProgress(pProgress);
                    mProgressText.setText(""+pProgress);
                }
            });
        }
        else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    mAlertDialog.hide();
                    mCustomAdapter.notifyDataSetChanged();
                    mListView.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    ServiceConnection mServiceConnection=new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            ImportService.ImportServiceBinder importServiceBinder= (ImportService.ImportServiceBinder) service;
            mImportService= importServiceBinder.getService();
            mIsBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mIsBound=false;
        }
    };



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode==PERMISSION_FOR_READ_CONTACTS)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                startImportService();
            }
            else
            {
                Toast.makeText(this,"Cannot Read Contacts Without Permission. Import Cancelled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==ADD_NEW_CONTACT)
        {
            if(resultCode==RESULT_OK)
            {
                Toast.makeText(this,"Contact Saved",Toast.LENGTH_SHORT).show();
                Collections.sort(mContacts);
                mCustomAdapter.notifyDataSetChanged();
            }
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(this,"Contact Discarded",Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode== DISPLAY_EDIT_DELETE_CONTACT)
        {
            if(resultCode== RESULT_OK)
            {
                ContactPOJO contactPOJO=(ContactPOJO) data.getExtras().getSerializable("contactobj");
                mContactsTable.delete(contactPOJO.getContactName());
                mContacts.remove(contactPOJO);
                mCustomAdapter.notifyDataSetChanged();
            }
            else if(resultCode==RESULT_FIRST_USER)
            {
                ContactPOJO contactPOJO=(ContactPOJO) data.getExtras().getSerializable("contactobj");
                mContactsTable.update(data.getExtras().getString("oldName"),contactPOJO.getContactName(),contactPOJO.getContactNumber(),contactPOJO.getEMailId(),contactPOJO.getNumberType(),null);
                Toast.makeText(this,"Contact Updated",Toast.LENGTH_SHORT).show();
                mContacts.set(mPosition,contactPOJO);
                Collections.sort(mContacts);
                mCustomAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mContactsTable.close();
    }
}

class CustomAdapter extends ArrayAdapter<ContactPOJO>
{

    public CustomAdapter(Context context, int resource, List<ContactPOJO> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final int itemPosition=position;
        final ViewGroup itemParent=parent;
        if(convertView==null)
        {
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.list_view,parent,false);
        }
        final ContactPOJO contactPOJO= getItem(itemPosition);
        TextView contactSymbol= convertView.findViewById(R.id.contact_symbol);
        TextView contactNameDisplay= convertView.findViewById(R.id.contact_item);
        TextView contactNumberDisplay= convertView.findViewById(R.id.number_item);
        Button button= convertView.findViewById(R.id.call_email_button);
        contactSymbol.setText(contactPOJO.getContactName().substring(0,1).toUpperCase());
        contactNameDisplay.setText(contactPOJO.getContactName());
        if(!contactPOJO.getContactNumber().equals(""))
        {
            contactNumberDisplay.setText(contactPOJO.getContactNumber());
            button.setBackgroundResource(R.drawable.ic_call_img);
        }
        else
        {
            contactNumberDisplay.setText(contactPOJO.getEMailId());
            button.setBackgroundResource(R.drawable.ic_email_img);
        }
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((ListView) itemParent).performItemClick(v, itemPosition, 0);
            }
        });
        return convertView;
    }
}