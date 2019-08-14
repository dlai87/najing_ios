package com.mlz.shuiguang.util;

import android.os.Environment;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.model.MessageData;
import com.mlz.shuiguang.model.ThemeColor;

import java.util.ArrayList;

/**
 * Created by dehualai on 12/28/16.
 */

public class Global {

    public final static String Bmob_AppId = "4774f926532739f0556d610cafe9ece3";

    public final static String FOLDER_APP_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.mlz.shuiguang/";
    public final static String AVATAR_PATH = FOLDER_APP_ROOT + "avatar.jpg";

    public static boolean DEMO_MODE = false;

    public static String APP_FONT =  "fonts/FZPWJW.TTF"; //"fonts/han_yi_zong_yi.ttf"; //

    public static int activeTheme = 0;
    public static ArrayList<ThemeColor> themeColorOptions = new ArrayList<ThemeColor>(){{
        add(new ThemeColor(R.color.theme_1_major_color,R.color.theme_1_minor_color, R.color.theme_1_minor_color_2 ));
        add(new ThemeColor(R.color.theme_2_major_color,R.color.theme_2_minor_color, R.color.theme_2_minor_color_2 ));
        add(new ThemeColor(R.color.theme_3_major_color,R.color.theme_3_minor_color, R.color.theme_3_minor_color_2 ));
        add(new ThemeColor(R.color.theme_4_major_color,R.color.theme_4_minor_color, R.color.theme_4_minor_color_2 ));
        add(new ThemeColor(R.color.theme_5_major_color,R.color.theme_5_minor_color, R.color.theme_5_minor_color_2 ));
        add(new ThemeColor(R.color.theme_6_major_color,R.color.theme_6_minor_color, R.color.theme_6_minor_color_2 ));
        add(new ThemeColor(R.color.theme_7_major_color,R.color.theme_7_minor_color, R.color.theme_7_minor_color_2 ));
        add(new ThemeColor(R.color.theme_8_major_color,R.color.theme_8_minor_color, R.color.theme_8_minor_color_2 ));
        add(new ThemeColor(R.color.theme_9_major_color,R.color.theme_9_minor_color, R.color.theme_9_minor_color_2 ));
    }};




}
