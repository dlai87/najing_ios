package com.mlz.shuiguang.fragment.detect;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mlz.bluetooth.BluetoothLeWrapper;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBDetection;
import com.mlz.shuiguang.model.DetectionData;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.dialog.AlertDialog;
import com.mlz.shuiguang.util.dialog.DetectionSuccessDialog;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.mlz.shuiguang.util.widget.button.NJButton;
import com.mlz.shuiguang.util.widget.wave.WaveHelper;
import com.mlz.shuiguang.util.widget.wave.WaveView;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dehualai on 12/24/16.
 */

public class DetectFragment extends Fragment {


    int DISMISS_RESULT_IN_SEC = 6;

    String TAG = "DetectFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;


    ArrayList<DetectionData> detectionRocords = new ArrayList<DetectionData>();

    private WaveHelper mWaveHelper;
    View view;


    public static String selectWhere = ""; //DetectionData.HAND;
    public static String selectWhen = ""; //DetectionData.PRE_NURSING;

    final int STATUS_GET_READY = 0;
    final int STATUS_CONNECTING = 1;
    final int STATUS_DETECTED = 3;
    int status = STATUS_GET_READY;

    RelativeLayout bgLayout;
    LinearLayout headerLayout;

    NJButton handButton;
    NJButton faceButton;
    NJButton eyesButton;
    NJButton preNursingButton;
    NJButton postNursingButton;

    TextView bluetoothStatusTextView;
    TextView waterLevelTextView;
    LinearLayout linearLayout;
    Button startButton;
    WaveView waveView;

    TextView detectionRecordTextView;



    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;

        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_detect, container, false);
        Bundle args = getArguments();

        BluetoothLeWrapper.getInstance().setConnectionHandler(new BluetoothLeWrapper.ConnectionHandler() {
            @Override
            public void onConectionStateChange(BluetoothLeWrapper.ConnectionStateEnum theconnectionStateEnum) {
                switch (theconnectionStateEnum) {											//Four connection state
                    case isConnected:
                       // buttonScan.setText("Connected");
                        break;
                    case isConnecting:
                      //  buttonScan.setText("Connecting");
                        break;
                    case isToScan:
                       // buttonScan.setText("Scan");
                        break;
                    case isScanning:
                      //  buttonScan.setText("Scanning");
                        break;
                    case isDisconnecting:
                       // buttonScan.setText("isDisconnecting");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onValueDetected(final int value){
                if (value > 0){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DetectionData data = new DetectionData(new DetectionData.DetectionDataBuilder(selectWhere, new Date(), value, selectWhen ));
                            // add record buffer
                            detectionRocords.add(data);
                            // insert database
                            TBDetection tbDetection = new TBDetection();
                            tbDetection.smartInsert(DBAdapter.getDatabase(mContext), data);
                            // update UI
                            status = STATUS_DETECTED;
                            presetDetectionValue(value);
                        }
                    });
                }else{
                    resetStatus();
                }
            }

            @Override
            public void onDisplayInstruction(final BluetoothLeWrapper.InstructionStateEnum instructionState){
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String instruction = "";
                        switch (instructionState){
                            case notConnected:
                                instruction = mContext.getResources().getString(R.string.bluetooth_status_not_connect);
                                break;
                            case connecting:
                                instruction = mContext.getResources().getString(R.string.bluetooth_status_connecting);
                                break;
                            case reading:
                                instruction = mContext.getResources().getString(R.string.bluetooth_status_reading);
                                break;
                            default:
                                break;
                        }
                        bluetoothStatusTextView.setText(instruction);
                    }
                });

            }
        });


        assignViews();
        addActionToViews();

        waveView = (WaveView) view.findViewById(R.id.wave);
        waveView.setBorder(10, mContext.getResources().getColor(R.color.theme_2_color_1));
        waveView.setShapeType(WaveView.ShapeType.CIRCLE);
        mWaveHelper = new WaveHelper(waveView);



        return view;
    }


    private void assignViews(){
        bgLayout = (RelativeLayout) view.findViewById(R.id.bg_layout);
        headerLayout = (LinearLayout)view.findViewById(R.id.header_layout);

        handButton = (NJButton)view.findViewById(R.id.handButton);
        faceButton = (NJButton)view.findViewById(R.id.faceButton);
        eyesButton = (NJButton)view.findViewById(R.id.eyesButton);
        preNursingButton = (NJButton)view.findViewById(R.id.preNursingButton);
        postNursingButton = (NJButton)view.findViewById(R.id.postNursingButton);

        startButton = (Button)view.findViewById(R.id.start_button);
        waterLevelTextView = (TextView)view.findViewById(R.id.water_level_text);
        bluetoothStatusTextView = (TextView)view.findViewById(R.id.bluetooth_status_text);
        linearLayout = (LinearLayout)view.findViewById(R.id.main_layout);

        detectionRecordTextView = (TextView)view.findViewById(R.id.detection_record);
    }

    private void addActionToViews(){

        handButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWhere = DetectionData.HAND;
                buttonGroup1();
            }
        });

        faceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWhere = DetectionData.FACE;
                buttonGroup1();
            }
        });

        eyesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWhere = DetectionData.EYES;
                buttonGroup1();
            }
        });

        preNursingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWhen = DetectionData.PRE_NURSING;
                buttonGroup2();
            }
        });

        postNursingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectWhen = DetectionData.POST_NURSING;
                buttonGroup2();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectWhere.equals("") || selectWhen.equals("")){
                    AlertDialog alertDialog = new AlertDialog(mContext);
                    alertDialog.setTitleView(null);
                    alertDialog.setMessageView(mContext.getResources().getString(R.string.please_select_when_and_where));
                    alertDialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                    alertDialog.showDialog();
                }else{
                    BluetoothLeWrapper.getInstance().buttonScanOnClickProcess();
                    status = STATUS_CONNECTING;
                    buttonGroup3();
                }
            }
        });
    }


    private void refreshViews() {
        ThemeColor theme = Util.getActiveTheme();
        bgLayout.setBackgroundColor(theme.getMainColor(mContext));
        headerLayout.setBackgroundColor(theme.getMinorColor(mContext));
    }


    private void setButtonsClickable(boolean isClickable){
        faceButton.setClickable(isClickable);
        eyesButton.setClickable(isClickable);
        handButton.setClickable(isClickable);
        preNursingButton.setClickable(isClickable);
        postNursingButton.setClickable(isClickable);
    }

    @Override
    public void onPause() {
        super.onPause();
        BluetoothLeWrapper.getInstance().onPauseProcess();
    }

    @Override
    public void onResume() {
        super.onResume();
        detectionRocords = new ArrayList<DetectionData>();
        BluetoothLeWrapper.getInstance().onResumeProcess();
        refreshViews();

    }


    private void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(1f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(start - end);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {

            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 100) * count;

            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "%");
                }
            }, time);
        }
    }


    private void buttonGroup1(){
        if(selectWhere.equals(DetectionData.HAND)){
            handButton.setButtonTheme(2);
            faceButton.setButtonTheme(1);
            eyesButton.setButtonTheme(1);
        }else if (selectWhere.equals(DetectionData.FACE)){
            handButton.setButtonTheme(1);
            faceButton.setButtonTheme(2);
            eyesButton.setButtonTheme(1);
        }else if (selectWhere.equals(DetectionData.EYES)){
            handButton.setButtonTheme(1);
            faceButton.setButtonTheme(1);
            eyesButton.setButtonTheme(2);
        }else {
            handButton.setButtonTheme(1);
            faceButton.setButtonTheme(1);
            eyesButton.setButtonTheme(1);
        }
    }

    private void buttonGroup2(){
        if(selectWhen.equals(DetectionData.PRE_NURSING)){
            preNursingButton.setButtonTheme(2);
            postNursingButton.setButtonTheme(1);
        }else if (selectWhen.equals(DetectionData.POST_NURSING)){
            preNursingButton.setButtonTheme(1);
            postNursingButton.setButtonTheme(2);
        }else {
            preNursingButton.setButtonTheme(1);
            postNursingButton.setButtonTheme(1);
        }
    }

    private void buttonGroup3(){
        switch (status){
            case STATUS_GET_READY:
                setButtonsClickable(true);
                startButton.setVisibility(View.VISIBLE);
                bluetoothStatusTextView.setVisibility(View.GONE);
                waterLevelTextView.setVisibility(View.GONE);
                waveView.setVisibility(View.GONE);
                dismissDetectionValue();
                displayRecords();
                break;
            case STATUS_CONNECTING:
                setButtonsClickable(false);
                startButton.setVisibility(View.GONE);
                bluetoothStatusTextView.setVisibility(View.VISIBLE);
                waterLevelTextView.setVisibility(View.GONE);
                waveView.setVisibility(View.GONE);
                break;
            case STATUS_DETECTED:
                setButtonsClickable(false);
                startButton.setVisibility(View.GONE);
                bluetoothStatusTextView.setVisibility(View.GONE);
                waterLevelTextView.setVisibility(View.VISIBLE);
                waveView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    private void presetDetectionValue(final int value){

        buttonGroup3();
        mWaveHelper.start(value);
        animateTextView(0, value, waterLevelTextView);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resetStatus();
                DetectionSuccessDialog successDialog = new DetectionSuccessDialog(mContext);
                successDialog.setMessageView(mContext.getResources().getString(R.string.detection_result_is) + value + "%");
                successDialog.showDialog();
            }
        }, DISMISS_RESULT_IN_SEC*1000);
    }

    private void dismissDetectionValue(){
        mWaveHelper.cancel();
    }

    private void displayRecords(){
        String str = "";
        for (DetectionData data : detectionRocords){
            if (str.equals("")){
                str = mContext.getResources().getString(R.string.detection_records);
            }
            str += data.toString() + "\n";
        }
        detectionRecordTextView.setText(str);
    }


    private void resetStatus(){
        status = STATUS_GET_READY;
        selectWhen = "";
        selectWhere = "";
        buttonGroup1();
        buttonGroup2();
        buttonGroup3();
    }




}
