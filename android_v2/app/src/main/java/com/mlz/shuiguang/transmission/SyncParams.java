package com.mlz.shuiguang.transmission;

/**
 * Created by dehualai on 1/13/17.
 */

public class SyncParams {

    /**
     * Results & Message type
     */
    public static final int TYPE_SUCCESS = 9999;
    // registration_id or any id when registering
    // not exists in db
    public static final int TYPE_NO_MATCHING_AUTH = 5000;
    // missing some data in json
    public static final int TYPE_JSON_DATA_ERROR = 5001;
    // the task not exists in backend, wrong request
    public static final int TYPE_NO_MATCHING_TASK = 5002;
    // interrupted at the beginning of backend controller
    public static final int TYPE_INTERRUPTION = 5003;
    // User not exists in db (using wrong registration_id
    // or any wrong identication id when requests)
    public static final int TYPE_NO_MATCHING_USER = 5004;
    // or fatal exception in backend
    public static final int TYPE_RESCUED_ERROR = 5005;
    // like email already exists when registration
    public static final int TYPE_EMAIL_VALIDATION_FAIL = 5006;
    // some task not implemented in front end
    public static final int TYPE_UNKNOW_SYNC_TASK = 5007;
    // no internet connection(no 4g or wifi or server down)
    // or wrong request uri
    // front end exception/error captured
    public static final int TYPE_FRONT_END_ERROR = 5008;
    //registration_id already exists when registration
    public static final int TYPE_REG_VALIDATION_FAIL = 5009;
    //both email&reg id validation error
    public static final int TYPE_EMAIL_REG_VALIDATION_FAIL = 5010;
    //data already in db or file system, cannot override
    public static final int TYPE_DATA_ALREADY_EXISTS = 5011;

    public static final int TYPE_FILE_FAILED_SAVING = 5012;

}
