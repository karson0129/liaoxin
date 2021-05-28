package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

/**
 * 认证成功
 * */
public class CertificationSuccessActivity extends BaseInitActivity {

    private String TAG = "CertificationSuccessActivity";

    private TextView action;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, CertificationSuccessActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_success;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        action = findViewById(R.id.action);
    }

    @Override
    protected void initListener() {
        super.initListener();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetPayPasswordActivity.actionStart(mContext);
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        initUserInfo();
    }

    /**
     *  获取用户信息
     * */
    private void initUserInfo(){
        showLoading();
        HttpUtils.getInstance().post(mContext, HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG,"自己的用户信息:"+str);
                UserInfoRequest request = new Gson().fromJson(str,UserInfoRequest.class);
                PrefUtils.setString(mContext, UserConstant.ClientId,request.data.clientId);
                PrefUtils.setBoolean(mContext, UserConstant.isCoinPassword,request.data.isSetCoinPassword);
                PrefUtils.setBoolean(mContext, UserConstant.isRealAuth,request.data.isRealAuth);
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e,str);
            }
        });
    }
}
