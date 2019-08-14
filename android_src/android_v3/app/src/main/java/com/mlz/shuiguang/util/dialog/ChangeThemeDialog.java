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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.widget.button.NJButton;

import java.util.ArrayList;

/**
 * Created by dehualai on 1/21/17.
 */

public class ChangeThemeDialog {


    Context mContext = null;
    Dialog dialog = null;
    View view;

    TextView titleView;
    TextView messageView;

    /*
    RelativeLayout layoutOption1;
    ImageView majorColorOption1;
    ImageView minorColorOption1;
    Button buttonOption1;

    RelativeLayout layoutOption2;
    ImageView majorColorOption2;
    ImageView minorColorOption2;
    Button buttonOption2;

    RelativeLayout layoutOption3;
    ImageView majorColorOption3;
    ImageView minorColorOption3;
    Button buttonOption3;

    RelativeLayout layoutOption4;
    ImageView majorColorOption4;
    ImageView minorColorOption4;
    Button buttonOption4;

    RelativeLayout layoutOption5;
    ImageView majorColorOption5;
    ImageView minorColorOption5;
    Button buttonOption5;

    RelativeLayout layoutOption6;
    ImageView majorColorOption6;
    ImageView minorColorOption6;
    Button buttonOption6;

    RelativeLayout layoutOption7;
    ImageView majorColorOption7;
    ImageView minorColorOption7;
    Button buttonOption7;

    RelativeLayout layoutOption8;
    ImageView majorColorOption8;
    ImageView minorColorOption8;
    Button buttonOption8;

    RelativeLayout layoutOption9;
    ImageView majorColorOption9;
    ImageView minorColorOption9;
    Button buttonOption9;
    */

    ArrayList<RelativeLayout> relativeLayouts = new ArrayList<RelativeLayout>();
    ArrayList<ImageView> majorColorViews = new ArrayList<ImageView>();
    ArrayList<ImageView> minorColorViews = new ArrayList<ImageView>();
    ArrayList<Button> buttons = new ArrayList<Button>();


    NJButton button1;
    NJButton button2;


    ButtonActionHandler handler;


    public ChangeThemeDialog(Context context) {
        mContext = context;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        view = inflater.inflate(R.layout.widget_dialog_change_theme_color, null);
        assignViews();
        addActionToViews();
    }

    private void assignViews(){
        titleView = (TextView) view.findViewById(R.id.title);
        messageView = (TextView) view.findViewById(R.id.message);

        button1 = (NJButton) view.findViewById(R.id.button1);
        button2 = (NJButton) view.findViewById(R.id.button2);

        relativeLayouts = new ArrayList<RelativeLayout>();
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_1_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_2_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_3_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_4_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_5_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_6_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_7_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_8_layout));
        relativeLayouts.add((RelativeLayout)view.findViewById(R.id.option_9_layout));

        majorColorViews = new ArrayList<ImageView>();
        majorColorViews.add((ImageView)view.findViewById(R.id.option_1_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_2_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_3_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_4_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_5_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_6_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_7_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_8_major_color));
        majorColorViews.add((ImageView)view.findViewById(R.id.option_9_major_color));

        minorColorViews = new ArrayList<ImageView>();
        minorColorViews.add((ImageView)view.findViewById(R.id.option_1_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_2_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_3_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_4_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_5_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_6_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_7_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_8_minor_color));
        minorColorViews.add((ImageView)view.findViewById(R.id.option_9_minor_color));


        buttons = new ArrayList<Button>();
        buttons.add((Button)view.findViewById(R.id.option_1_button));
        buttons.add((Button)view.findViewById(R.id.option_2_button));
        buttons.add((Button)view.findViewById(R.id.option_3_button));
        buttons.add((Button)view.findViewById(R.id.option_4_button));
        buttons.add((Button)view.findViewById(R.id.option_5_button));
        buttons.add((Button)view.findViewById(R.id.option_6_button));
        buttons.add((Button)view.findViewById(R.id.option_7_button));
        buttons.add((Button)view.findViewById(R.id.option_8_button));
        buttons.add((Button)view.findViewById(R.id.option_9_button));


    }


    private void addActionToViews(){

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(handler!=null){
                    handler.button1Pressed();
                }
                dismiss();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handler != null) {
                    handler.button2Pressed();
                }
                dismiss();
            }
        });

        refreshOptions();
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

    private void refreshOptions(){
        for (int i = 0; i < Global.themeColorOptions.size(); i ++){
            ThemeColor themeColor = Global.themeColorOptions.get(i);
            themeColor.setOptionID(i);
            themeColor.setLayout(relativeLayouts.get(i), mContext);
            themeColor.setMajorColorView(majorColorViews.get(i), mContext);
            themeColor.setMinorColorView(minorColorViews.get(i), mContext);
            themeColor.setButton(buttons.get(i), new ThemeColor.ButtonHandler() {
                @Override
                public void onButtonPressed() {
                    refreshOptions();
                }
            });
        }
        Log.e("show", "active theme " + Global.activeTheme);
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