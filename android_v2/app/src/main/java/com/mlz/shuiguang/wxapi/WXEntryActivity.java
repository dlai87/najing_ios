package com.mlz.shuiguang.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mlz.shuiguang.MainActivity;
import com.mlz.shuiguang.R;
import com.mlz.shuiguang.database.DBAdapter;
import com.mlz.shuiguang.database.operation.TBUser;
import com.mlz.shuiguang.login.LoginActivity2;
import com.mlz.shuiguang.model.UserData;
import com.mlz.shuiguang.util.Global;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by dehualai on 2/1/18.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    String TAG = "LoginActivity2";
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;


    Context mContext ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //如果没回调onResp，八成是这句没有写
        LoginActivity2.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Log.e(TAG, "onReq " + req);
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {

        Log.e(TAG, "错误码 : " + resp.errCode + " |||| " + resp.errStr );
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.e(TAG, "登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                //拿到了微信返回的code,立马再去请求access_token
                String code = ((SendAuth.Resp) resp).code;
                Log.e(TAG, "code = " + code);
                sendRequest(code);
                break;
        }
    }



    private void sendRequest(String code){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.WECHAT_URL + "oauth2/access_token?appid=" +
                Global.WECHAT_APP_ID + "&secret=" +Global.WECHAT_APP_SECRET+ "&code="
                + code + "&grant_type=authorization_code";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject object = new JSONObject(response.toString().trim());
                            String accessToken = object.getString("access_token");
                            String openID = object.getString("openid");
                            String refreshToken = object.getString("refresh_token");
                            long expires_in = object.getLong("expires_in");

                            getUserInfo(accessToken, openID);
                        }catch (Exception e){
                            Log.e(TAG, "");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }


    private void getUserInfo(String accessToken, String openID){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Global.WECHAT_URL + "userinfo?access_token=" + accessToken + "&openid=" + openID;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject object = new JSONObject(response.toString().trim());
                            String nikeName = object.getString("nickname");
                            int sex = object.getInt("sex");
                            String unionID = object.getString("unionid");
                            String image = object.getString("headimgurl");

                            // convert Transmission Data to Model
                            UserData.UserBuilder userBuilder = new UserData.UserBuilder(nikeName, Global.LOGIN_WECHAT_PREFIX + unionID);
                            if (sex>0) {
                                userBuilder.gender(getResources().getString(R.string.male));
                            }else{
                                userBuilder.gender(getResources().getString(R.string.female));
                            }
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


                        }catch (Exception e){
                            Log.e(TAG, "");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }


}
