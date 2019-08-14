package com.mlz.shuiguang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.login.LoginActivity;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Global;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;

/**
 * Created by dehualai on 1/17/17.
 */

public class ActivitySwitch extends Activity {

    String TAG = ActivitySwitch.class.getSimpleName();
    private Context mContext = null;

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onStart() {
        super.onStart();
        // initial Bmob SDK
        Bmob.initialize(this, Global.Bmob_AppId);

        mContext = this;
        // initial database
        SQLiteDatabase db = DBAdapter.getDatabase(mContext);

        TBUser tbUser = new TBUser();
        ArrayList<UserData> users = tbUser.getUserList(db);
        Intent targetIntent = null;
        if (users == null || users.size() == 0){
            targetIntent = new Intent(mContext, LoginActivity.class);
        }else{
            targetIntent = new Intent(mContext, MainActivity.class);
        }

        if(targetIntent!=null){
            targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            mContext.startActivity(targetIntent);
        }

    }
}
