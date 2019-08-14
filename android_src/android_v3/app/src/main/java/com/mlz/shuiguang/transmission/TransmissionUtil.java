package com.mlz.shuiguang.transmission;

import android.content.Context;

import com.mlz.shuiguang.transmission.task.DetectionsHistoryTask;
import com.mlz.shuiguang.transmission.task.UserInfoTask;
import com.mlz.shuiguang.util.Global;

import java.util.HashMap;

/**
 * Created by dehualai on 2/12/17.
 */

public class TransmissionUtil {

    public static void syncAll(Context mContext){
        syncDetection(mContext);
        syncUserInfo(mContext);
    }


    public static void syncDetection(Context mContext){
        if (!Global.DEMO_MODE) {
            DetectionsHistoryTask detectionsHistoryTask = new DetectionsHistoryTask(mContext);
            HashMap params = new HashMap();
            // pull
            params.put(SyncTask.PARAM_TASK, DetectionsHistoryTask.TASK_PULL);
            detectionsHistoryTask.sync(params);
            params = new HashMap();
            // push
            params.put(SyncTask.PARAM_TASK, DetectionsHistoryTask.TASK_PUSH);
            detectionsHistoryTask.sync(params);
        }
    }

    public static void syncUserInfo(Context mContext){
       // if (!Global.DEMO_MODE){
            UserInfoTask userInfoTask = new UserInfoTask(mContext);
            HashMap params = new HashMap();
            params.put(SyncTask.PARAM_TASK, DetectionsHistoryTask.TASK_PUSH);
            userInfoTask.sync(params);
      //  }
    }

}
