package com.mlz.shuiguang.transmission.bmobObject;

import cn.bmob.v3.BmobObject;

/**
 * Created by dehualai on 2/12/17.
 */

public class Detection extends BmobObject{
    String value;
    String user_id;
    String nursing_status;
    String date_time;
    String body_part;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNursing_status() {
        return nursing_status;
    }

    public void setNursing_status(String nursing_status) {
        this.nursing_status = nursing_status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getBody_part() {
        return body_part;
    }

    public void setBody_part(String body_part) {
        this.body_part = body_part;
    }
}
