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

public class ChangePasswordActivity2 extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "ChangePasswordActivity2";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;
    private TextView hint;
    private TextView tv_name;

    private String mOldCode;

    public static void actionStart(Context context,String oldCode) {
        Intent starter = new Intent(context, ChangePasswordActivity2.class);
        starter.putExtra("oldCode",oldCode);
        context.startActivity(starter);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mOldCode = getIntent().getStringExtra("oldCode");
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
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                if (!TextUtils.isEmpty(code) && code.length() >= 6){
                    ChangePasswordActivity3.actionStart(mContext,mOldCode,code);
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
