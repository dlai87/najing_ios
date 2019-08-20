package com.mlz.shuiguang.transmission.AWSTask;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.model.aws.USERDO;
import com.mlz.shuiguang.transmission.SyncTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dehualai on 2/1/18.
 */

public class USERTask extends SyncTask {

    String TAG = "show";
    public final static int TASK_PUSH = 1;

    public USERTask(Context context) {
        super(context);
    }

    @Override
    public void sync(HashMap<Object, Object> params) {
        Integer task = (Integer) params.get(PARAM_TASK);
        switch (task) {
            case TASK_PUSH:
                push();
                break;
            default:
                push();
                break;
        }
    }


    private void push() {
        final TBUser tbUser = new TBUser();
        String transmissionStatus = tbUser.checkTransmissionStatus(DBAdapter.getDatabase(mContext));
        if (DBGlobal.TRANS_STATUS_UPDATED.equals(transmissionStatus)
                || DBGlobal.TRANS_STATUS_INSERTED.equals(transmissionStatus)) {
            ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));

            if (users.size() > 0) {

                try {
                    AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
                    final DynamoDBMapper dynamoDBMapper = DynamoDBMapper.builder()
                            .dynamoDBClient(dynamoDBClient)
                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                            .build();

                    UserData user = users.get(0);

                    final USERDO newsItem = new USERDO();

                    newsItem.setUserId(user.getUserID());
                    newsItem.setEmail(user.getEmail());
                    newsItem.setPhone(user.getPhone());
                    newsItem.setGender(user.getGender());
                    newsItem.setDateOfBirth(user.getDate_of_birth());
                    newsItem.setWechat(user.getWechat());
                    newsItem.setGoogle(user.getGoogle());
                    newsItem.setFacebook(user.getFacebook());
                    newsItem.setUsername(user.getUsername());
                    newsItem.setPlatform("Android");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dynamoDBMapper.save(newsItem);
                                // Item saved
                                tbUser.updateTransmissionStatus(DBAdapter.getDatabase(mContext), DBGlobal.TRANS_STATUS_TRANSMITTED);
                            } catch (Exception e) {
                                Log.e("show", "exception " + e.getMessage());
                            }
                            Log.e("show", "item saved");
                        }
                    }).start();
                } catch (Exception e) {
                    Log.e("show", "exception " + e.getMessage());
                }


            }

        }

    }
}
