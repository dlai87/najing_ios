package com.mlz.shuiguang.database;

import android.content.ContentValues;

/**
 * Created by dehualai on 1/7/17.
 */

public class DBUtil {
    public static void smartPut(ContentValues cv, String colName, String object ){
        if(object!=null) cv.put(colName, object);
    }
    public static void smartPut(ContentValues cv, String colName, Integer object ){
        if(object!=null) cv.put(colName, object);
    }
    public static void smartPut(ContentValues cv, String colName, Float object ){
        if(object!=null) cv.put(colName, object);
    }

}
