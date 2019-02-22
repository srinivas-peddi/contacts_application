package com.example.user.contacts;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactPOJO implements Comparable<Object>,Serializable
{

    private int mId;
    private String mContactName;
    private ArrayList<String> mContactNumber;
    private ArrayList<String> mEMailId;
    private ArrayList<Integer> mNumberType;
    private String mPictureUri;
    private String mAddress;
    private String mWebsite;

    ContactPOJO(String pName)
    {
        this.mContactName=pName;
    }

    void setId(int pId)
    {
        mId=pId;
    }

    void setContactName(String pContactName)
    {
        mContactName = pContactName;
    }

    void setContactNumber(ArrayList<String> pContactNumber)
    {
        if(pContactNumber!=null)
        {
            mContactNumber=pContactNumber;
        }
    }

    void setEMailId(ArrayList<String> pEMailId)
    {
        if(pEMailId!=null)
        {
            mEMailId=pEMailId;
        }
    }

    void setNumberType(ArrayList<Integer> pNumberType)
    {
        if(pNumberType!=null)
        {
            mNumberType=pNumberType;
        }
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

    ArrayList<String> getContactNumber()
    {
        return mContactNumber;
    }

    ArrayList<String> getEMailId()
    {
        return mEMailId;
    }

    String getPictureUri()
    {
        return mPictureUri;
    }

    ArrayList<Integer> getNumberType()
    {
        return mNumberType;
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
