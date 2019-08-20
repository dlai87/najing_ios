package com.mlz.shuiguang.model;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.util.Global;

/**
 * Created by dehualai on 1/22/17.
 */

public class ThemeColor {

    int mainColor;
    int minorColor;
    int minorColor2;

    RelativeLayout layout;
    ImageView majorColorView;
    ImageView minorColorView;
    Button button;
    int optionID;


    public interface ButtonHandler{
        public void onButtonPressed();
    }

    public ThemeColor(int mainColor, int minorColor, int minorColor2){
        this.mainColor = mainColor;
        this.minorColor = minorColor;
        this.minorColor2 = minorColor2;
    }

    public int getMainColor(Context context) {
        return context.getResources().getColor(mainColor);
    }

    public int getMinorColor(Context context) {
        return context.getResources().getColor(minorColor);
    }

    public int getMinorColor2(Context context) {
        return context.getResources().getColor(minorColor2);
    }

    public RelativeLayout getLayout() {
        return layout;
    }

    public void setOptionID(int optionID) {
        this.optionID = optionID;
    }

    public void setLayout(RelativeLayout layout, Context context) {
        if (Global.activeTheme == optionID){
            layout.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else{
            layout.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        this.layout = layout;
    }


    public void setMajorColorView(ImageView majorColorView, Context context) {
        majorColorView.setBackgroundColor(context.getResources().getColor(mainColor));
        this.majorColorView = majorColorView;
    }


    public void setMinorColorView(ImageView minorColorView, Context context) {
        minorColorView.setBackgroundColor(context.getResources().getColor(minorColor));
        this.minorColorView = minorColorView;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button, final ButtonHandler handler) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.activeTheme = optionID;
                if (handler!=null) handler.onButtonPressed();
            }
        });
        this.button = button;
    }



}
