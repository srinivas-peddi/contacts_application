package com.example.user.contacts;

import java.io.Serializable;

public class ContactPOJO implements Comparable<Object>,Serializable
{
    private String mContactName;
    private String mContactNumber;
    private String mEMailId;
    private int mNumberType;


    public ContactPOJO(String pName, String pNumber, String pEMailId,int pNumberType)
    {
        mContactName = pName;
        mContactNumber = pNumber;
        mEMailId = pEMailId;
        mNumberType=pNumberType;
    }

    public void setNumberType(int pNumberType)
    {
        this.mNumberType = pNumberType;
    }

    public void setmContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    public void setmEMailId(String mEMailId) {
        this.mEMailId = mEMailId;
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

    public int getNumberType() {
        return mNumberType;
    }

    public String toString()
    {
        return this.mContactName+"\t"+this.mContactNumber+"\t"+this.getEMailId();
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
