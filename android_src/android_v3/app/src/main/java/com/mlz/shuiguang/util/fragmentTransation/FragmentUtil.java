package com.mlz.shuiguang.util.fragmentTransation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by dehualai on 12/24/16.
 */

public class FragmentUtil {
    String TAG = getClass().getSimpleName();
    private Activity activity;

    public FragmentUtil(Activity activity){
        if(activity instanceof IMainAppHandler){
            this.activity = activity;
        }else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }

    public void showFragment(Fragment fragment){
        try {
            if (activity!=null){
                FragmentTransaction transaction = activity.getFragmentManager()
                        .beginTransaction();
                transaction.replace(((IMainAppHandler) activity).getMainFragmentID(), fragment);
                // add the fragment to BackStack , the when we need to pop back, just call FragmentManager.popBackStack()
                transaction.addToBackStack(null);
                //transaction.commit();
                transaction.commitAllowingStateLoss();
            }
        }catch (Exception e){
            Log.e(TAG, "exception " + e.getMessage());
        }

    }


    public void back(){
        try {
            FragmentManager fm = activity.getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
                // super.onBackPressed();
            }
        }catch (Exception e){
            Log.e(TAG, "exception " + e.getMessage());
        }

    }
}
