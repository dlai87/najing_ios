package com.mlz.shuiguang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.login.LoginActivity;
import com.mlz.shuiguang.login.LoginActivity2;
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


    String[] PERMISSIONS = {
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.BLUETOOTH_PRIVILEGED",
            "android.permission.INTERNET",
            "android.permission.ACCESS_WIFI_STATE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.READ_INTERNAL_STORAGE"};
    int REQUEST_PERMISSIONS = 200;

    private void requestVideoPermissions() {
        ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        break;
                    }
                }
                runSwitchAfterPermissionGranted();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mContext,permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }



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


        runSwitchAfterPermissionGranted();


    }

    private void runSwitchAfterPermissionGranted(){
        // initial database
        SQLiteDatabase db = DBAdapter.getDatabase(mContext);

        TBUser tbUser = new TBUser();
        ArrayList<UserData> users = tbUser.getUserList(db);
        Intent targetIntent = null;
        if (users == null || users.size() == 0){
            targetIntent = new Intent(mContext, LoginActivity2.class);
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
