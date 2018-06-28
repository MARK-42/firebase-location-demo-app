package com.projects.rtk154.firebasedemoapp;

/**
 * Created by rtk154 on 27/6/18.
 */

public class UserInfo {
    public String mName,mAddress;

    public UserInfo(){}
    public UserInfo(String Name, String Address) {
        this.mName = Name;
        this.mAddress = Address;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

}
