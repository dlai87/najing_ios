package com.mlz.shuiguang.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mlz.shuiguang.MainActivity;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.dialog.AlertDialog;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Arrays;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by dehualai on 1/31/18.
 */

public class LoginActivity2 extends Activity {

    String TAG = "LoginActivity2";
    Context mContext;

    private FirebaseAuth mAuth;
    public static IWXAPI mWxApi;
    CallbackManager mCallbackManager;  // facebook
    GoogleSignInClient mGoogleSignInClient;  // google



    Button wechatLoginButton;
    Button googleLoginButton;
    Button facebookLoginButton;
    Button useWithoutLoginButton;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        mContext = this;

        // wechat
        registToWX();

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // facebook
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.i(TAG, "Login " );
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }
                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel");
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        Log.i(TAG, "exception " + exception.getMessage());

                    }
                });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        assignViews();
        addActionToViews();

    }



    private void assignViews(){
        wechatLoginButton = (Button)findViewById(R.id.wechat_login_button);
        googleLoginButton = (Button)findViewById(R.id.google_login_button);
        facebookLoginButton = (Button)findViewById(R.id.facebook_login_button);
        useWithoutLoginButton = (Button)findViewById(R.id.without_login_button);
    }

    private void addActionToViews(){
        wechatLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "button pressed");
                if (!mWxApi.isWXAppInstalled()) {
                    Log.e(TAG, "Wechat not install yet");
                    AlertDialog dialog = new AlertDialog(mContext);
                    dialog.setTitleView("");
                    dialog.setMessageView(mContext.getResources().getString(R.string.wechat_not_installed_yet));
                    dialog.setButtons(mContext.getResources().getString(R.string.ok), null, null);
                    dialog.showDialog();
                    return;
                }
                final SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "shuiguang_wx_login";
                mWxApi.sendReq(req);

            }
        });
        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e(TAG, "google button pressed");
                Global.login_method = Global.LOGIN_GOOGLE;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, Global.LOGIN_GOOGLE);
            }
        });
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "facebook button pressed");
                Global.login_method = Global.LOGIN_FACEBOOK;
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity2.this, Arrays.asList("public_profile", "email"));
            }
        });
        useWithoutLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent targetIntent = new Intent(mContext, BasicInfoActivity.class);
                if(targetIntent!=null){
                    targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(targetIntent);
                }
            }
        });
    }


    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, getResources().getString(R.string.wechat_app_id), false);
        // 将该app注册到微信
        mWxApi.registerApp(getResources().getString(R.string.wechat_app_id));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(TAG, "onActivityResult " + requestCode + " Global.login_method " + Global.login_method );
        switch (Global.login_method){
            case Global.LOGIN_GOOGLE:
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleGoogleSignInResult(task);
                break;
            case Global.LOGIN_FACEBOOK:
                // Pass the activity result back to the Facebook SDK
                mCallbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }

    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.e(TAG, "Google :success" + account.getDisplayName() + " " + account.getEmail() + " " + account.getId());

            // convert Transmission Data to Model
            UserData.UserBuilder userBuilder = new UserData.UserBuilder(account.getDisplayName(), Global.LOGIN_GOOGLE_PREFIX + account.getId());
            userBuilder.email(account.getEmail());

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

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.e(TAG, "Sign in exception " + e.getMessage());
        }
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.e(TAG, "facebook: on Complete " + user.getDisplayName() +
                                    " " + user.getEmail() + " " + user.getUid() );

                            // convert Transmission Data to Model
                            UserData.UserBuilder userBuilder = new UserData.UserBuilder(user.getDisplayName(), Global.LOGIN_FACEBOOK_PREFIX + user.getUid());
                            userBuilder.email(user.getEmail());

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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity2.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "facebook: on fail ");
                        }

                    }
                });
    }





}
