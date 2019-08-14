package com.mlz.shuiguang.database;

/**
 * Created by dehualai on 12/28/16.
 */

public class DBGlobal {

    public static String DB = "najing_db";
    public static  int DB_VERSION = 1;
    public static boolean DB_ON_SDCARD = true;

    public static String TRANS_STATUS_INSERTED = "inserted";
    public static String TRANS_STATUS_UPDATED = "updated";
    public static String TRANS_STATUS_TRANSMITTED = "transmitted";




    public static String COL_AUTO_ID = "_id";
    public static String COL_TRANSMISSION_STATUS = "transmission_status";

    public static String TABLE_USER = "User";
    public static String COL_USERNAME = "username";
    public static String COL_AVATAR = "avatar";
    public static String COL_GENDER = "gender";
    public static String COL_DATE_OF_BIRTH = "date_of_birth";
    public static String COL_PHONE_NUMBER = "phone_number";
    public static String COL_EMAIL = "email";
    public static String COL_USER_ID = "user_id";
    public static String COL_SKIN_TYPE = "skin_type";

    public static String TABLE_DETECTION = "detection";
    public static String COL_BODY_PART = "body_part";
    public static String COL_DETECT_DATE_TIME = "detect_date_time";
    public static String COL_VALUE = "value";
    public static String COL_NURSING_STATUS = "nursing_status";


}
