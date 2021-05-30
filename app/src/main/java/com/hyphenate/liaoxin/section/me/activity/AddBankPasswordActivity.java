package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class AddBankPasswordActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "AddBankPasswordActivity";

    private EaseTitleBar titleBar;
    private TextView tvName;
    private TextView hint;
    private PasswordCodeBox passwordCodeBox;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, AddBankPasswordActivity.class);
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
        tvName = findViewById(R.id.tv_name);
        hint = findViewById(R.id.hint);
        passwordCodeBox = findViewById(R.id.activationCode);
        tvName.setText("添加银行卡");
        hint.setText("请输入支付密码，以验证身份");
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                AddBankActivity.actionStart(mContext,code);
                finish();
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
