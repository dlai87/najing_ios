package com.mlz.shuiguang.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Contacts;

import com.github.mikephil.charting.data.Entry;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.transmission.bmobObject.Detection;
import com.mlz.shuiguang.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by dehualai on 12/28/16.
 */

public class HistoryModelOperation {


    public static final int TIME_RANGE_LAST_WEEK = 1;
    public static final int TIME_RANGE_LAST_TWO_WEEKS = 2;
    public static final int TIME_RANGE_LAST_MONTH = 3;
    public static final int TIME_RANGE_ALL = 4;

    private int TIME_RANGE = TIME_RANGE_LAST_TWO_WEEKS;

    private static String[] dateLabels;
    private int targetDays = -1;



    private Date startTime(){
        Calendar calendar = Calendar.getInstance();
        switch (TIME_RANGE){
            case TIME_RANGE_LAST_WEEK:
                calendar.add(Calendar.DATE, -7);
                targetDays = 7;
                break;
            case TIME_RANGE_LAST_TWO_WEEKS:
                calendar.add(Calendar.DATE, -14);
                targetDays = 14;
                break;
            case TIME_RANGE_LAST_MONTH:
                calendar.add(Calendar.DATE, -30);
                targetDays = 30;
                break;
            case TIME_RANGE_ALL:
                calendar.add(Calendar.YEAR, -100); // 100 years before
                targetDays = -1;
                break;
            default:
                break;
        }
        return calendar.getTime();
    }




    ///////////////////////////  2017-02-15  //////////////////////////////

    private ArrayList<DetectionData> getRawData(SQLiteDatabase db){
        TBDetection tbDetection = new TBDetection();
        ArrayList<DetectionData> rawDataList = tbDetection.getDetectionList(db);
        ArrayList<DetectionData> filterList = new ArrayList<>();
        // step 1 : filter data list
        for (DetectionData eachData : rawDataList){
            if (!eachData.getDetectionTime().before(startTime())){
                filterList.add(eachData);
            }
        }
        return filterList;
    }





    private Integer[] fillDummyIfNoEnoughData(int desiredLength, int currentLength, Integer[] data){
        if (currentLength>=desiredLength){
            return data;
        }
        Integer[] newData = new Integer[desiredLength];
        for(int i = 0; i < data.length; i++){
            newData[desiredLength-1-i] = data[currentLength-1-i];
        }
        for(int i = 0 ; i < newData.length; i++){
            if (newData[i] == null || newData[i] <0){
                newData[i] = 0;
            }else{
                break;
            }
        }
        return newData;
    }


    private Date firstDateOf(ArrayList<DetectionData> list, String desiredTag){
        for(int i = 0 ; i < list.size(); i++){
            DetectionData data = list.get(i);
            if(data.getPre_post().equals(desiredTag)){
                return data.getDetectionTime();
            }
        }
        return null;
    }

    private int getTotalDays(ArrayList<DetectionData> list, String desiredTag){
        Date date = firstDateOf(list, desiredTag);
        if (date!=null){
            return Util.countDaysDifferent(date, new Date());
        }
        return 0;
    }

    public static String[] getDateLabels(){
        return dateLabels;
    }

    /**
     *
     *  Get data by Date
     *
     *  targetBodyPart : what part you looking for.  if null, means gets all parts
     *
     * */
    public HashMap<String, Integer[]> getDataByDate(SQLiteDatabase db, String targetBodyPart){
        ArrayList<DetectionData> rawDataList = getRawData(db);
        if (rawDataList!=null && rawDataList.size()>0){
            Date firstDatePre = firstDateOf(rawDataList, DetectionData.PRE_NURSING);
            Date firstDatePost = firstDateOf(rawDataList, DetectionData.POST_NURSING);
            int totalDaysForPre = getTotalDays(rawDataList, DetectionData.PRE_NURSING);
            int totalDaysForPost = getTotalDays(rawDataList, DetectionData.POST_NURSING);
            Date targetFirstDate =  Util.convertStringToTime(Util.getDate(new Date(), -targetDays), Util.FORMAT_DATE);

            Integer[] preNursingRecord = new Integer[totalDaysForPre];
            Integer[] postNursingRecord = new Integer[totalDaysForPost];




            // create labels
            dateLabels = new String[targetDays];
            for(int i=0 ; i < targetDays; i++){
                String date = Util.getDate(targetFirstDate, i);
                // create labels for xAxis
                if (i%2==0) {
                    dateLabels[i] = Util.convertTimeToString(Util.convertStringToTime(date, Util.FORMAT_DATE), "MM/dd");
                }else{
                    dateLabels[i] = "";
                }
            }

            for(int i=0; i < totalDaysForPre; i++){
                // create data
                int preNursingSum = 0;
                int numPre = 0 ;
                String date = Util.getDate(firstDatePre, i);

                for (DetectionData eachData : rawDataList){
                    if (Util.convertTimeToString(eachData.getDetectionTime(), Util.FORMAT_DATE).equals(date)){
                        if (DetectionData.PRE_NURSING.equals(eachData.getPre_post())){
                            if(targetBodyPart==null || targetBodyPart.equals(eachData.getBodyPart())){
                                preNursingSum += eachData.getValue();
                                numPre++;
                            }
                        }
                    }
                }

                if (numPre!=0){
                    preNursingRecord[i] = (int)(preNursingSum*1.0f/numPre);
                }else{
                    preNursingRecord[i] = 0 ;
                }
            }

            // if value == 0 , use previous record , therefore avoid sudden drop on graphic
            for(int i=1 ; i < totalDaysForPre; i++){
                if(preNursingRecord[i]==0){
                    preNursingRecord[i] = preNursingRecord[i-1];
                }
            }


            for(int i=0; i < totalDaysForPost; i++){
                // create data
                int postNursingSum = 0;
                int numPost = 0 ;
                String date = Util.getDate(firstDatePost, i);

                for (DetectionData eachData : rawDataList){
                    if (Util.convertTimeToString(eachData.getDetectionTime(), Util.FORMAT_DATE).equals(date)){
                        if (DetectionData.POST_NURSING.equals(eachData.getPre_post())){
                            if(targetBodyPart==null || targetBodyPart.equals(eachData.getBodyPart())){
                                postNursingSum += eachData.getValue();
                                numPost++;
                            }
                        }
                    }
                }

                if (numPost!=0){
                    postNursingRecord[i] = (int)(postNursingSum*1.0f/numPost);
                }else{
                    postNursingRecord[i] = 0 ;
                }
            }


            // if value == 0 , use previous record , therefore avoid sudden drop on graphic
            for(int i=1 ; i < totalDaysForPost; i++){
                if(postNursingRecord[i]==0){
                    postNursingRecord[i] = postNursingRecord[i-1];
                }
            }


            // make list long
            preNursingRecord = fillDummyIfNoEnoughData(targetDays, totalDaysForPre, preNursingRecord);
            postNursingRecord = fillDummyIfNoEnoughData(targetDays, totalDaysForPost, postNursingRecord);



            HashMap<String, Integer[]> result = new HashMap<String, Integer[]>();
            result.put(DetectionData.PRE_NURSING, preNursingRecord);
            result.put(DetectionData.POST_NURSING, postNursingRecord);
            return result;
        }
        return null;
    }

    public HashMap<String, Integer> getDataByBodyPart(SQLiteDatabase db){

        ArrayList<DetectionData> rawDataList = getRawData(db);

        HashMap<String, Integer> record = new HashMap<>();
        record.put(DetectionData.HAND, 0);
        record.put(DetectionData.FACE, 0);
        record.put(DetectionData.EYES, 0);

        for (DetectionData eachData : rawDataList){
            if(DetectionData.HAND.equals(eachData.getBodyPart())){
                record.put(DetectionData.HAND, record.get(DetectionData.HAND)+1);
            }
            if(DetectionData.FACE.equals(eachData.getBodyPart())){
                record.put(DetectionData.FACE, record.get(DetectionData.FACE)+1);
            }
            if(DetectionData.EYES.equals(eachData.getBodyPart())){
                record.put(DetectionData.EYES, record.get(DetectionData.EYES)+1);
            }
        }

        return record;

    }






}
