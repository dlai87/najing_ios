package com.mlz.shuiguang.transmission.task;

import android.app.backup.BackupManager;
import android.content.Context;
import android.util.Log;

import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.transmission.SyncTask;
import com.mlz.shuiguang.transmission.bmobObject.Detection;
import com.mlz.shuiguang.transmission.bmobObject.User;
import com.mlz.shuiguang.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dehualai on 2/20/17.
 */

public class UserInfoTask extends SyncTask {

    public final static int TASK_PUSH = 1;

    public UserInfoTask(Context context){
        super(context);
    }

    @Override
    public void sync(HashMap<Object, Object> params){
        Integer task = (Integer) params.get(PARAM_TASK);
        switch (task){
            case TASK_PUSH:
                push();
                break;
            default:
                push();
                break;
        }
    }


    private void push(){
        final TBUser tbUser = new TBUser();
        String transmissionStatus = tbUser.checkTransmissionStatus(DBAdapter.getDatabase(mContext));
        if (DBGlobal.TRANS_STATUS_UPDATED.equals(transmissionStatus)){
            ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));
            if (users.size() > 0 ){
                UserData user = users.get(0);

                User bmobUser = new User();
                bmobUser.setEmail(user.getEmail());
                bmobUser.setPhoneNumber(user.getPhone());
                bmobUser.setDate_of_birth(user.getDate_of_birth());
                bmobUser.setGender(user.getGender());
              //  bmobUser.setSkin_type();
              //  bmobUser.setPassword();

                bmobUser.update(user.getUserID(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            tbUser.updateTransmissionStatus(DBAdapter.getDatabase(mContext), DBGlobal.TRANS_STATUS_TRANSMITTED);
                            Log.e("show","userInfo update success ");
                        }else{
                            Log.e("show","userInfo update fails: " + e.getMessage());
                        }
                    }
                });
            }
        }

    }

}
