package com.mlz.shuiguang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mlz.shuiguang.util.Global;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dehualai on 12/28/16.
 */

public class DBAdapter extends SQLiteOpenHelper {

    private static DBAdapter mInstance = null;
    private String TAG = "DBAdapter";
    private static SQLiteDatabase db = null;
    private static String name ;


    public static String getDatabaseName(Context ctx) {
        if (DBGlobal.DB_ON_SDCARD == false){
            name = DBGlobal.DB;
        } else {
            File a = ctx.getExternalFilesDir(null);
            if (a != null) {
                name = a.getAbsolutePath() + "/" + DBGlobal.DB;
            } else {
                name = Global.FOLDER_APP_ROOT + DBGlobal.DB;
            }
        }
        return name;
    }

    private DBAdapter(Context context, String name) {
        super(context,name ,null, DBGlobal.DB_VERSION);
        Log.d(TAG,name);
    }

    /** Get a readable and writable from here.
     *  This makes sure that only one database is open all the time.
     *
     * @param ctx This context is used to get application context
     * @return
     */
    public  static SQLiteDatabase getDatabase(Context ctx) {
        if (mInstance == null) {
            name = getDatabaseName(ctx);
            mInstance = new DBAdapter(ctx.getApplicationContext(), name);
        }
        if (db == null && mInstance != null) {
            db = mInstance.getWritableDatabase();
        }
        return db;
    }

    /**
     *  Close database
     */
    public static void closeDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
        if (mInstance != null) {
            mInstance.close();
            mInstance = null;
        }
    }


    /**
	 * Create database from here.
	 */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableDetection(db);

    }

    /**
	 * Upgrade database.
	 */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    break;
                default:
                    break;
            }
            upgradeTo++;
        }
    }


    private String generateCreationSpec(String tableName, ArrayList<String[]> keyValuePair) {

        StringBuffer SPEC = new StringBuffer();
        SPEC.append("CREATE TABLE " + tableName + " (");
        for (int i = 0; i < keyValuePair.size(); i++) {
            String[] str = keyValuePair.get(i);
            SPEC.append(" " + str[0] + " " + str[1] );
            SPEC.append( i < keyValuePair.size() - 1  ? "," : ");");
        }
        return SPEC.toString();

    }

    /**
     * ///////////////////////////
     *
     * DB create code
     *
     * ////////////////////////////
     */

    ////=========== version 1

    private void createTableUser(SQLiteDatabase db){

        ArrayList<String[]> keyValuePair = new ArrayList<String[]>();
        keyValuePair.add(new String[] {DBGlobal.COL_AUTO_ID,"integer primary key autoincrement"});
        keyValuePair.add(new String[] {DBGlobal.COL_USER_ID, "TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_USERNAME,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_AVATAR, "TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_GENDER,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_DATE_OF_BIRTH,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_PHONE_NUMBER,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_EMAIL,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_TRANSMISSION_STATUS,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_SKIN_TYPE, "TEXT"});

        String SPEC = generateCreationSpec( DBGlobal.TABLE_USER, keyValuePair);
        db.execSQL(SPEC);
        Log.d(TAG,"Db Created" + SPEC );
    }

    private void createTableDetection(SQLiteDatabase db){

        ArrayList<String[]> keyValuePair = new ArrayList<String[]>();
        keyValuePair.add(new String[] {DBGlobal.COL_AUTO_ID,"integer primary key autoincrement"});
        keyValuePair.add(new String[] {DBGlobal.COL_BODY_PART,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_DETECT_DATE_TIME,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_VALUE,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_NURSING_STATUS,"TEXT"});
        keyValuePair.add(new String[] {DBGlobal.COL_TRANSMISSION_STATUS,"TEXT"});

        String SPEC = generateCreationSpec( DBGlobal.TABLE_DETECTION, keyValuePair);
        db.execSQL(SPEC);
        Log.d(TAG,"Db Created" + SPEC );
    }


    ////============= version 2


}
