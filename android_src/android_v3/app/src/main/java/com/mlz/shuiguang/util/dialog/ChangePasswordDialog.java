package com.mlz.shuiguang.util.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mlz.shuiguang.MainActivity;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.transmission.SyncTaskHandler;
import com.mlz.shuiguang.transmission.bmobObject.User;
import com.mlz.shuiguang.transmission.task.AuthsTask;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.widget.button.NJButton;
import com.mlz.shuiguang.util.widget.dialog.NJProgressDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dehualai on 2/24/17.
 */

public class ChangePasswordDialog {

    Context mContext = null;
    Dialog dialog = null;
    View view;

    TextView titleView;
    TextView messageView;

    NJButton button1;
    NJButton button2;

    EditText currentPassword;
    EditText newPassword;
    EditText confirmNewPassword;


    ButtonActionHandler handler;




    public ChangePasswordDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_dialog_change_password, null);
        assignViews();
    }

    private void assignViews(){
        titleView = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.message);
        currentPassword = (EditText)view.findViewById(R.id.current_password_text_field);
        newPassword = (EditText)view.findViewById(R.id.new_password_text_field);
        confirmNewPassword = (EditText)view.findViewById(R.id.confirm_new_password_text_field);

        button1 = (NJButton) view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(handler!=null){
                    handler.button1Pressed();
                }
                dismiss();

            }
        });
        button2 = (NJButton) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentP = currentPassword.getText().toString();
                final String newP = newPassword.getText().toString();
                final String cNewP = confirmNewPassword.getText().toString();

                if (newP.equals(cNewP)){
                    final NJProgressDialog progressDialog = NJProgressDialog.show(mContext);

                    UserData userData = queryUserDataFromDB();


                    AuthsTask authsTask = new AuthsTask(mContext);
                    HashMap params = new HashMap();
                    params.put(AuthsTask.PARAM_TASK, AuthsTask.TASK_LOGIN);
                    params.put(AuthsTask.PARAM_AUTH_TYPE, AuthsTask.TYPE_PWD);
                    params.put(AuthsTask.PARAM_USERNAME, userData.getUsername());
                    params.put(AuthsTask.PARAM_PASSWORD, currentP);

                    authsTask.setHandler(new SyncTaskHandler() {

                        @Override
                        public void onSuccess(Object object) {

                            progressDialog.dismiss();
                            BmobUser.updateCurrentUserPassword(currentP, newP, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        AlertDialog alertDialog = new AlertDialog(mContext);
                                        alertDialog.setTitleView(mContext.getResources().getString(R.string.success));
                                        alertDialog.setMessageView(mContext.getResources().getString(R.string.you_can_start_using_your_new_password));
                                        alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                                        alertDialog.showDialog();
                                    }else{
                                        AlertDialog alertDialog = new AlertDialog(mContext);
                                        alertDialog.setTitleView(mContext.getResources().getString(R.string.failure));
                                        alertDialog.setMessageView(mContext.getResources().getString(R.string.please_try_again));
                                        alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                                        alertDialog.showDialog();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailure(String message, int errorCode) {
                            progressDialog.dismiss();
                            AlertDialog alertDialog = new AlertDialog(mContext);
                            alertDialog.setTitleView(mContext.getResources().getString(R.string.failure));
                            alertDialog.setMessageView(mContext.getResources().getString(R.string.please_try_again));
                            alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                            alertDialog.showDialog();
                        }
                    });
                    authsTask.sync(params);


                }else{
                    AlertDialog alertDialog = new AlertDialog(mContext);
                    alertDialog.setTitleView(null);
                    alertDialog.setMessageView(mContext.getResources().getString(R.string.new_password_not_match));
                    alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                    alertDialog.showDialog();
                }


                if (handler != null) {
                    handler.button2Pressed();
                }
                dismiss();
            }
        });


    }

    private void refreshViews(){
        ThemeColor theme = Util.getActiveTheme();
        button1.updateTheme(theme.getMinorColor(mContext),
                theme.getMinorColor(mContext),
                theme.getMinorColor2(mContext));
        button2.updateTheme(theme.getMinorColor(mContext),
                theme.getMinorColor(mContext),
                theme.getMinorColor2(mContext));
    }


    /**
     *  set title,  null will show no title
     * */
    public void setTitleView(String title){
        if (title != null){
            titleView.setText(title);
            titleView.setVisibility(View.VISIBLE);
        }else{
            titleView.setVisibility(View.GONE);
        }
    }

    /**
     *  set message,  null will show no message
     * */
    public void setMessageView(String message){
        if (message != null){
            messageView.setText(message);
            messageView.setVisibility(View.VISIBLE);
        }else{
            messageView.setVisibility(View.GONE);
        }
    }

    /**
     *  set buttons
     * */
    public void setButtons(String buttonTitle1, String buttonTitle2, ButtonActionHandler handler){
        if (buttonTitle1!=null){
            button1.setText(buttonTitle1);
        }else {
            button1.setVisibility(View.GONE);
        }
        if (buttonTitle2!=null){
            button2.setText(buttonTitle2);
            button2.setVisibility(View.VISIBLE);
        }else {
            button2.setVisibility(View.GONE);
        }
        this.handler = handler;

    }

    public void showDialog() {

        dialog = new Dialog(mContext);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        refreshViews();
        dialog.show();
    }

    public void dismiss()
    {
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }


    private UserData queryUserDataFromDB(){
        TBUser tbUser = new TBUser();
        ArrayList<UserData> userList = tbUser.getUserList(DBAdapter.getDatabase(mContext));
        if (userList.size() > 0 ){
            UserData userData = userList.get(0);
            return userData;
        }
        return null;
    }
}
