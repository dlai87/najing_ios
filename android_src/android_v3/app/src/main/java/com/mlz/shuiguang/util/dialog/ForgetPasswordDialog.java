package com.mlz.shuiguang.util.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.widget.button.NJButton;

import java.util.ArrayList;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dehualai on 2/25/17.
 */

public class ForgetPasswordDialog {

    Context mContext = null;
    Dialog dialog = null;
    View view;

    TextView titleView;
    TextView messageView;

    NJButton button1;
    NJButton button2;

    EditText emailInput;


    ButtonActionHandler handler;




    public ForgetPasswordDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_dialog_text_input, null);
        assignViews();
    }

    private void assignViews(){
        titleView = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.message);
        emailInput = (EditText)view.findViewById(R.id.text_input);

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
                final String email = emailInput.getText().toString().trim().replace(" ", "").toLowerCase();
                if (email !=null && Util.validateEmailFormat(email)){
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //Log.e("show", "重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                                AlertDialog alertDialog = new AlertDialog(mContext);
                                alertDialog.setTitleView(mContext.getResources().getString(R.string.success));
                                alertDialog.setMessageView(mContext.getResources().getString(R.string.please_check_your_email_and_reset_your_password));
                                alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                                alertDialog.showDialog();
                            }else{
                                //Log.e("show", "失败:" + e.getMessage() + " | " +e.getErrorCode());
                                if (e.getErrorCode()==205){
                                    AlertDialog alertDialog = new AlertDialog(mContext);
                                    alertDialog.setTitleView(mContext.getResources().getString(R.string.failure));
                                    alertDialog.setMessageView(mContext.getResources().getString(R.string.no_user_found_with_email) + email);
                                    alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                                    alertDialog.showDialog();
                                }else{
                                    AlertDialog alertDialog = new AlertDialog(mContext);
                                    alertDialog.setTitleView(mContext.getResources().getString(R.string.failure));
                                    alertDialog.setMessageView(mContext.getResources().getString(R.string.please_try_again) );
                                    alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                                    alertDialog.showDialog();
                                }
                            }
                        }
                    });

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
}
