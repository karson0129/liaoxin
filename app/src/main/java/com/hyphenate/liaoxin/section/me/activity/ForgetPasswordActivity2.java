package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

public class ForgetPasswordActivity2 extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "ForgetPasswordActivity2";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;
    private TextView hint;
    private TextView action;

    private String mCode;
    private String mPassword;

    public static void actionStart(Context context, String mCode,String password) {
        Intent starter = new Intent(context, ForgetPasswordActivity2.class);
        starter.putExtra("code",mCode);
        starter.putExtra("password",password);
        context.startActivity(starter);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mCode = getIntent().getStringExtra("code");
        mPassword = getIntent().getStringExtra("password");
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
        hint = findViewById(R.id.hint);
        action = findViewById(R.id.action);

        hint.setText("请再次填写以确认");
        action.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassword();
            }
        });
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                Log.i(TAG,"code :"+code);
                if (!TextUtils.isEmpty(code) && code.equals(mPassword)){
                    action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
                    action.setTextColor(mContext.getResources().getColor(R.color.white));
                }else {
                    action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
                    action.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
                }
            }
        });
    }

    private void setPassword(){
        showLoading();
        CoinPssswordBean bean = new CoinPssswordBean();
        bean.coinPsssword = mCode;
        Log.i(TAG,"参数："+new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.SET_COIN_PASSWORD, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                ToastUtils.showToast("设置成功");
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }
}
