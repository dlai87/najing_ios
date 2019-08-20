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

@DynamoDBTable(tableName = "shuiguang-mobilehub-1086816736-detection")

public class DetectionDO {
    private String _detectionId;
    private String _bodyArea;
    private String _dateTime;
    private String _nursingStatus;
    private String _userId;
    private String _value;

    @DynamoDBHashKey(attributeName = "detection_id")
    @DynamoDBAttribute(attributeName = "detection_id")
    public String getDetectionId() {
        return _detectionId;
    }

    public void setDetectionId(final String _detectionId) {
        this._detectionId = _detectionId;
    }
    @DynamoDBAttribute(attributeName = "body_area")
    public String getBodyArea() {
        return _bodyArea;
    }

    public void setBodyArea(final String _bodyArea) {
        this._bodyArea = _bodyArea;
    }
    @DynamoDBAttribute(attributeName = "date_time")
    public String getDateTime() {
        return _dateTime;
    }

    public void setDateTime(final String _dateTime) {
        this._dateTime = _dateTime;
    }
    @DynamoDBAttribute(attributeName = "nursing_status")
    public String getNursingStatus() {
        return _nursingStatus;
    }

    public void setNursingStatus(final String _nursingStatus) {
        this._nursingStatus = _nursingStatus;
    }
    @DynamoDBAttribute(attributeName = "user_id")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "value")
    public String getValue() {
        return _value;
    }

    public void setValue(final String _value) {
        this._value = _value;
    }

}
