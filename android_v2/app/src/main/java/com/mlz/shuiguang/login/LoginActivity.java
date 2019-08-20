package com.mlz.shuiguang.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.util.Global;
import com.mlz.shuiguang.util.fragmentTransation.FragmentUtil;
import com.mlz.shuiguang.util.fragmentTransation.IMainAppHandler;

import javax.microedition.khronos.opengles.GL;

import cn.bmob.v3.Bmob;

/**
 * Created by dehualai on 1/8/17.
 */

public class LoginActivity extends Activity implements IMainAppHandler, SignInInterface {


    static final int GOOGLE_SIGN_IN = 980;
    static final int FACEBOOK_SIGN_IN = 999;
    String TAG = "LoginActivity";
    FragmentUtil fragmentUtil;
    Context mContext;
    GoogleSignInClient mGoogleSignInClient;

    CallbackManager facebookCallbackManager ; //= CallbackManager.Factory.create();


    private FirebaseAuth mAuth;


    @Override
    public void onStart(){
        super.onStart();
        mContext = this;
        DBAdapter.getDatabase(mContext);
        fragmentUtil = new FragmentUtil(this);


        LoginFragment2 fragment = new LoginFragment2();
        fragment.iSignIn = this;
        fragment.setFacebookCallbackManager(facebookCallbackManager);
        fragmentUtil.showFragment(fragment);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);


        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        // FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i(TAG, "currentUser ==1==" + account);


        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.i("show", "activity callback onSucdess");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.i("show", "activity callback onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i("show", "activity callback onError");
                    }
                });


        // initial Bmob SDK
       // Bmob.initialize(this, Global.Bmob_AppId);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

      //   FirebaseApp.initializeApp(this);
       // mAuth = FirebaseAuth.getInstance();

        // Build a GoogleSignInClient with the options specified by gso.
        //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }




    // implement interface IMainAppHandler
    public int getMainFragmentID(){
        return R.id.loginFragment;
    }

    public void onBackPressed(){

    }

    public void refreshTabBar(){}


    @Override
    public void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    public void facebookSignIn(){

        Intent signInIntent = new Intent(mContext, FacebookActivity.class);
        startActivityForResult(signInIntent, FACEBOOK_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("show", "onActivityResult ==== " + Global.login_method + "  requestCode " + requestCode);

        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        if (requestCode == FACEBOOK_SIGN_IN){
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);

        }

        /*
        Log.e("show", "login method ==== " + Global.login_method);
        switch (Global.login_method){
            case Global.LOGIN_GOOGLE:
                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

                break;
            case Global.LOGIN_FACEBOOK:

                break;
        }
        */




    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Log.e(TAG, "signInWithCredential:success" + account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
            Log.e(TAG, "Sign in exception " + e.getMessage());
        }
    }

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                          //  Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }

                        // ...
                    }
                });
    }

    */

}
