package com.mlz.shuiguang.transmission.AWSTask;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.model.aws.DetectionDO;
import com.mlz.shuiguang.transmission.SyncTask;
import com.mlz.shuiguang.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dehualai on 2/1/18.
 */

public class DetectionTask extends SyncTask {


    public final static int TASK_PULL = 0 ;
    public final static int TASK_PUSH = 1;

    public DetectionTask(Context context){
        super(context);
    }

    @Override
    public void sync(HashMap<Object, Object> params){

        Integer task = (Integer) params.get(PARAM_TASK);
        switch (task){
            case TASK_PULL:
                pull();
                break;
            case TASK_PUSH:
                push();
                break;
            default:
                break;
        }
    }

    private void pull(){
        TBUser tbUser = new TBUser();
        ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));
        if (users.size() > 0 ){
            String user_id = users.get(0).getUserID();


        }
    }

    private void push(){

        TBUser tbUser = new TBUser();
        ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));
        if (users.size() > 0 ){
            String user_id = users.get(0).getUserID();

            final TBDetection tbDetection = new TBDetection();
            ArrayList<DetectionData> list = tbDetection.getDetectionList(DBAdapter.getDatabase(mContext));
            ArrayList<DetectionDO> dataHaveNotTransmitted = new ArrayList<DetectionDO>();
            ArrayList<DetectionData> localDataToUpdated = new ArrayList<DetectionData>();

            for(DetectionData data : list){
                if (!DBGlobal.TRANS_STATUS_TRANSMITTED.equals(data.getTransmission_status())){
                    DetectionDO detection = new DetectionDO();
                    detection.setUserId(user_id);
                    detection.setBodyArea(data.getBodyPart());
                    String detectionTime = Util.convertTimeToString(data.getDetectionTime(), Util.FORMAT_DATE_TIME);
                    detection.setDateTime(detectionTime);
                    detection.setNursingStatus(data.getPre_post());
                    detection.setValue(String.valueOf(data.getValue()));
                    detection.setDetectionId("A_" + (user_id + detectionTime).hashCode());
                    dataHaveNotTransmitted.add(detection);
                    localDataToUpdated.add(data);
                }
            }

            if (dataHaveNotTransmitted.size() > 0 ){

                final DetectionDO detection = dataHaveNotTransmitted.get(0);

                if (detection.getValue()!=null){

                    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
                    final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                            .dynamoDBClient(dynamoDBClient)
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .build();

                    final DetectionData localData = localDataToUpdated.get(0);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dynamoDBMapper.save(detection);
                                // Item saved
                                tbDetection.updateTransmissionStatus(DBAdapter.getDatabase(mContext), localData);
                            } catch (Exception e) {
                                Log.e("show", "exception " + e.getMessage());
                            }
                            Log.e("show", "item saved");
                        }
                    }).start();
                }
            }

        }
    }
}
