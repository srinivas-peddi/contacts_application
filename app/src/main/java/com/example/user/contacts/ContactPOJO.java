package com.example.user.contacts;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactPOJO implements Comparable<Object>,Serializable
{

    private int mId;
    private String mContactName;
    ArrayList<ContactDetails> mDetails;
    private String mPictureUri;
    private String mAddress;
    private String mWebsite;

    ContactPOJO(String pName)
    {
        this.mContactName=pName;
        this.mDetails=new ArrayList<>();
        this.mPictureUri="";
        this.mAddress="";
        this.mWebsite="";
    }

    void setId(int pId)
    {
        mId=pId;
    }

    void setContactName(String pContactName)
    {
        mContactName = pContactName;
    }

    public void setmDetails(ArrayList<ContactDetails> pDetails)
    {
        mDetails.addAll(pDetails);
    }

    void setPictureUri(String pPictureUri)
    {
        mPictureUri = pPictureUri;
    }

    void setAddress(String pAddress)
    {
        mAddress=pAddress;
    }

    void setWebsite(String pWebsite)
    {
        mWebsite=pWebsite;
    }

    String getContactName()
    {
        return mContactName;
    }

    String getPictureUri()
    {
        return mPictureUri;
    }

    public ArrayList<ContactDetails> getmDetails()
    {
        return mDetails;
    }

    String getAddress()
    {
        return mAddress;
    }

    String getWebsite()
    {
        return mWebsite;
    }

    @Override
    public boolean equals(Object obj)
    {
        ContactPOJO contact=null;
        if(obj instanceof ContactPOJO)
        {
            contact=(ContactPOJO)obj;
        }
        if(obj==null || contact==null)
            return false;
        else
            return (this.mContactName.equalsIgnoreCase(contact.mContactName));
    }

    @Override
    public int compareTo(Object o)
    {
        ContactPOJO c=(ContactPOJO) o;
        return this.mContactName.compareToIgnoreCase(c.mContactName);
    }
}
