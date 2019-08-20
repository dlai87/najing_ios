package com.mlz.shuiguang.model;

/**
 * Created by dehualai on 12/28/16.
 */

public class UserData {
    // required
    private String username;
    private String user_id;
    // optional
    private String avatar;
    private String gender;
    private String date_of_birth;
    private String phone;
    private String email;
    private String skin_type;
    private String google;
    private String wechat;
    private String facebook;

    public UserData(UserBuilder builder){
        this.username = builder.username;
        this.user_id = builder.user_id;
        this.avatar = builder.avatar;
        this.gender = builder.gender;
        this.date_of_birth = builder.date_of_birth;
        this.phone = builder.phone;
        this.email = builder.email;
        this.skin_type = builder.skin_type;
        this.wechat = builder.wechat;
        this.google = builder.google;
        this.facebook = builder.facebook;
    }

    /**
     *  Builder Design Pattern
     * */
    public static class UserBuilder {
        private String username;
        private String user_id;
        private String gender;
        private String date_of_birth;
        private String phone;
        private String email;
        private String avatar;
        private String skin_type;
        private String google;
        private String facebook;
        private String wechat;

        public UserBuilder(String username, String user_id) {
            this.username = username;
            this.user_id = user_id;
        }
        public UserBuilder gender(String gender){
            this.gender = gender;
            return this;
        }
        public UserBuilder date_of_birth(String date){
            this.date_of_birth = date;
            return this;
        }
        public UserBuilder phone(String phone){
            this.phone = phone;
            return this;
        }
        public UserBuilder email(String email){
            this.email = email;
            return this;
        }
        public UserBuilder avatar(String avatar){
            this.avatar = avatar;
            return this;
        }

        public  UserBuilder skin_type(String skin_type){
            this.skin_type = skin_type;
            return this;
        }

        public UserBuilder google(String google){
            this.google = google;
            return this;
        }

        public UserBuilder wechat(String wechat){
            this.wechat = wechat;
            return this;
        }

        public UserBuilder facebook(String facebook){
            this.facebook = facebook;
            return this;
        }
        public UserData build(){
            return new UserData(this);
        }



    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {return user_id; }

    public String getAvatar(){ return avatar; }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkin_type() {
        return skin_type;
    }

    public void setSkin_type(String skin_type) {
        this.skin_type = skin_type;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}

