package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class ForgetPasswordActivity1 extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "ForgetPasswordActivity1";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;

    private String mCode;

    public static void actionStart(Context context,String mCode) {
        Intent starter = new Intent(context, ForgetPasswordActivity1.class);
        starter.putExtra("code",mCode);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_pay_password;
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mCode = getIntent().getStringExtra("code");
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
                if (!TextUtils.isEmpty(code) && code.length() >= 6){
                    ForgetPasswordActivity2.actionStart(mContext,mCode,code);
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
