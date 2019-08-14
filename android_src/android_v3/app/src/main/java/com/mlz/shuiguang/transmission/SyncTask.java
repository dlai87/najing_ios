package com.mlz.shuiguang.transmission;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by dehualai on 1/13/17.
 */

public abstract class SyncTask {

    public static final String PARAM_TASK = "task";

    protected Context mContext;
    protected SyncTaskHandler handler;

    public void setHandler(SyncTaskHandler handler){
        this.handler = handler;
    }

    public SyncTask(Context context){
        this.mContext = context;
    }

    public abstract void sync(HashMap<Object, Object> params);

}
