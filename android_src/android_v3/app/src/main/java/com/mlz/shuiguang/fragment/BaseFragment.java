package com.mlz.shuiguang.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 1/20/17.
 */

public class BaseFragment extends Fragment {


    protected Context mContext;
    protected FragmentUtil fragmentUtil;
    protected IMainAppHandler mainAppHandler;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

        fragmentUtil = new FragmentUtil(activity);
        if(activity instanceof IMainAppHandler){
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e("Exception", "The input activity must implement interface IMainAppHandler");
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * mFragment to allow an interaction in this mFragment to be communicated
     * to the mActivity and potentially other fragments contained in that
     * mActivity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void onFragmentInteraction(String id);
        public void onFragmentInteraction(int actionId);
    }
}
