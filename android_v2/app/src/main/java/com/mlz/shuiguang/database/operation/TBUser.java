package com.mlz.shuiguang.database.operation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.DBUtil;
import com.mlz.shuiguang.model.UserData;

import java.util.ArrayList;

/**
 * Created by dehualai on 12/28/16.
 */

public class TBUser {


    public void insert(SQLiteDatabase db, UserData user){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_USERNAME, user.getUsername());
        DBUtil.smartPut(cv, DBGlobal.COL_USER_ID, user.getUserID());
        DBUtil.smartPut(cv, DBGlobal.COL_AVATAR, user.getAvatar());
        DBUtil.smartPut(cv, DBGlobal.COL_GENDER, user.getGender());
        DBUtil.smartPut(cv, DBGlobal.COL_DATE_OF_BIRTH, user.getDate_of_birth());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, user.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_EMAIL, user.getEmail());
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, DBGlobal.TRANS_STATUS_INSERTED);
        DBUtil.smartPut(cv, DBGlobal.COL_SKIN_TYPE, user.getSkin_type());
        DBUtil.smartPut(cv, DBGlobal.COL_WECHAT, user.getWechat());
        DBUtil.smartPut(cv, DBGlobal.COL_GOOGLE, user.getGoogle());
        DBUtil.smartPut(cv, DBGlobal.COL_FACEBOOK, user.getFacebook());
        db.insert(DBGlobal.TABLE_USER, null, cv);
    }

    public void update(SQLiteDatabase db, UserData user){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_AVATAR, user.getAvatar());
        DBUtil.smartPut(cv, DBGlobal.COL_GENDER, user.getGender());
        DBUtil.smartPut(cv, DBGlobal.COL_DATE_OF_BIRTH, user.getDate_of_birth());
        DBUtil.smartPut(cv, DBGlobal.COL_PHONE_NUMBER, user.getPhone());
        DBUtil.smartPut(cv, DBGlobal.COL_EMAIL, user.getEmail());
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, DBGlobal.TRANS_STATUS_UPDATED);
        DBUtil.smartPut(cv, DBGlobal.COL_SKIN_TYPE, user.getSkin_type());
        DBUtil.smartPut(cv, DBGlobal.COL_WECHAT, user.getWechat());
        DBUtil.smartPut(cv, DBGlobal.COL_GOOGLE, user.getGoogle());
        DBUtil.smartPut(cv, DBGlobal.COL_FACEBOOK, user.getFacebook());
        String selection = DBGlobal.COL_USERNAME + " =? ";
        String[] selectionArgs = {user.getUsername()};
        db.update(DBGlobal.TABLE_USER, cv, selection, selectionArgs);
    }


    public String checkTransmissionStatus(SQLiteDatabase db){

        String status = DBGlobal.TRANS_STATUS_INSERTED;
        Cursor result = db.query(DBGlobal.TABLE_USER, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_status = result.getColumnIndexOrThrow(DBGlobal.COL_TRANSMISSION_STATUS);
            status = result.getString(col_status);
        }
        result.close();
        return status;
    }

    public void updateTransmissionStatus(SQLiteDatabase db, String status){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, status);
        db.update(DBGlobal.TABLE_USER, cv, null, null);
    }



    public ArrayList<UserData> getUserList(SQLiteDatabase db){
        /*  NOTE:
        * Cursor query (String table,
                String[] columns,
                String selection,
                String[] selectionArgs,
                String groupBy,
                String having,
                String orderBy,
                String limit)
                */
        ArrayList<UserData> queryResult = new ArrayList<UserData>();
        Cursor result = db.query(DBGlobal.TABLE_USER, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_user_id = result.getColumnIndexOrThrow(DBGlobal.COL_USER_ID);
            int col_username = result.getColumnIndexOrThrow(DBGlobal.COL_USERNAME);
            int col_gender = result.getColumnIndexOrThrow(DBGlobal.COL_GENDER);
            int col_dob = result.getColumnIndexOrThrow(DBGlobal.COL_DATE_OF_BIRTH);
            int col_phone = result.getColumnIndexOrThrow(DBGlobal.COL_PHONE_NUMBER);
            int col_email = result.getColumnIndexOrThrow(DBGlobal.COL_EMAIL);
            int col_avatar = result.getColumnIndexOrThrow(DBGlobal.COL_AVATAR);
            int col_skin_type = result.getColumnIndexOrThrow(DBGlobal.COL_SKIN_TYPE);


            queryResult.add( (new UserData.UserBuilder(result.getString(col_username), result.getString(col_user_id))
                    .gender(result.getString(col_gender))
                    .date_of_birth(result.getString(col_dob))
                    .phone(result.getString(col_phone))
                    .email(result.getString(col_email))
                    .avatar(result.getString(col_avatar))
                    .skin_type(result.getString(col_skin_type))
            ).build());

            while(result.moveToNext()){
                queryResult.add( (new UserData.UserBuilder(result.getString(col_username), result.getString(col_user_id))
                        .gender(result.getString(col_gender))
                        .date_of_birth(result.getString(col_dob))
                        .phone(result.getString(col_phone))
                        .email(result.getString(col_email))
                        .avatar(result.getString(col_avatar))
                        .skin_type(result.getString(col_skin_type))
                ).build());
            }

        }
        result.close();
        return queryResult;
    }

    public void smartInsert(SQLiteDatabase db, UserData user){
        ArrayList<UserData> users = getUserList(db);
        boolean userExist = false;
        for (UserData userData : users){
            if (userData.getUsername().equals(user.getUsername())){
                userExist = true;
            }
        }

        if (userExist){
            update(db, user);
        }else {
            insert(db, user);
        }
    }


    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_USER, null, null);
    }

}
