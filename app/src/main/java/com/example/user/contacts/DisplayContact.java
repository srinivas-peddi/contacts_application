package com.example.user.contacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayContact extends AppCompatActivity implements EmailAdapter.EmailClickListener,NumberAdapter.NumberClickListener
{

    private static final int EDIT_CONTACT = 20;
    TextView mAddressText;
    TextView mWebsiteText;
    NumberAdapter mNumberAdapter;
    EmailAdapter mEmailAdapter;
    ContactPOJO mContactPOJO;
    Toolbar mToolbar;
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
        mAddressText = findViewById(R.id.address_display);
        mWebsiteText = findViewById(R.id.website_display);
        mContactImage= findViewById(R.id.expandedImage);
        RecyclerView numberRecycler = findViewById(R.id.number_recycler_view);
        numberRecycler.setHasFixedSize(true);
        numberRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNumberAdapter = new NumberAdapter(mContactPOJO.getContactNumber(),mContactPOJO.getNumberType());
        numberRecycler.setAdapter(mNumberAdapter);
        RecyclerView emailRecycler = findViewById(R.id.email_recycler_view);
        emailRecycler.setHasFixedSize(true);
        emailRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEmailAdapter = new EmailAdapter(mContactPOJO.getEMailId());
        emailRecycler.setAdapter(mEmailAdapter);

        if(mContactPOJO.getPictureUri()!=null && !mContactPOJO.getPictureUri().equals(""))
        {
            mContactImage.setImageURI(null);
            mContactImage.setImageURI(Uri.parse(mContactPOJO.getPictureUri()));
            mContactImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            mContactImage.setImageResource(R.drawable.user);
        }

        if(mContactPOJO.getAddress()!=null && !mContactPOJO.getAddress().equals(""))
        {
            mAddressText.setText(mContactPOJO.getAddress());
        }
        else
        {
            findViewById(R.id.address_layout).setVisibility(View.GONE);
        }

        if(mContactPOJO.getWebsite()!=null && !mContactPOJO.getWebsite().equals(""))
        {
            mWebsiteText.setText(mContactPOJO.getWebsite());
        }
        else
        {
            findViewById(R.id.website_layout).setVisibility(View.GONE);
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

    public void dialNumber(String pContactNumber)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+ pContactNumber));
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(new Intent(intent));
        }
        else
        {
            Toast.makeText(this,"No Dialer found to Dial Number",Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMail(String pEmail)
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

    public void openLocation(View view)
    {
        Intent mapIntent= new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(Uri.parse("geo:0,0?q="+mContactPOJO.getAddress()));
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void openBrowser(View view)
    {
        String webAddress= mContactPOJO.getWebsite();
        if(!webAddress.startsWith("http://") && webAddress.startsWith("https://"))
        {
            webAddress="http://"+webAddress;
        }
        Intent openUrlIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(webAddress));
        if(openUrlIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(openUrlIntent);
        }
        else
        {
            Toast.makeText(this,"No App found to Open Link",Toast.LENGTH_SHORT).show();
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
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

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
class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder>
{
    ArrayList<String> mNumber;
    ArrayList<Integer> mNumberType;
    NumberClickListener mListener;

    NumberAdapter(ArrayList<String> pNumber, ArrayList<Integer> pNumberType)
    {
        mNumber=pNumber;
        mNumberType=pNumberType;
    }
    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i)
    {
        LinearLayout numberLayout=(LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phone_number_items,viewGroup,false);
        numberLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mListener.dialNumber(mNumber.get(i));
            }
        });
        return new NumberViewHolder(numberLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder numberViewHolder, int i)
    {
        TextView numberText= numberViewHolder.mLinearLayout.findViewById(R.id.number_display_text);
        numberText.setText(mNumber.get(i));
        TextView numberType= numberViewHolder.mLinearLayout.findViewById(R.id.number_type_display);
        switch (mNumberType.get(i))
        {
            case 1:
                numberType.setText("Home");
                break;

            case 2:
                numberType.setText("Mobile");
                break;

            case 3:
                numberType.setText("Work");
                break;

            case 4:
                numberType.setText("Work Fax");
                break;

            case 5:
                numberType.setText("Home Fax");
                break;

            case 6:
                numberType.setText("Pager");
                break;

            default:
                mNumberType.set(i,6);
                numberType.setText("Other");
                break;
        }
    }

    @Override
    public int getItemCount()
    {
        if(mNumber!=null)
        {
            return mNumber.size();
        }
        else
        {
            return 0;
        }
    }


    static class NumberViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout mLinearLayout;
        public NumberViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mLinearLayout=(LinearLayout) itemView;
        }
    }

    public interface NumberClickListener
    {
        public void dialNumber(String pNumber);
    }
}
class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder>
{
    ArrayList<String> mEmail;

    EmailClickListener mListener;

    EmailAdapter(ArrayList<String> pEmail)
    {
        mEmail=pEmail;
    }
    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i)
    {
        LinearLayout numberLayout=(LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.email_item,viewGroup,false);
        numberLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                mListener.sendMail(mEmail.get(i));
            }
        });
        return new EmailViewHolder(numberLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder emailViewHolder, int i)
    {
        TextView emailText= emailViewHolder.mLinearLayout.findViewById(R.id.email_display_text);
        emailText.setText(mEmail.get(i));
    }

    @Override
    public int getItemCount()
    {
        if(mEmail!=null)
        {
            return mEmail.size();
        }
        else
        {
            return 0;
        }
    }


    static class EmailViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout mLinearLayout;
        public EmailViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mLinearLayout=(LinearLayout) itemView;
        }
    }

    public interface EmailClickListener
    {
        public void sendMail(String pEmail);
    }
}