package com.mlz.shuiguang.transmission.bmobObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by dehualai on 1/14/17.
 */

public class User extends BmobUser {

    String gender;
    String date_of_birth;
    String phoneNumber;
   // String password;
    String skin_type;

    BmobFile avatar;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public String getSkin_type() {
        return skin_type;
    }

    public void setSkin_type(String skin_type) {
        this.skin_type = skin_type;
    }
}
