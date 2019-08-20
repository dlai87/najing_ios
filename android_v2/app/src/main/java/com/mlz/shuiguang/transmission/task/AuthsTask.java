package com.mlz.shuiguang.transmission.task;

import android.content.Context;
import android.util.Log;

import com.mlz.shuiguang.transmission.SyncTask;
import com.mlz.shuiguang.transmission.bmobObject.User;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;

import java.util.HashMap;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dehualai on 1/13/17.
 */

public class AuthsTask extends SyncTask {

    public static final int TASK_REGISTER = 0;
    public static final int TASK_LOGIN = 1;
    public static final String PARAM_AUTH_TYPE = "auth_type";
    public static final int TYPE_PWD = 0;
    public static final int TYPE_WECHAT = 1;
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";

    public AuthsTask(Context context){
        super(context);
    }

    @Override
    public void sync(HashMap<Object, Object> params) {
        Integer task = (Integer) params.get(PARAM_TASK);
        switch (task){
            case TASK_REGISTER:
                register(params);
                break;
            case TASK_LOGIN:
                login(params);
                break;
            default:
                break;
        }
    }

    private void register(HashMap<Object, Object> params){
        Integer type = (Integer) params.get(PARAM_AUTH_TYPE);
        switch (type){
            case TYPE_PWD:

                /*
                String username = (String)params.get(PARAM_USERNAME);
                String password = (String)params.get(PARAM_PASSWORD);
                String email = (String)params.get(PARAM_EMAIL);
                User user = new User();
                user.setUsername(username.replace(" ", "").toLowerCase());
                user.setPassword(password);
                user.setEmail(email.replace(" ", "").toLowerCase());
                user.signUp(new SaveListener<User>(){
                    @Override
                    public void done(User u, BmobException e) {
                        if(e==null){
                            Log.d("show", "添加数据成功，返回objectId为："+u.getObjectId());
                            if (handler!=null) handler.onSuccess(u);
                        }else{
                            Log.d("show", "创建数据失败：" + e.getMessage() + " code: " + e.getErrorCode());
                            if (handler!=null) handler.onFailure(e.getMessage(), e.getErrorCode());
                        }
                    }
                });

                */
                break;
            case TYPE_WECHAT:
                break;
            default:
                break;
        }
    }

    private void login(HashMap<Object, Object> params){
        Integer type = (Integer) params.get(PARAM_AUTH_TYPE);
        switch (type){
            case TYPE_PWD:

                /*
                String username = (String)params.get(PARAM_USERNAME);
                String password = (String)params.get(PARAM_PASSWORD);
                User user = new User();
                user.setUsername(username.replace(" ", "").toLowerCase());
                user.setPassword(password);
                user.login(new SaveListener<User>(){
                    @Override
                    public void done(User u, BmobException e) {
                        if(e==null){
                            Log.d("show", "登录成功，返回objectId为："+u.getObjectId());
                            if (handler!=null) handler.onSuccess(u);
                        }else{
                            Log.d("show", "登录失败：" + e.getMessage() + " code: " + e.getErrorCode());
                            if (handler!=null) handler.onFailure(e.getMessage(), e.getErrorCode());
                        }
                    }
                });

*/

                break;
            default:
                break;
        }
    }
}
