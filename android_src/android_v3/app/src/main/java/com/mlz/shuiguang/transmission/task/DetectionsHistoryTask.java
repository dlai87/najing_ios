package com.mlz.shuiguang.transmission.task;

import android.content.Context;
import android.util.Log;
import android.widget.Adapter;

import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.DBGlobal;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.transmission.SyncTask;
import com.mlz.shuiguang.transmission.bmobObject.Detection;
import com.mlz.shuiguang.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by dehualai on 2/12/17.
 */

public class DetectionsHistoryTask extends SyncTask {

    public final static int TASK_PULL = 0 ;
    public final static int TASK_PUSH = 1;

    public DetectionsHistoryTask(Context context){
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
            BmobQuery<Detection> bmobQuery = new BmobQuery<Detection>();
            //user_id query
            bmobQuery.addWhereEqualTo("user_id", user_id);
            //return 99999 records，without setting the limits, will return 10 records by default
            bmobQuery.setLimit(99999);
            bmobQuery.findObjects(new FindListener<Detection>() {
                @Override
                public void done(List<Detection> list, BmobException e) {
                    if(e==null){
                        Log.e("show", "Pull Success: "+ list.size()+" records in total");
                        for (Detection d : list) {
                            DetectionData dData = new DetectionData(
                                    new DetectionData.DetectionDataBuilder(
                                            d.getBody_part(),
                                            Util.convertStringToTime(d.getDate_time(), Util.FORMAT_DATE_TIME),
                                            Float.valueOf(d.getValue()),
                                            d.getNursing_status()
                                            ).transmission_status(DBGlobal.TRANS_STATUS_TRANSMITTED));
                            TBDetection tbDetection = new TBDetection();
                            tbDetection.smartInsert(DBAdapter.getDatabase(mContext), dData);
                        }
                    }else{
                        Log.e("show", "Pull fails " + e.getMessage());
                    }
                }
            });
        }
    }

    private void push(){

        TBUser tbUser = new TBUser();
        ArrayList<UserData> users = tbUser.getUserList(DBAdapter.getDatabase(mContext));
        if (users.size() > 0 ){
            String user_id = users.get(0).getUserID();

            final TBDetection tbDetection = new TBDetection();
            ArrayList<DetectionData> list = tbDetection.getDetectionList(DBAdapter.getDatabase(mContext));
            ArrayList<DetectionData> dataHaveNotTransmitted = new ArrayList<DetectionData>();

            List<BmobObject> dataToBeTransmitted = new ArrayList<BmobObject>();
            for (DetectionData data : list) {
                if (!DBGlobal.TRANS_STATUS_TRANSMITTED.equals(data.getTransmission_status())){
                    Detection detection = new Detection();
                    detection.setUser_id(user_id);
                    detection.setBody_part(data.getBodyPart());
                    detection.setDate_time(Util.convertTimeToString(data.getDetectionTime(), Util.FORMAT_DATE_TIME));
                    detection.setNursing_status(data.getPre_post());
                    detection.setValue(String.valueOf(data.getValue()));
                    dataToBeTransmitted.add(detection);
                    dataHaveNotTransmitted.add(data);
                }
            }

            // no data that have not been transmitted.
            if (dataHaveNotTransmitted.size() <= 0 ){
                return;
            }

            // create a final list to handle callback update
            final ArrayList<DetectionData> dataToBeUpdated = dataHaveNotTransmitted;
            // batch transmit
            new BmobBatch().insertBatch(dataToBeTransmitted).doBatch(new QueryListListener<BatchResult>() {
                @Override
                public void done(List<BatchResult> o, BmobException e) {
                    if(e==null){

                        for(int i=0;i<o.size();i++){
                            BatchResult result = o.get(i);
                            BmobException ex =result.getError();
                            if(ex==null){
                                DetectionData toBeUpdated = dataToBeUpdated.get(i);
                                tbDetection.updateTransmissionStatus(DBAdapter.getDatabase(mContext), toBeUpdated);
                                Log.i("show","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                            }else{
                                Log.i("show","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                            }
                        }
                    }else{
                        Log.i("show","失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }

}
