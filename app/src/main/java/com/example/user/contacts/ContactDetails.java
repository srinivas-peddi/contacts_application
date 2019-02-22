package com.example.user.contacts;

public class ContactDetails
{
    private int mId;
    private String mNumberOrEmail;
    private int mNumberOrEmailType;
    private int mDetailType;

    public void setmNumberOrEmail(String mNumberOrEmail)
    {
        this.mNumberOrEmail = mNumberOrEmail;
    }

    public void setmDetailType(int mDetailType)
    {
        this.mDetailType = mDetailType;
    }

    public void setmNumberOrEmailType(int mNumberOrEmailType)
    {
        this.mNumberOrEmailType = mNumberOrEmailType;
    }

    public int getmId()
    {
        return mId;
    }

    public String getmNumberOrEmail()
    {
        return mNumberOrEmail;
    }

    public int getmNumberOrEmailType()
    {
        return mNumberOrEmailType;
    }

    public int getmDetailType()
    {
        return mDetailType;
    }

    @Override
    public boolean equals(Object obj)
    {
        ContactDetails detail=null;
        if(obj instanceof ContactPOJO)
        {
            detail=(ContactDetails) obj;
        }
        if(obj==null || detail==null)
            return false;
        else
            return (this.mId==(detail.mId));
    }
}
