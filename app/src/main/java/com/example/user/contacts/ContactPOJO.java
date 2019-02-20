package com.example.user.contacts;

import java.io.Serializable;

public class ContactPOJO implements Comparable<Object>,Serializable
{

    private String mContactName;
    private String mContactNumber;
    private String mEMailId;
    private int mNumberType;
    private String mPictureUri;


    ContactPOJO()
    {

    }

    ContactPOJO(String pName, String pNumber, String pEMailId,int pNumberType,String pPictureUri)
    {
        mContactName = pName;
        mContactNumber = pNumber;
        mEMailId = pEMailId;
        mNumberType=pNumberType;
        mPictureUri=pPictureUri;
    }

    void setNumberType(int pNumberType)
    {
        this.mNumberType = pNumberType;
    }

    void setmContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    void setmEMailId(String mEMailId) {
        this.mEMailId = mEMailId;
    }

    void setPictureUri(String pPictureUri)
    {
        this.mPictureUri = pPictureUri;
    }

    String getContactName()
    {
        return this.mContactName;
    }

    String getContactNumber()
    {
        return mContactNumber;
    }

    String getEMailId()
    {
        return mEMailId;
    }

    String getPictureUri()
    {
        return mPictureUri;
    }

    int getNumberType() {
        return mNumberType;
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
