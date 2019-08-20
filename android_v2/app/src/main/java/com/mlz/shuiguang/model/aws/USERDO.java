package com.mlz.shuiguang.model.aws;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "shuiguang-mobilehub-1086816736-USER")

public class USERDO {
    private String _userId;
    private String _dateOfBirth;
    private String _email;
    private String _facebook;
    private String _gender;
    private String _google;
    private String _phone;
    private String _platform;
    private String _username;
    private String _wechat;

    @DynamoDBHashKey(attributeName = "userId")
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "date_of_birth")
    public String getDateOfBirth() {
        return _dateOfBirth;
    }

    public void setDateOfBirth(final String _dateOfBirth) {
        this._dateOfBirth = _dateOfBirth;
    }
    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return _email;
    }

    public void setEmail(final String _email) {
        this._email = _email;
    }
    @DynamoDBAttribute(attributeName = "facebook")
    public String getFacebook() {
        return _facebook;
    }

    public void setFacebook(final String _facebook) {
        this._facebook = _facebook;
    }
    @DynamoDBAttribute(attributeName = "gender")
    public String getGender() {
        return _gender;
    }

    public void setGender(final String _gender) {
        this._gender = _gender;
    }
    @DynamoDBAttribute(attributeName = "google")
    public String getGoogle() {
        return _google;
    }

    public void setGoogle(final String _google) {
        this._google = _google;
    }
    @DynamoDBAttribute(attributeName = "phone")
    public String getPhone() {
        return _phone;
    }

    public void setPhone(final String _phone) {
        this._phone = _phone;
    }
    @DynamoDBAttribute(attributeName = "platform")
    public String getPlatform() {
        return _platform;
    }

    public void setPlatform(final String _platform) {
        this._platform = _platform;
    }
    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return _username;
    }

    public void setUsername(final String _username) {
        this._username = _username;
    }
    @DynamoDBAttribute(attributeName = "wechat")
    public String getWechat() {
        return _wechat;
    }

    public void setWechat(final String _wechat) {
        this._wechat = _wechat;
    }

}
