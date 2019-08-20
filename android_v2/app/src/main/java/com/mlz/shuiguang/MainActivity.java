package com.mlz.shuiguang;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.facebook.FacebookSignInProvider;
import com.amazonaws.mobile.auth.google.GoogleSignInProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.common.Scopes;
import com.mlz.bluetooth.BluetoothLeWrapper;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.fragment.BaseFragment;
import com.mlz.shuiguang.fragment.detect.DetectFragment;
import com.mlz.shuiguang.fragment.history.HistoryFragment;
import com.mlz.shuiguang.fragment.message.MessageFragment;
import com.mlz.shuiguang.fragment.user.UserFragment;
import com.mlz.shuiguang.transmission.SyncTask;
import com.mlz.shuiguang.transmission.TransmissionUtil;
import com.mlz.shuiguang.transmission.task.AuthsTask;
import com.mlz.shuiguang.transmission.task.DetectionsHistoryTask;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.mlz.shuiguang.util.widget.tabbar.TabBarView;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends Activity implements IMainAppHandler, BaseFragment.OnFragmentInteractionListener{

    String TAG = "MainActivity";

    private int latestPosition = -1;


    TabBarView tabBarAnimView;
    FragmentUtil fragmentUtil;
    Context mContext;



    ActivityResultHandler resultHandler;

    public interface ActivityResultHandler{
        public void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    public void setActivityResultHandler(ActivityResultHandler handler){
        this.resultHandler = handler;
    }



    private static final int REQUEST_PERMISSIONS = 1;

    private static final String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private void requestVideoPermissions() {
        ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "onRequestPermissionsResult DENIED");

                        com.mlz.shuiguang.util.dialog.AlertDialog alertDialog = new com.mlz.shuiguang.util.dialog.AlertDialog(this);
                        alertDialog.setTitleView(null);
                        alertDialog.setMessageView(getResources().getString(R.string.please_allow_all_permissions));
                        alertDialog.setButtons(getResources().getString(R.string.ok), null, null);
                        alertDialog.showDialog();


                        break;
                    }
                }
            } else {
                Log.e(TAG, "onRequestPermissionsResult GRAND");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onStart(){
        super.onStart();
        mContext = this;



        DBAdapter.getDatabase(mContext);
        fragmentUtil = new FragmentUtil(this);

        TransmissionUtil.syncAll(mContext);
       // Util.createDemoUserData(mContext, 30);


        switch (latestPosition){
            case 0:
                fragmentUtil.showFragment(new MessageFragment());
                break;
            case 1:
                fragmentUtil.showFragment(new DetectFragment());
                break;
            case 3:
                fragmentUtil.showFragment(new HistoryFragment());
                break;
            case 4:
                fragmentUtil.showFragment(new UserFragment());
                break;
            default:
                fragmentUtil.showFragment(new DetectFragment());
                break;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasPermissionsGranted(PERMISSIONS)) {
            requestVideoPermissions();
        }


        BluetoothLeWrapper.getInstance().onCreateProcess(this);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initTabBarAnimView();

        initializeAWS();

    }


    private void initializeAWS() {
        AWSConfiguration awsConfiguration = new AWSConfiguration(this);

        if (IdentityManager.getDefaultIdentityManager() == null) {
            final IdentityManager identityManager = new IdentityManager(getApplicationContext(), awsConfiguration);
            IdentityManager.setDefaultIdentityManager(identityManager);
        }


        FacebookSignInProvider.setPermissions("public_profile");

        // Add Facebook as an SignIn Provider.
        IdentityManager.getDefaultIdentityManager().addSignInProvider(FacebookSignInProvider.class);

        GoogleSignInProvider.setPermissions(Scopes.EMAIL, Scopes.PROFILE);

        // Add Google as an SignIn Provider.
        IdentityManager.getDefaultIdentityManager().addSignInProvider(GoogleSignInProvider.class);


        AWSMobileClient.getInstance().initialize(this).execute();
    }


    @Override
    protected void onStop() {
        super.onStop();
        BluetoothLeWrapper.getInstance().onStopProcess();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothLeWrapper.getInstance().onDestroyProcess();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BluetoothLeWrapper.getInstance().onActivityResultProcess(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        tabBarAnimView = (TabBarView) findViewById(R.id.tabBarAnimView);
    }

    private void initListener() {
        tabBarAnimView.setOnTabBarClickListener(onTabBarClickListener);
    }



    private void initTabBarAnimView() {
        tabBarAnimView.setMainBitmap(R.drawable.icon_plus);
        tabBarAnimView.bindBtnsForPage(0, R.drawable.icon_message, 0, 0);
        tabBarAnimView.bindBtnsForPage(1, R.drawable.icon_detect, 0, 0);
        tabBarAnimView.bindBtnsForPage(2, R.drawable.icon_history, 0, 0);
        tabBarAnimView.bindBtnsForPage(3, R.drawable.icon_user, 0, 0);
        tabBarAnimView.initializePage(1); // default on detect page
    }

    private TabBarView.OnTabBarClickListener onTabBarClickListener = new TabBarView.OnTabBarClickListener() {

        @Override
        public void onMainBtnsClick(int position, int[] clickLocation) {
            Log.e(TAG, "click on ---> " + position);
            latestPosition = position;
            switch (position){
                case 0:
                    fragmentUtil.showFragment(new MessageFragment());
                    break;
                case 1:
                    fragmentUtil.showFragment(new DetectFragment());
                    break;
                case 3:
                    fragmentUtil.showFragment(new HistoryFragment());
                    break;
                case 4:
                    fragmentUtil.showFragment(new UserFragment());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onMainBtnsClick(int position) {
            Log.e(TAG, "center--->" + position);
        }

        @Override
        public void onLeftBtnClick(int page) {
            Log.e(TAG, "left--->" + page);
        }

        @Override
        public void onRightBtnClick(int page) {
            Log.e(TAG, "right--->" + page);
        }

    };



    /**
     *  implement interface IMainAppHandler
     * */
    public int getMainFragmentID(){
        return R.id.mainFragment;
    }


    public void refreshTabBar(){
        if(tabBarAnimView!=null) tabBarAnimView.refreshViews();
    }


    /**
     * Handle Incoming messages from contained fragments.
     */

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(String id) {

    }

    @Override
    public void onFragmentInteraction(int actionId) {

    }



}
