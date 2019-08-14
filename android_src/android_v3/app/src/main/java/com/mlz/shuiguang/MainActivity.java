package com.mlz.shuiguang;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

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
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.mlz.shuiguang.util.widget.tabbar.TabBarView;

import java.util.HashMap;

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
        BluetoothLeWrapper.getInstance().onCreateProcess(this);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initTabBarAnimView();
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
