package com.android.androidtech.business.contact;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by yuchengluo on 2016/5/31.
 */
public class ContactInfo{
    private long contactId = -1;
    private String contactName,contactNum;
    public ContactInfo(long id,String name,String number){
        this.contactId = id;
        this.contactName = name;
        this.contactNum = number;
    }
    public void setName(String name){
        this.contactName = name;
    }
    public String getContactName(){
        return this.contactName;
    }
    public void setNumber(String number){
        this.contactNum = number;
    }
    public String getContactNum(){
        return this.contactNum;
    }
    public void setID(long id){
        this.contactId = id;
    }
    public long getContactId(){
        return contactId;
    }
}
