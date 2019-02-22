package com.example.user.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class AddContact extends AppCompatActivity
{

    EditText mNameText;
    EditText mNumberText;
    EditText mEmailText;
    EditText mAddressText;
    EditText mWebsiteText;
    LinearLayout mNumberLayout;
    LinearLayout mEmailLayout;
    ArrayList<LinearLayout> mNumberViews;
    ArrayList<LinearLayout> mEmailViews;
    ContactPOJO mContactPOJO;
    int mTagCount;
    Spinner mNumberTypeSpinner;
    int mNumberType;
    String mPicUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);
        mNumberViews= new ArrayList<>();
        mEmailViews = new ArrayList<>();
        mNumberLayout= findViewById(R.id.number_layout);
        mAddressText = findViewById(R.id.address_text);
        mWebsiteText = findViewById(R.id.website_text);
        mNameText = findViewById(R.id.name_text);
        mEmailLayout = findViewById(R.id.email_add_layout);
        if(getIntent().getExtras()!=null)
        {
            mContactPOJO = (ContactPOJO) getIntent().getExtras().getSerializable("contactobj");
            mNameText.setText(mContactPOJO.getContactName());
            ArrayList<ContactDetails> details= mContactPOJO.getmDetails();
            if(details!=null && details.size()>0)
            {
                for(int i=0;i<details.size();i++)
                {
                    numberFieldInflater(details.get(i).getmId());
                    if(details.get(i).getmDetailType()==0)
                    {
                        LinearLayout numberLayout=mNumberViews.get(mNumberViews.size()-1);
                        EditText numberText= numberLayout.findViewById(R.id.number_add);
                        numberText.setText(details.get(i).getmNumberOrEmail());
                        Spinner numberType= numberLayout.findViewById(R.id.number_type);
                        if(details.get(i).getmNumberOrEmailType()>6)
                        {
                            numberType.setSelection(6);
                        }
                        else
                        {
                            numberType.setSelection(details.get(i).getmNumberOrEmailType());
                        }
                        i++;
                    }
                    else
                    {
                        emailFieldInflater(details.get(i).getmId());
                        LinearLayout emailLayout=mEmailViews.get(mEmailViews.size()-1);
                        EditText emailText= emailLayout.findViewById(R.id.email_add);
                        emailText.setText(details.get(i).getmNumberOrEmail());
                        i++;
                    }
                }
            }
            else
            {
                numberFieldInflater(mTagCount++);
                emailFieldInflater(mTagCount++);
            }
            mPicUri= mContactPOJO.getPictureUri();
            mAddressText.setText(mContactPOJO.getAddress());
            mWebsiteText.setText(mContactPOJO.getWebsite());
        }
        else
        {
            numberFieldInflater(mTagCount++);
            emailFieldInflater(mTagCount++);
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void addNumberField(View view)
    {
        EditText numEdit= mNumberViews.get(mNumberViews.size()-1).findViewById(R.id.number_add);
        if(!numEdit.getText().toString().equals(""))
        {
            numberFieldInflater(mTagCount++);
            mNumberViews.get(mNumberViews.size()-1).findViewById(R.id.number_add).requestFocus();
        }
    }

    public void addEmailField(View view)
    {
        EditText emailEdit= mEmailViews.get(mEmailViews.size()-1).findViewById(R.id.email_add);
        if(!emailEdit.getText().toString().equals(""))
        {
            emailFieldInflater(mTagCount++);
            mEmailViews.get(mEmailViews.size()-1).findViewById(R.id.email_add).requestFocus();
        }

    }

    void numberFieldInflater(int pTag)
    {
        LinearLayout linearLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.number_field_layout,null);
        linearLayout.setPadding(5,10,5,0);
        linearLayout.setTag(pTag);
        mNumberLayout.addView(linearLayout);
        mNumberViews.add(linearLayout);
    }

    void emailFieldInflater(int pTag)
    {
        LinearLayout linearLayout= (LinearLayout) getLayoutInflater().inflate(R.layout.email_field_layout,null);
        linearLayout.setPadding(5,10,5,0);
        linearLayout.setTag(pTag);
        mEmailLayout.addView(linearLayout);
        mEmailViews.add(linearLayout);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        finish();
        return true;
    }

    public void saveContact(View view)
    {

        ArrayList<Integer> changedIds= new ArrayList<>();
        if(!mNameText.getText().toString().trim().equals(""))
        {
            if(mContactPOJO==null)
            {
                for(LinearLayout numberLayout:mNumberViews)
                {
                    EditText numberEdit= numberLayout.findViewById(R.id.number_add);
                    Spinner numberType= numberLayout.findViewById(R.id.number_type);

//                        changedIds.add((Integer) numberLayout.getTag());
//                        ContactDetails details=new ContactDetails();
//                        details.setId((Integer) numberLayout.getTag());
//                        mContactPOJO.getmDetails().get(mContactPOJO.getmDetails().indexOf(details)).setmNumberOrEmail("");

                    if(numberEdit.getText().toString().trim().matches("\\+?\\d{2,4}-?\\d{6,11}"))   //TODO //Create One more regex Expression...
                    {
                        ContactDetails details=new ContactDetails();
                        details.setmNumberOrEmail(numberEdit.getText().toString().trim());
                        details.setmNumberOrEmailType(numberType.getSelectedItemPosition());
                    }
                    else
                    {
                        mNumberLayout.removeView(numberLayout);
                    }
                }
                for(LinearLayout emailLayout:mEmailViews)
                {
                    EditText emailEdit
                }
            }
        }
        else
        {
            Toast.makeText(this, "Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
            mNameText.setText("");
            mNameText.requestFocus();
        }
//        HashSet<String> tempNumberList;
//        ArrayList<Integer> tempNumberTypeList=new ArrayList<>();
//        HashSet<String> tempEmailList;

//            if(mContactPOJO!=null)
//            {
//                if(mContactPOJO.getContactNumber()!=null)
//                {
//                    tempNumberList= new HashSet<>(mContactPOJO.getContactNumber());
//                }
//                else
//                {
//                    tempNumberList=new HashSet<>();
//                }
//                if(mContactPOJO.getEMailId()!=null)
//                {
//                    tempEmailList=new HashSet<>(mContactPOJO.getEMailId());
//                }
//                else
//                {
//                    tempEmailList=new HashSet<>();
//                }
//            }
//            else
//            {
//                tempNumberList=new HashSet<>();
//                tempEmailList=new HashSet<>();
//            }
//            for(LinearLayout numberLayout:mNumberViews)
//            {
//                EditText numberEdit=numberLayout.findViewById(R.id.number_add);
//                Spinner numberType=numberLayout.findViewById(R.id.number_type);
//                if(numberEdit.getText().toString().trim().equals(""))
//                {
//                    mNumberLayout.removeView(numberLayout);
//                }
//                else if(numberEdit.getText().toString().trim().matches("\\+?\\d{2,4}-?\\d{6,11}"))
//                {
//
//                    tempNumberList.add(numberEdit.getText().toString().trim());
//                    tempNumberTypeList.add(numberType.getSelectedItemPosition());
//                }
//                else
//                {
//                    Toast.makeText(this,"Invalid Number.\nPlease Enter a Valid Number",Toast.LENGTH_SHORT).show();
//                    numberEdit.requestFocus();
//                    return;
//                }
//            }
//            for(LinearLayout emailLayout:mEmailViews)
//            {
//                EditText emailText= emailLayout.findViewById(R.id.email_add);
//                if(emailText.getText().toString().trim().equals(""))
//                {
//                    mEmailLayout.removeView(emailLayout);
//                }
//                else if(emailText.getText().toString().trim().matches("[\\w-+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})"))
//                {
//                    tempEmailList.add(emailText.getText().toString().trim());
//                }
//                else
//                {
//                    Toast.makeText(this,"Invalid Email ID.\nPlease Enter a Valid EmailID",Toast.LENGTH_SHORT).show();
//                    emailText.requestFocus();
//                    return;
//                }
//            }
//
//            if(tempNumberList.size()==0 && tempEmailList.size()==0)
//            {
//                Toast.makeText(this,"Please Add A Number Or EMailID",Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if(mContactPOJO == null)
//            {
//                mContactPOJO = new ContactPOJO(mNameText.getText().toString().trim());
//                if(ContactsHomeScreen.mContacts.contains(mContactPOJO))
//                {
//                    Toast.makeText(this,"Contact Already Exists with this Name.\nPlease Change the Name",Toast.LENGTH_SHORT).show();
//                    mNameText.requestFocus();
//                    mContactPOJO=null;
//                    return;
//                }
//                mContactPOJO.setContactNumber(new ArrayList<>(tempNumberList));
//                mContactPOJO.setNumberType(tempNumberTypeList);
//                mContactPOJO.setEMailId(new ArrayList<>(tempEmailList));
//                setResult(RESULT_OK,new Intent().putExtra("contactobj",mContactPOJO));
//                finish();
//            }
//            else
//            {
//                String oldName=mContactPOJO.getContactName();
//                mContactPOJO.setContactName(mNameText.getText().toString().trim());
//                if(ContactsHomeScreen.mContacts.contains(mContactPOJO))
//                {
//                    if(ContactsHomeScreen.mContacts.indexOf(mContactPOJO)!=getIntent().getExtras().getInt("position"))
//                    {
//                        Toast.makeText(this,"Contact Already Exists.\nPlease Change the Name",Toast.LENGTH_SHORT).show();
//                        mNameText.requestFocus();
//                        return;
//                    }
//                }
//                setResult(RESULT_FIRST_USER,new Intent().putExtra("oldName",oldName).putExtra("contactobj",mContactPOJO));
//                finish();
//            }
//
//        }
//        else
//        {

//        }
    }

    private void makeContact(HashSet<String> pNumberList, ArrayList<Integer> pNumberTypeList, HashSet<String> pEmailList)
    {
        mContactPOJO.setContactNumber(new ArrayList<>(pNumberList));
        mContactPOJO.setNumberType(new ArrayList<>(pNumberTypeList));
        mContactPOJO.setEMailId(new ArrayList<>(pEmailList));
        if(!mWebsiteText.getText().toString().trim().equals(""))
        {
            mContactPOJO.setWebsite(mWebsiteText.getText().toString().trim());
        }
        if(!mAddressText.getText().toString().trim().equals(""))
        {
            mContactPOJO.setAddress(mAddressText.getText().toString().trim());
        }
    }

//        if(!mNameText.getText().toString().trim().equals(""))
//        {
//            if(!mNumberText.getText().toString().trim().equals("") || !mEmailText.getText().toString().trim().equals("") )
//            {
//                if(mNumberText.getText().toString().trim().equals("") || mNumberText.getText().toString().matches("\\+?\\d{2,4}-?\\d{6,11}"))
//                {
//                    if( mEmailText.getText().toString().trim().equals("") || mEmailText.getText().toString().matches())
//                    {
//                        if(mContactPOJO ==null)
//                        {
////                            mContactPOJO =new ContactPOJO(mNameText.getText().toString().trim(), mNumberText.getText().toString().trim(), mEmailText.getText().toString().trim(),mNumberType,"","",mWebsiteText.getText().toString().trim());
//                            else
//                            {
//                                ContactsHomeScreen.mContacts.add(mContactPOJO);
//                                setResult(RESULT_OK,new Intent().putExtra("contactobj",mContactPOJO));
//                                finish();
//                            }
//                        }
//                        else
//                        {
//                            String oldName= mContactPOJO.getContactName();
//                            mContactPOJO.setContactName(mNameText.getText().toString().trim());
//                            int indexOfContact=ContactsHomeScreen.mContacts.indexOf(mContactPOJO);
//                            if(indexOfContact==-1)
//                            {
//
////                                mContactPOJO.setContactNumber(mNumberText.getText().toString().trim());
////                                mContactPOJO.setEMailId(mEmailText.getText().toString().trim());
////                                mContactPOJO.setNumberType(mNumberType);
//                                mContactPOJO.setAddress(mAddressText.getText().toString().trim());
//                                mContactPOJO.setWebsite(mWebsiteText.getText().toString().trim());
//
//                                ContactsHomeScreen.mContacts.add(mContactPOJO);
//                            }
//                            else
//                            {
//                                {
//                                    mContactPOJO.setContactName(mNameText.getText().toString().trim());
////                                    mContactPOJO.setContactNumber(mNumberText.getText().toString().trim());
////                                    mContactPOJO.setNumberType(mNumberType);
////                                    mContactPOJO.setEMailId(mEmailText.getText().toString().trim());
//                                    mContactPOJO.setAddress(mAddressText.getText().toString().trim());
//                                    mContactPOJO.setWebsite(mWebsiteText.getText().toString().trim());
//
//                                    setResult(RESULT_FIRST_USER,new Intent().putExtra("oldName",oldName).putExtra("contactobj", mContactPOJO));
//                                    finish();
//                                }
//                                else
//                                {
//                                    Toast.makeText(this,"Contact Already Exists.\nPlease Change the Name",Toast.LENGTH_SHORT).show();
//                                    mContactPOJO.setContactName(oldName);
//                                }
//                            }
//
//                        }
//                    }
//                    else
//                    {
//                        Toast.makeText(this,"Invalid Email ID.\nPlease Enter a Valid EmailID",Toast.LENGTH_SHORT).show();
//                        mEmailText.requestFocus();
//                    }
//                }
//                else
//                {
//                    Toast.makeText(this,"Invalid Number.\nPlease Enter a Valid Number",Toast.LENGTH_SHORT).show();
//                    mNumberText.requestFocus();
//                }
//            }
//            else
//            {
//                Toast.makeText(this,"Please Add A Number Or EMailID",Toast.LENGTH_SHORT).show();
//                mEmailText.requestFocus();
//            }
//        }
//        else
//        {
//
//        }
//    }

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

    public void displayDetails(View view)
    {
        RelativeLayout displayMoreFields= findViewById(R.id.address_website_layout);
        TextView displayMoreText = findViewById(R.id.display_more_fields_text);
        displayMoreText.setVisibility(View.GONE);
        displayMoreFields.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
