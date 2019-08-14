package com.mlz.shuiguang.util.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.widget.button.NJButton;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dehualai on 1/29/17.
 */

public class DatePickerDialog {

    Context mContext = null;
    Dialog dialog = null;
    View view;

    TextView titleView;
    TextView messageView;

    DatePicker datePicker;

    NJButton button1;
    NJButton button2;


    ButtonActionHandler handler;




    public DatePickerDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_dialog_date_picker, null);
        assignViews();
    }

    private void assignViews(){
        titleView = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.message);
        datePicker = (DatePicker) view.findViewById(R.id.date_picker);


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


                Date selectDate = new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth());
                if (selectDate !=null){
                    TBUser tbUser = new TBUser();
                    ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));
                    UserData user = users.get(0);
                    user.setDate_of_birth(Util.convertTimeToString(selectDate, Util.FORMAT_DATE));
                    tbUser.smartInsert(DBAdapter.getDatabase(mContext), user);
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
