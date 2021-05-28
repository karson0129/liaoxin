package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class ChangePasswordActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "ChangePasswordActivity";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;
    private TextView hint;
    private TextView tv_name;

    private String mCode;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ChangePasswordActivity.class);
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
        hint = findViewById(R.id.hint);
        tv_name = findViewById(R.id.tv_name);
        hint.setText("请输入支付密码，以验证身份");
        tv_name.setText("修改支付密码");
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                if (!TextUtils.isEmpty(code) && code.length() >= 6){
                    ChangePasswordActivity2.actionStart(mContext,code);
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
