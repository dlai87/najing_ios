package com.mlz.shuiguang.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

import cn.bmob.v3.Bmob;

/**
 * Created by dehualai on 1/8/17.
 */

public class LoginActivity extends Activity implements IMainAppHandler {


    String TAG = "LoginActivity";
    FragmentUtil fragmentUtil;
    Context mContext;

    @Override
    public void onStart(){
        super.onStart();
        mContext = this;
        DBAdapter.getDatabase(mContext);
        fragmentUtil = new FragmentUtil(this);
        fragmentUtil.showFragment(new LoginFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initial Bmob SDK
        //Bmob.initialize(this, Global.Bmob_AppId);

    }


    // implement interface IMainAppHandler
    public int getMainFragmentID(){
        return R.id.loginFragment;
    }

    public void onBackPressed(){

    }

    public void refreshTabBar(){}

}
