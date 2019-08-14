package com.mlz.shuiguang.util;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.mlz.bluetooth.BluetoothDecrypt;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.fragment.detect.DetectFragment;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.transmission.bmobObject.Detection;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dehualai on 12/28/16.
 */

public class Util {

    static String TAG = "Util";

    public static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_DATE = "yyyy-MM-dd";
    public static String FORMAT_TIME = "HH:mm:ss";



    public static String convertTimeToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }

    public static Date convertStringToTime(String time, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        format.setTimeZone(TimeZone.getDefault());
        try {
            Date date = format.parse(time);
            return date;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void highlightLayout(LinearLayout layout, boolean highLight, Context mContext ){
        if (highLight)
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        else
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
    }

    public static boolean validateEmailFormat(String emailStr){
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
            return matcher.find();
    }


    public static ThemeColor getActiveTheme(){
        for (int i = 0 ; i < Global.themeColorOptions.size(); i++){
            if (i == Global.activeTheme)
                return Global.themeColorOptions.get(i);
        }
        return Global.themeColorOptions.get(0);
    }



    public static int countDaysDifferent(Date fromDate, Date toDate) {

        Date fromDateWithoutTime = Util.convertStringToTime(Util.convertTimeToString(fromDate, FORMAT_DATE), FORMAT_DATE);
        Date toDateWithoutTime = Util.convertStringToTime(Util.convertTimeToString(toDate, FORMAT_DATE), FORMAT_DATE);


        Calendar from_cal = Calendar.getInstance();
        from_cal.setTime(fromDateWithoutTime);

        int from_year = from_cal.get(Calendar.YEAR);
        int from_month = from_cal.get(Calendar.MONTH);
        int from_day = from_cal.get(Calendar.DAY_OF_MONTH);


        Calendar to_cal = Calendar.getInstance();
        to_cal.setTime(toDateWithoutTime);

        int to_year = to_cal.get(Calendar.YEAR);
        int to_month = to_cal.get(Calendar.MONTH);
        int to_day = to_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(from_year, from_month, from_day);
        date2.clear();
        date2.set(to_year, to_month, to_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000) + 1;

        return (int) dayCount;
    }


    public static String getDate(Date baseDate, int offset){
        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);
        cal.add(Calendar.DATE, offset);
        Date target = cal.getTime();
        return Util.convertTimeToString(target, FORMAT_DATE);
    }


    public static void createDemoUserData(Context mContext, int numDays) {

        SQLiteDatabase db = DBAdapter.getDatabase(mContext);
        TBDetection tbDetection = new TBDetection();
        tbDetection.clean(db);

        for (int i = numDays; i > 0; i--) {
            String date = getDate(new Date(), -i);

            DetectFragment.selectWhen = DetectionData.PRE_NURSING;
            DetectionData temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.FACE,
                            convertStringToTime(date + " 08:18:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.PRE_NURSING
                    ));
            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);

            temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.HAND,
                            convertStringToTime(date + " 08:28:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.PRE_NURSING
                    ));

            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);


            temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.EYES,
                            convertStringToTime(date + " 08:38:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.PRE_NURSING
                    ));
            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);

            DetectFragment.selectWhen = DetectionData.POST_NURSING;

            temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.FACE,
                            convertStringToTime(date + " 09:18:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.POST_NURSING
                    ));
            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);

            temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.HAND,
                            convertStringToTime(date + " 09:28:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.POST_NURSING
                    ));
            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);

            temp = new DetectionData(
                    new DetectionData.DetectionDataBuilder(DetectionData.EYES,
                            convertStringToTime(date + " 09:38:08", FORMAT_DATE_TIME),
                            new BluetoothDecrypt().fakeDataForDemo(),
                            DetectionData.POST_NURSING
                    ));
            tbDetection.smartInsert(db, temp);
            tbDetection.updateTransmissionStatus(db, temp);

            DetectFragment.selectWhen = "";
        }

    }


    public static String translateBodyPart(Context mContext, String bodyPart){
        if (bodyPart.equals(DetectionData.HAND)){
            return mContext.getResources().getString(R.string.hand);
        }
        if (bodyPart.equals(DetectionData.FACE)){
            return mContext.getResources().getString(R.string.face);
        }
        if (bodyPart.equals(DetectionData.EYES)){
            return mContext.getResources().getString(R.string.eyes);
        }
        return "";
    }

}
