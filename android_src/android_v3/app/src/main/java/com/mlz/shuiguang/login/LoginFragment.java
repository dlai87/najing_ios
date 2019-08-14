package com.mlz.shuiguang.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mlz.shuiguang.MainActivity;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.ThemeColor;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.transmission.SyncTaskHandler;
import com.mlz.shuiguang.transmission.bmobObject.User;
import com.mlz.shuiguang.transmission.task.AuthsTask;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;
import com.mlz.shuiguang.util.dialog.ButtonActionHandler;
import com.mlz.shuiguang.util.dialog.EmailDialog;
import com.mlz.shuiguang.util.dialog.ForgetPasswordDialog;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.mlz.shuiguang.util.widget.button.NJButton;
import com.mlz.shuiguang.util.widget.dialog.NJProgressDialog;

import java.io.File;
import java.util.HashMap;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * Created by dehualai on 1/8/17.
 */

public class LoginFragment extends Fragment {

    String TAG = "LoginFragment";
    Context mContext;
    FragmentUtil fragmentUtil;
    IMainAppHandler mainAppHandler;

    View view;
    EditText usernameField;
    EditText passwordField;
    Button forgetPasswordButton;
    NJButton loginButton;
    Button signUpButton;
    LinearLayout usernameLayout;
    LinearLayout passwordLayout;
    LinearLayout usernameIcon;
    LinearLayout passwordIcon;
    LinearLayout bgLayout;

    TextView errorMessage;

    NJProgressDialog progressDialog;

    String username;
    String password;


    final int NO_ERROR = 9999;
    final int ERROR_FIELD_EMPTY = 5000;

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
        refreshViews();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_login, container, false);
        Bundle args = getArguments();
        keyboardAdvance(view);

        assignViews();
        addActionToViews();


        return view;
    }


    private void refreshViews() {
        ThemeColor theme = Util.getActiveTheme();
        bgLayout.setBackgroundColor(theme.getMainColor(mContext));
        loginButton.updateTheme(theme.getMinorColor(mContext),
                theme.getMinorColor(mContext),
                theme.getMinorColor2(mContext));
        usernameIcon.setBackgroundColor(theme.getMinorColor(mContext));
        passwordIcon.setBackgroundColor(theme.getMinorColor(mContext));

    }


    private void assignViews(){
        bgLayout = (LinearLayout)view.findViewById(R.id.bg_layout);
        usernameLayout = (LinearLayout)view.findViewById(R.id.username_layout);
        passwordLayout = (LinearLayout)view.findViewById(R.id.password_layout);
        usernameField = (EditText)view.findViewById(R.id.input_username);
        passwordField = (EditText)view.findViewById(R.id.input_password);
        forgetPasswordButton = (Button)view.findViewById(R.id.forget_password_button);
        loginButton = (NJButton) view.findViewById(R.id.login_button);
        errorMessage = (TextView)view.findViewById(R.id.error_message);
        signUpButton = (Button)view.findViewById(R.id.sing_up_button);
        usernameIcon = (LinearLayout)view.findViewById(R.id.username_icon);
        passwordIcon = (LinearLayout)view.findViewById(R.id.password_icon);

    }

    private void addActionToViews(){

        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPasswordDialog dialog = new ForgetPasswordDialog(mContext);
                dialog.setTitleView(mContext.getResources().getString(R.string.please_enter_your_email));
                dialog.setMessageView(mContext.getResources().getString(R.string.we_will_send_you_a_password_reset_via_email));
                dialog.setButtons(mContext.getResources().getString(R.string.cancel),
                        mContext.getResources().getString(R.string.ok),
                        null);
                dialog.showDialog();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verifyInputFields() == NO_ERROR){

                    progressDialog = NJProgressDialog.show(mContext);


                    AuthsTask authsTask = new AuthsTask(mContext);
                    HashMap params = new HashMap();
                    params.put(AuthsTask.PARAM_TASK, AuthsTask.TASK_LOGIN);
                    params.put(AuthsTask.PARAM_AUTH_TYPE, AuthsTask.TYPE_PWD);
                    params.put(AuthsTask.PARAM_USERNAME, username);
                    params.put(AuthsTask.PARAM_PASSWORD, password);

                    authsTask.setHandler(new SyncTaskHandler() {

                        @Override
                        public void onSuccess(Object object) {
                            User user = (User)object;
                            // get avatar image
                            BmobFile avatar = user.getAvatar();
                            if (avatar!=null){
                                avatar.download(new File(Global.AVATAR_PATH) , new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        Log.e("show", "下载： " + s);
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {
                                        Log.e("show", "正在下载");
                                    }
                                });
                            }

                            // convert Transmission Data to Model
                            UserData.UserBuilder userBuilder = new UserData.UserBuilder(user.getUsername(), user.getObjectId());
                            if (user.getGender()!=null) userBuilder.gender(user.getGender());
                            if (user.getEmail()!=null) userBuilder.email(user.getEmail());
                            if (user.getDate_of_birth()!=null) userBuilder.date_of_birth(user.getDate_of_birth());
                            if (user.getPhoneNumber()!=null) userBuilder.phone(user.getPhoneNumber());
                            if (user.getSkin_type() !=null) userBuilder.skin_type(user.getSkin_type());
                            UserData userData = new UserData(userBuilder);

                            // save model to Database
                            TBUser tbUser = new TBUser();
                            tbUser.smartInsert(DBAdapter.getDatabase(mContext), userData);


                            if (username.equals("demo")){
                                Global.DEMO_MODE = true;
                                Util.createDemoUserData(mContext, 30);
                            }

                            progressDialog.dismiss();


                            // start Intent
                            Intent intent = new Intent(mContext, MainActivity.class);
                            if(intent!=null){
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(intent);
                            }


                        }

                        @Override
                        public void onFailure(String message, int errorCode) {
                            Log.e("show", "message " + message + " | error code | " + errorCode);
                            progressDialog.dismiss();
                            if (errorCode == 101) {
                                errorMessage.setText(mContext.getResources().getString(R.string.error_username_or_password));
                            }else if(errorCode == 9016){
                                errorMessage.setText(mContext.getResources().getString(R.string.error_network));
                            }else {
                                errorMessage.setText(mContext.getResources().getString(R.string.error_login));
                            }
                        }
                    });
                    authsTask.sync(params);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentUtil.showFragment(new SignUpFragment());
            }
        });

    }



    /**
     *
     * Enable dismiss keyboard when touch out-side of the edit box
     *
     * */
    public void keyboardAdvance(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    Util.hideSoftKeyboard((Activity) mContext);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                keyboardAdvance(innerView);
            }
        }
    }


    /**
     *
     * verify input fields
     *
     * */
    private int verifyInputFields(){
        // clean hight light first
        Util.highlightLayout(usernameLayout, false, mContext);
        Util.highlightLayout(passwordLayout, false, mContext);

        // get content
        String username = usernameField.getText().toString().replace(" ", "").toLowerCase();
        String password = passwordField.getText().toString().replace(" ", "");

        if (username == null || username.equals("") ||
                password == null || password.equals("")
                ){
            if (username == null || username.equals("")) Util.highlightLayout(usernameLayout, true, mContext);
            if (password == null || password.equals("")) Util.highlightLayout(passwordLayout, true, mContext);
            errorMessage.setText(mContext.getResources().getString(R.string.please_fill_up_the_form));
            return ERROR_FIELD_EMPTY;
        }
        errorMessage.setText("");
        this.username = username;
        this.password = password;
        return NO_ERROR;

    }

}