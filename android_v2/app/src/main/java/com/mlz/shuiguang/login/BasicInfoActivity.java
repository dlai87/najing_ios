package com.mlz.shuiguang.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.Util;

/**
 * Created by dehualai on 1/31/18.
 */

public class BasicInfoActivity extends Activity {

    final int NO_ERROR = 9999;
    final int ERROR_FIELD_EMPTY = 5000;
    final int ERROR_INVALID_EMAIL_FORMAT = 5001;

    Context mContext;

    Button startAppButton;
    Button cancelButton;

    EditText usernameField;
    EditText emailField;
    LinearLayout usernameLayout;
    LinearLayout emailLayout;

    TextView errorView;
    String username;
    String email;

    @Override
    public void onStart(){
        super.onStart();
        mContext = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);


        assignViews();
        addActionToViews();

    }


    private void assignViews(){

        startAppButton = (Button)findViewById(R.id.start_using_app_button);
        cancelButton = (Button)findViewById(R.id.back_to_login_button);
        usernameField = (EditText)findViewById(R.id.input_username);
        emailField = (EditText)findViewById(R.id.input_email);
        usernameLayout = (LinearLayout)findViewById(R.id.username_layout);
        emailLayout = (LinearLayout)findViewById(R.id.email_layout);
        errorView = (TextView)findViewById(R.id.error_message);

    }

    private void addActionToViews(){

        startAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyInputFields() == NO_ERROR){
                    // convert Transmission Data to Model
                    UserData.UserBuilder userBuilder = new UserData.UserBuilder(username, Global.LOGIN_RANDOM_PREFIX + Util.generateRandomString(16));
                    userBuilder.email(email);

                    UserData userData = new UserData(userBuilder);

                    // save model to Database
                    TBUser tbUser = new TBUser();
                    tbUser.smartInsert(DBAdapter.getDatabase(mContext), userData);


                    // start Intent
                    Intent intent = new Intent(mContext, MainActivity.class);
                    if(intent!=null){
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(intent);
                    }

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetIntent = new Intent(mContext, LoginActivity2.class);
                if(targetIntent!=null){
                    targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(targetIntent);
                }

            }
        });
    }




    // verify input fields
    private int verifyInputFields(){
        // clean hight light first
        Util.highlightLayout(usernameLayout, false, mContext);
        Util.highlightLayout(emailLayout, false, mContext);

        // get content
        String username = usernameField.getText().toString().replace(" ", "").toLowerCase();
        String email = emailField.getText().toString().replace(" ", "").toLowerCase();

        if (username == null || username.equals("") ||
                email == null || email.equals("")
                ){
            if (username == null || username.equals("")) Util.highlightLayout(usernameLayout, true, mContext);
            if (email == null || email.equals("")) Util.highlightLayout(emailLayout, true, mContext);
            errorView.setText(mContext.getResources().getString(R.string.please_fill_up_the_form));
            return ERROR_FIELD_EMPTY;
        }
        if (!Util.validateEmailFormat(email)){
            Util.highlightLayout(emailLayout, true, mContext);
            errorView.setText(mContext.getResources().getString(R.string.incorrect_email_format));
            return ERROR_INVALID_EMAIL_FORMAT;
        }

        errorView.setText("");
        this.username = username;
        this.email = email;

        return NO_ERROR;

    }



}
