package com.mlz.shuiguang.database.operation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.DBUtil;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Util;

import java.util.ArrayList;

/**
 * Created by dehualai on 12/28/16.
 */

public class TBDetection {




    private void insert(SQLiteDatabase db, DetectionData detectionData) {
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_BODY_PART, detectionData.getBodyPart());
        DBUtil.smartPut(cv, DBGlobal.COL_DETECT_DATE_TIME, Util.convertTimeToString(detectionData.getDetectionTime(), Util.FORMAT_DATE_TIME));
        DBUtil.smartPut(cv, DBGlobal.COL_VALUE, String.format("%.1f", detectionData.getValue()));
        DBUtil.smartPut(cv, DBGlobal.COL_NURSING_STATUS, detectionData.getPre_post());
        if (detectionData.getTransmission_status() == null || detectionData.getTransmission_status().equals("")) {
            DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, DBGlobal.TRANS_STATUS_INSERTED);
        } else {
            DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, detectionData.getTransmission_status());
        }
        db.insert(DBGlobal.TABLE_DETECTION, null, cv);
    }


    public void updateTransmissionStatus(SQLiteDatabase db, DetectionData detectionData){
        ContentValues cv = new ContentValues();
        DBUtil.smartPut(cv, DBGlobal.COL_TRANSMISSION_STATUS, DBGlobal.TRANS_STATUS_TRANSMITTED);

        String selection = DBGlobal.COL_BODY_PART + " =? AND " +
                DBGlobal.COL_DETECT_DATE_TIME + " =? ";
        String[] selectionArgs = {detectionData.getBodyPart(),
                Util.convertTimeToString(detectionData.getDetectionTime(),Util.FORMAT_DATE_TIME)};
        db.update(DBGlobal.TABLE_DETECTION, cv, selection, selectionArgs);
    }

    public ArrayList<DetectionData> getDetectionList(SQLiteDatabase db) {

        ArrayList<DetectionData> queryResult = new ArrayList<DetectionData>();
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, null, null, null, null, null);
        if (result.getCount() > 0) {
            result.moveToFirst();
            int col_body_part = result.getColumnIndexOrThrow(DBGlobal.COL_BODY_PART);
            int col_time = result.getColumnIndexOrThrow(DBGlobal.COL_DETECT_DATE_TIME);
            int col_value = result.getColumnIndexOrThrow(DBGlobal.COL_VALUE);
            int col_pre_post = result.getColumnIndexOrThrow(DBGlobal.COL_NURSING_STATUS);
            int col_transmission_status = result.getColumnIndexOrThrow(DBGlobal.COL_TRANSMISSION_STATUS);

            queryResult.add((new DetectionData.DetectionDataBuilder(result.getString(col_body_part),
                    Util.convertStringToTime(result.getString(col_time), Util.FORMAT_DATE_TIME),
                    Float.valueOf(result.getString(col_value)),
                    result.getString(col_pre_post)
            )
            ).transmission_status(result.getString(col_transmission_status))
                    .build());


            while (result.moveToNext()) {
                queryResult.add((new DetectionData.DetectionDataBuilder(result.getString(col_body_part),
                        Util.convertStringToTime(result.getString(col_time), Util.FORMAT_DATE_TIME),
                        Float.valueOf(result.getString(col_value)),
                        result.getString(col_pre_post)
                )
                ).transmission_status(result.getString(col_transmission_status))
                        .build());
            }

        }
        result.close();
        return queryResult;
    }


    private boolean isRecordExist(SQLiteDatabase db, DetectionData detectionData) {
        ArrayList<DetectionData> queryResult = new ArrayList<DetectionData>();
        String selection = DBGlobal.COL_DETECT_DATE_TIME + " =? ";
        String[] selectArgs = new String[]{Util.convertTimeToString(detectionData.getDetectionTime(), Util.FORMAT_DATE_TIME)};
        Cursor result = db.query(DBGlobal.TABLE_DETECTION, null, selection, selectArgs, null, null, null);
        return result.getCount() > 0;
    }

    public void smartInsert(SQLiteDatabase db, DetectionData detectionData) {
        ArrayList<DetectionData> list = getDetectionList(db);
        if (!isRecordExist(db, detectionData)) {
            insert(db, detectionData);
        }
    }


    public void clean(SQLiteDatabase db) {
        db.delete(DBGlobal.TABLE_DETECTION, null, null);
    }


}
