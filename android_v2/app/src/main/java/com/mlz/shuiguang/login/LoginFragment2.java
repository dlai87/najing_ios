package com.mlz.shuiguang.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 1/27/18.
 */

public class LoginFragment2 extends Fragment {

    String TAG = "LoginFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    View view;

    Button wechatLoginButton;
    Button googleLoginButton;
    Button facebookLoginButton;
    Button useWithoutLoginButton;
    LoginButton loginButton;
    CallbackManager callbackManager;

    SignInInterface iSignIn ;

    public void setSignInInterface(SignInInterface i){
        this.iSignIn = i;
    }

    public void setFacebookCallbackManager(CallbackManager callbackManager){
        this.callbackManager = callbackManager;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        mContext = activity;
        fragmentUtil = new FragmentUtil(activity);
        if (activity instanceof IMainAppHandler) {
            this.mainAppHandler = (IMainAppHandler) activity;
        } else {
            Log.e(TAG, "The input activity must implement interface IMainAppHandler");
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_login2, container, false);
        Bundle args = getArguments();

        assignViews();
        addActionToViews();


        return view;
    }



    private void assignViews(){
        wechatLoginButton = (Button)view.findViewById(R.id.wechat_login_button);
        googleLoginButton = (Button)view.findViewById(R.id.google_login_button);
        facebookLoginButton = (Button)view.findViewById(R.id.facebook_login_button);
        useWithoutLoginButton = (Button)view.findViewById(R.id.without_login_button);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);

    }

    private void addActionToViews(){

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSignIn!=null) iSignIn.googleSignIn();
            }
        });

        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSignIn!=null) iSignIn.facebookSignIn();
            }
        });
        useWithoutLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentUtil.showFragment(new BasicInfoFragment());
            }
        });


        //loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iSignIn!=null) iSignIn.facebookSignIn();
            }
        });



    }




}