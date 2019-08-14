package com.mlz.shuiguang.util.dialog;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.widget.button.NJButton;

/**
 * Created by dehualai on 2/19/17.
 */

public class DetectionSuccessDialog {


    Context mContext = null;
    Dialog dialog = null;
    View view;

    TextView titleView;
    TextView messageView;
    NJButton button1;


   // int DISMISS_IN_SEC = 4;



    public DetectionSuccessDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_detect_success, null);
        assignViews();
    }

    private void assignViews(){
        titleView = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.message);
        button1 = (NJButton) view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void refreshViews(){
        ThemeColor theme = Util.getActiveTheme();
            button1.updateTheme(theme.getMinorColor(mContext),
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

        /*
        final android.os.Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    dialog.dismiss();
                }catch (Exception e){

                }
            }
        }, DISMISS_IN_SEC*1000);
        */
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
