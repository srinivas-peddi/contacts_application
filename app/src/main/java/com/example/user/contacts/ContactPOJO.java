package com.example.user.contacts;

import java.io.Serializable;

public class ContactPOJO implements Comparable<Object>,Serializable
{

    private String mContactName;
    private String mContactNumber;
    private String mEMailId;
    private int mNumberType;
    private String mPictureUri;
    private String mAddress;
    private String mWebsite;


    public ContactPOJO()
    {
        super();
    }

    ContactPOJO(String pName, String pNumber, String pEMailId,int pNumberType,String pPictureUri,String pAddress, String pWebsite)
    {
        this.mContactName = pName;
        this.mContactNumber = pNumber;
        this.mEMailId = pEMailId;
        this.mNumberType=pNumberType;
        this.mPictureUri=pPictureUri;
        this.mAddress=pAddress;
        this.mWebsite=pWebsite;
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

    void setAddress(String pAddress)
    {
        this.mAddress=pAddress;
    }

    void setWebsite(String pWebsite)
    {
        this.mWebsite=pWebsite;
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

    public String getAddress()
    {
        return mAddress;
    }

    public String getWebsite()
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
