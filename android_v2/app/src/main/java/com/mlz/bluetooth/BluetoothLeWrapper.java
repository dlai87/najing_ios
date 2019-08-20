package com.mlz.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mlz.decode.DecryptUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehualai on 2/9/17.
 */

public class BluetoothLeWrapper {

    Context mContext;
    Activity mActivity;
    ConnectionHandler connectionHandler;


    int CONNECTION_TIME_OUT_IN_SEC = 60 ;
    public static final String targetDeviceName = "MyService";
    public static final String targetUUID = "00001601-0000-1000-8000-00805f9b34fb";


    static BluetoothLeWrapper instance;
    long connectedTime = 0 ;


    private static BluetoothGattCharacteristic mSCharacteristic, mModelNumberCharacteristic;   //, mSerialPortCharacteristic, mCommandCharacteristic;
    BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //private LeDeviceListAdapter mLeDeviceListAdapter=null;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;

    BluetoothDevice targetDevice;
    private String mDeviceAddress;

    public enum ConnectionStateEnum {
        isNull,
        isScanning,
        isToScan,
        isConnecting,
        isConnected,
        isDisconnecting};

    public enum InstructionStateEnum{
        notConnected,
        connecting,
        reading
    };


    public ConnectionStateEnum mConnectionState = ConnectionStateEnum.isNull;
    private static final int REQUEST_ENABLE_BT = 1;

    private Handler mHandler = new Handler();

    public boolean mConnected = false;

    String value;

    private final static String TAG = BluetoothLeWrapper.class.getSimpleName();



    private BluetoothLeWrapper() {
    }

    public static BluetoothLeWrapper getInstance() {
        if (instance == null) {
            instance = new BluetoothLeWrapper();
        }
        return instance;
    }




    public interface ConnectionHandler {
        public void onConectionStateChange(ConnectionStateEnum theconnectionStateEnum);
        public void onValueDetected(int value);
        public void onDisplayInstruction(InstructionStateEnum instructionState);

    }

    public void setConnectionHandler(ConnectionHandler handler) {
        connectionHandler = handler;
    }





    boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        if (!mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }


    private void reset() {
        mScanning = false;
        targetDevice = null;
        mDeviceAddress = null;
        mConnectionState = ConnectionStateEnum.isNull;
        mConnected = false;
        value = null;
        mSCharacteristic = null;
    }


    private Runnable mConnectingOverTimeRunnable = new Runnable() {

        @Override
        public void run() {
            if (mConnectionState == ConnectionStateEnum.isConnecting)
                mConnectionState = ConnectionStateEnum.isToScan;
            if (connectionHandler != null)
                connectionHandler.onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };

    private Runnable mDisonnectingOverTimeRunnable = new Runnable() {

        @Override
        public void run() {
            if (mConnectionState == ConnectionStateEnum.isDisconnecting)
                mConnectionState = ConnectionStateEnum.isToScan;
            if (connectionHandler != null)
                connectionHandler.onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };


    public void onCreateProcess(Activity activity) {
        this.mContext = activity;
        this.mActivity = activity;

        if (!initiate()) {
            // Blue tooth is not support.
            mActivity.finish();
        }

        Intent gattServiceIntent = new Intent(mActivity, BluetoothLeService.class);
        mActivity.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);


    }

    public void onResumeProcess() {

        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mActivity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        mContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }


    public void onPauseProcess() {
        scanLeDevice(false);
        mContext.unregisterReceiver(mGattUpdateReceiver);
        //mLeDeviceListAdapter.clear();
        mConnectionState = ConnectionStateEnum.isToScan;
        if (connectionHandler != null) connectionHandler.onConectionStateChange(mConnectionState);
        //mScanDeviceDialog.dismiss();
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);

//			mBluetoothLeService.close();
        }
        mSCharacteristic = null;

    }


    public void onStopProcess() {
        if (mBluetoothLeService != null) {
            mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
            mBluetoothLeService.close();
        }
        mSCharacteristic = null;
    }

    public void onDestroyProcess() {
        mContext.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    public void onActivityResultProcess(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_CANCELED) {
            mActivity.finish();
            return;
        }
    }


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            System.out.println("mGattUpdateReceiver->onReceive->action=" + action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mHandler.removeCallbacks(mConnectingOverTimeRunnable);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mConnectionState = ConnectionStateEnum.isToScan;
                if (connectionHandler != null)
                    connectionHandler.onConectionStateChange(mConnectionState);
                mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
                mBluetoothLeService.close();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                    System.out.println("ACTION_GATT_SERVICES_DISCOVERED  " +
                            gattService.getUuid().toString());
                }
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                System.out.println("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

                final Thread t = new Thread(new RepeatingThread());
                t.start();

            }
        }
    };


    public class RepeatingThread implements Runnable {
        private final Handler mHandler = new Handler();

        public RepeatingThread() {

        }

        @Override
        public void run() {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSCharacteristic != null) {
                        getOneValue();
                    }
                }
            });
            mHandler.postDelayed(this, 5000);
        }
    }


    private void getOneValue() {
        if (!checkValueValid()) {
            value = byteArrayToHexString(mSCharacteristic.getValue());
            Log.e("show", "读数 :: " + value);
            if (connectionHandler!=null){
                connectionHandler.onDisplayInstruction(InstructionStateEnum.reading);
            }
            // Time out
            if (connectedTime <= 0 ){
                connectedTime = System.currentTimeMillis();
            }
            if (connectedTime > 0 ){
                if (System.currentTimeMillis() - connectedTime > CONNECTION_TIME_OUT_IN_SEC*1000){
                    connectedTime = 0 ;
                    if (connectionHandler!=null) connectionHandler.onValueDetected(-1);
                    reset();
                    return;
                }
            }
            // broadcast
            broadcast();
        } else {
            connectedTime = 0 ;
            if (connectionHandler!=null){
                int percentage = new BluetoothDecrypt().decrypt(mSCharacteristic.getValue());  //(new DecryptUtil().decrypt(mSCharacteristic.getValue()))/1000 + 1;
                Log.e("show", "含水量 :: " + percentage);
                connectionHandler.onValueDetected(percentage);
                reset();
            }
        }
    }

    private boolean checkValueValid() {
        if (value != null && !value.equals("00000000000000")) {

            int percentage = new BluetoothDecrypt().decrypt(mSCharacteristic.getValue());  //(new DecryptUtil().decrypt(mSCharacteristic.getValue()))/1000 + 1;
            if (percentage >= 20 && percentage <= 60)
                return true;
        }
        return false;
    }


    public static String byteArrayToHexString(byte[] b) {
        if (b == null) return null;
        int len = b.length;
        String data = new String();

        for (int i = 0; i < len; i++) {
            data += Integer.toHexString((b[i] >> 4) & 0xf);
            data += Integer.toHexString(b[i] & 0xf);
        }
        return data;
    }

    public void buttonScanOnClickProcess() {
        reset();
        switch (mConnectionState) {
            case isNull:
                mConnectionState = ConnectionStateEnum.isScanning;
                if (connectionHandler != null) {
                    connectionHandler.onConectionStateChange(mConnectionState);
                    connectionHandler.onDisplayInstruction(InstructionStateEnum.notConnected);
                }
                scanLeDevice(true);
                Log.e("show", "未链接");
                break;
            case isToScan:
                mConnectionState = ConnectionStateEnum.isScanning;
                if (connectionHandler != null)
                    connectionHandler.onConectionStateChange(mConnectionState);
                scanLeDevice(true);
                Log.e("show", "搜索设备中");
                break;
            case isScanning:
                Log.e("show", "搜索设备。。。");
                break;

            case isConnecting:
                Log.e("show", "链接中");
                break;
            case isConnected:
                mBluetoothLeService.disconnect();
                mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
                mConnectionState = ConnectionStateEnum.isDisconnecting;
                if (connectionHandler != null)
                    connectionHandler.onConectionStateChange(mConnectionState);
                Log.e("show", "链接成功");
                break;
            case isDisconnecting:
                Log.e("show", "断开链接");
                break;

            default:
                break;
        }


    }

    void scanLeDevice(final boolean enable) {
        if (enable) {
            if (!mScanning) {
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            if (mScanning) {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    // Code to manage Service lifecycle.
    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                mActivity.finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            Log.e("show", "蓝牙服务断开");

        }
    };

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (targetDeviceName.equals(device.getName())) {
                        targetDevice = device;
                        Log.e("show", "停止扫描");
                        scanLeDevice(false);
                        Log.e("show", "开始链接");
                        startLEconnect();
                        if (connectionHandler!=null){
                            connectionHandler.onDisplayInstruction(InstructionStateEnum.connecting);
                        }
                    }
                }
            });
        }
    };



    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        mModelNumberCharacteristic = null;
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            System.out.println("displayGattServices + uuid=" + uuid);

            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                if (uuid.equals(targetUUID)) {
                    mModelNumberCharacteristic = gattCharacteristic;
                }
            }
            mGattCharacteristics.add(charas);
        }


        broadcast();

    }


    private void broadcast() {
        mSCharacteristic = mModelNumberCharacteristic;
        mBluetoothLeService.readCharacteristic(mSCharacteristic);
        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }


    private void startLEconnect() {

        if (targetDevice == null || targetDevice.getName() == null || targetDevice.getAddress() == null) {
            mConnectionState = ConnectionStateEnum.isToScan;
            if (connectionHandler != null)
                connectionHandler.onConectionStateChange(mConnectionState);
        } else {
            mDeviceAddress = targetDevice.getAddress().toString();

            if (mBluetoothLeService.connect(mDeviceAddress)) {
                Log.d(TAG, "Connect request success");
                mConnectionState = ConnectionStateEnum.isConnecting;
                if (connectionHandler != null)
                    connectionHandler.onConectionStateChange(mConnectionState);
                mHandler.postDelayed(mConnectingOverTimeRunnable, 10000);
            } else {
                Log.d(TAG, "Connect request fail");
                mConnectionState = ConnectionStateEnum.isToScan;
                if (connectionHandler != null)
                    connectionHandler.onConectionStateChange(mConnectionState);
            }
        }
    }


}
