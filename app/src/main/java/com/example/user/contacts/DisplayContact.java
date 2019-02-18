package com.example.user.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayContact extends AppCompatActivity
{

    private static final int EDIT_CONTACT = 20;
    TextView mNameText;
    TextView mNumberText;
    TextView mEmailText;
    ContactPOJO mContactPOJO;
    Toolbar mToolbar;
    TextView mNumberTypeText;
    ImageView mContactImage;
    Menu menu;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_contact_collapse);
        mToolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.toolbar_layout);
        setSupportActionBar(mToolbar);
        mContactPOJO =(ContactPOJO) getIntent().getExtras().getSerializable("contactobj");
        collapsingToolbarLayout.setTitle(mContactPOJO.getContactName());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mToolbar.setTitle(mContactPOJO.getContactName());
        mToolbar.setTitleTextColor(Color.WHITE);
        mNumberText =findViewById(R.id.number_display__text);
        mEmailText =findViewById(R.id.email_display_text);
        mNumberTypeText = findViewById(R.id.number_type_display);
        mContactImage= findViewById(R.id.expandedImage);
        if(!mContactPOJO.getContactNumber().equals(""))
        {
            mNumberText.setText(mContactPOJO.getContactNumber());
            switch (mContactPOJO.getNumberType())
            {
                case 1:
                    mNumberTypeText.setText("Home");
                    break;

                case 2:
                    mNumberTypeText.setText("Mobile");
                    break;

                case 3:
                    mNumberTypeText.setText("Work");
                    break;

                case 4:
                    mNumberTypeText.setText("Work Fax");
                    break;

                case 5:
                    mNumberTypeText.setText("Home Fax");
                    break;

                case 6:
                    mNumberTypeText.setText("Pager");
                    break;

                default:
                    mNumberTypeText.setText("Other");
                    break;

            }
        }
        else
        {
            LinearLayout linearLayout= findViewById(R.id.number_layout);
            linearLayout.setVisibility(View.GONE);
        }
        if(!mContactPOJO.getEMailId().equals(""))
        {
            mEmailText.setText(mContactPOJO.getEMailId());
        }
        else
        {
            LinearLayout linearLayout= findViewById(R.id.email_layout);
            linearLayout.setVisibility(View.GONE);
        }
        if(!mContactPOJO.getPictureUri().equals(""))
        {
            mContactImage.setImageURI(null);
            mContactImage.setImageURI(Uri.parse(mContactPOJO.getPictureUri()));
            mContactImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            mContactImage.setImageResource(R.drawable.user);
        }
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_home_arrow);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu=menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_delete_actionbar_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_edit:
                editContact();
                break;
            case R.id.action_delete:
                deleteContact();
                break;
            default:
                setResult(RESULT_CANCELED);
                finish();
        }
        return true;
    }

    public void dialNumber(View view)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ mContactPOJO.getContactNumber()));
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(new Intent(intent));
        }
        else
        {
            Toast.makeText(this,"No Dialer found to Dial Number",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMail(View view)
    {
        Intent intent=new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+ mContactPOJO.getEMailId()));
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"No Mail Application found To Send Mail",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteContact()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete the Contact?");
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface arg0, int arg1)
                {
                    setResult(RESULT_OK,new Intent().putExtra("contactobj",mContactPOJO));
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

    /*
    Comment 5
     */

    public void editContact()
    {
        startActivityForResult(new Intent(this,AddContact.class).putExtra("contactobj",mContactPOJO).putExtra("position",getIntent().getExtras().getInt("position")),EDIT_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(resultCode==RESULT_FIRST_USER)
        {
            Intent intent=new Intent().putExtra("oldName",data.getExtras().getString("oldName")).putExtra("contactobj",data.getExtras().getSerializable("contactobj"));
            setResult(RESULT_FIRST_USER,intent);
            finish();
        }
        else if(resultCode==RESULT_CANCELED)
        {
            Toast.makeText(this,"Changes Discarded",Toast.LENGTH_SHORT).show();
        }
    }
}
