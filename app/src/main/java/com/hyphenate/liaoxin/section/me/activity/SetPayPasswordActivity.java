package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.CoinPssswordBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ActivationCodeBox;
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

/**
 * 设置支付密码
 */
public class SetPayPasswordActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "SetPayPasswordActivity";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;

    private String mCode;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, SetPayPasswordActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pay_password;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        passwordCodeBox = findViewById(R.id.activationCode);

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                if (!TextUtils.isEmpty(code) && code.length() >=6){
                    AgreePasswordActivity.actionStart(mContext,code);
                    finish();
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

}
