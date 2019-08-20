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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mlz.shuiguang.R;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

/**
 * Created by dehualai on 1/27/18.
 */

public class BasicInfoFragment extends Fragment {

    String TAG = "BasicInfoFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    View view;

    Button startAppButton;
    Button cancelButton;

    EditText usernameField;
    EditText emailField;
    LinearLayout usernameLayout;
    LinearLayout emailLayout;

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
        view = inflater.inflate(R.layout.fragment_basic_info, container, false);
        Bundle args = getArguments();

        assignViews();
        addActionToViews();


        return view;
    }



    private void assignViews(){

        startAppButton = (Button)view.findViewById(R.id.start_using_app_button);
        cancelButton = (Button)view.findViewById(R.id.back_to_login_button);
        usernameField = (EditText)view.findViewById(R.id.input_username);
        emailField = (EditText)view.findViewById(R.id.input_email);
        usernameLayout = (LinearLayout)view.findViewById(R.id.username_layout);
        emailLayout = (LinearLayout)view.findViewById(R.id.email_layout);

    }

    private void addActionToViews(){

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentUtil.showFragment(new LoginFragment2());
            }
        });
    }

}
