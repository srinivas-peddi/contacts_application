package com.example.user.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddContact extends AppCompatActivity
{

    EditText mNameText;
    EditText mNumberText;
    EditText mEmailText;
    ContactPOJO mContactPOJO;
    ContactsTable mContactsTable;
    Spinner mNumberTypeSpinner;
    int mNumberType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        mNameText = findViewById(R.id.name_text);
        mNumberText = findViewById(R.id.number_text);
        mEmailText = findViewById(R.id.email_text);
        mEmailText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId==EditorInfo.IME_ACTION_DONE)
                {
                    saveContact(v);
                }
                return true;
            }
        });
        mNumberTypeSpinner= findViewById(R.id.number_type);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.number_type_options,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNumberTypeSpinner.setAdapter(adapter);
        mNumberTypeSpinner.setSelection(0);
        mNumberTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mNumberType=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        if(getIntent().getExtras()!=null)
        {
            mContactPOJO = (ContactPOJO) getIntent().getExtras().getSerializable("contactobj");
            mNameText.setText(mContactPOJO.getContactName());
            mNumberText.setText(mContactPOJO.getContactNumber());
            mEmailText.setText(mContactPOJO.getEMailId());
            if(mContactPOJO.getNumberType()<8)
            {
                mNumberTypeSpinner.setSelection(mContactPOJO.getNumberType()-1);
            }
            else
            {
                mNumberTypeSpinner.setSelection(6);
            }
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        mContactsTable = new ContactsTable(this);
        mContactsTable.open();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    public void saveContact(View view)
    {
        if(!mNameText.getText().toString().trim().equals(""))
        {
            if(!mNumberText.getText().toString().trim().equals("") || !mEmailText.getText().toString().trim().equals("") )
            {
                if(mNumberText.getText().toString().trim().equals("") || mNumberText.getText().toString().matches("\\+?\\d{2,4}-?\\d{6,11}"))
                {
                    if( mEmailText.getText().toString().trim().equals("") || mEmailText.getText().toString().matches("[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})"))
                    {
                        if(mContactPOJO ==null)
                        {
                            mContactPOJO =new ContactPOJO(mNameText.getText().toString().trim(), mNumberText.getText().toString().trim(), mEmailText.getText().toString().trim(),mNumberType);
                            if(ContactsHomeScreen.mContacts.contains(mContactPOJO))
                            {
                                Toast.makeText(this,"Contact Already Exists.\nPlease Change the Name",Toast.LENGTH_SHORT).show();
                                mContactPOJO=null;
                            }
                            else
                            {
                                ContactsHomeScreen.mContacts.add(mContactPOJO);
                                mContactsTable.save(mContactPOJO.getContactName(),mContactPOJO.getContactNumber(),mContactPOJO.getEMailId(),mContactPOJO.getNumberType());
                                setResult(RESULT_OK,new Intent());
                                finish();
                            }
                        }
                        else
                        {
                            String oldName= mContactPOJO.getContactName();
                            mContactPOJO.setmContactName(mNameText.getText().toString().trim());
                            int indexOfContact=ContactsHomeScreen.mContacts.indexOf(mContactPOJO);
                            if(indexOfContact==-1)
                            {

                                mContactPOJO.setmContactNumber(mNumberText.getText().toString().trim());
                                mContactPOJO.setmEMailId(mEmailText.getText().toString().trim());
                                mContactPOJO.setNumberType(mNumberType);
                                mContactsTable.save(mContactPOJO.getContactName(),mContactPOJO.getContactNumber(),mContactPOJO.getEMailId(),mContactPOJO.getNumberType());
                                ContactsHomeScreen.mContacts.add(mContactPOJO);
                                setResult(RESULT_FIRST_USER,new Intent());
                                finish();
                            }
                            else
                            {
                                if(indexOfContact==getIntent().getExtras().getInt("position"))
                                {
                                    mContactPOJO.setmContactName(mNameText.getText().toString().trim());
                                    mContactPOJO.setmContactNumber(mNumberText.getText().toString().trim());
                                    mContactPOJO.setmEMailId(mEmailText.getText().toString().trim());
                                    setResult(RESULT_FIRST_USER,new Intent().putExtra("oldName",oldName).putExtra("contactobj", mContactPOJO));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(this,"Contact Already Exists.\nPlease Change the Name",Toast.LENGTH_SHORT).show();
                                    mContactPOJO.setmContactName(oldName);
                                }
                            }

                        }
                    }
                    else
                    {
                        Toast.makeText(this,"Invalid Email ID.\nPlease Enter a Valid EmailID",Toast.LENGTH_SHORT).show();
                        mEmailText.requestFocus();
                    }
                }
                else
                {
                    Toast.makeText(this,"Invalid Number.\nPlease Enter a Valid Number",Toast.LENGTH_SHORT).show();
                    mNumberText.requestFocus();
                }
            }
            else
            {
                Toast.makeText(this,"Please Add A Number Or EMailID",Toast.LENGTH_SHORT).show();
                mEmailText.requestFocus();
            }
        }
        else
        {
            Toast.makeText(this,"Name Cannot Be Empty",Toast.LENGTH_SHORT).show();
            mNameText.setText("");
            mNameText.requestFocus();
        }
    }

    public void discardContact(View view)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to discard the Contact?");
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                setResult(RESULT_CANCELED,new Intent());
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mContactsTable.close();
    }
}
