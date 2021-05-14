package com.hyphenate.liaoxin.section.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 设置登录密码
 * */
public class SetPasswordActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "SetPasswordActivity";


    private static String Request = "Request";

    private EaseTitleBar mToolbarRegister;
    private EditText etLoginPassword;
    private TextView btnLogin;
    private TextView tvTitle;

    private String mPassword;
    private Drawable eyeClose;
    private Drawable eyeOpen;

    private RegisterBean request;

    public static void startAction(Context context,RegisterBean request) {
        Intent intent = new Intent(context, SetPasswordActivity.class);
        intent.putExtra(Request,request);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        request = (RegisterBean) intent.getSerializableExtra(Request);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolbarRegister = findViewById(R.id.toolbar_register);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("设置密码");
    }


    @Override
    protected void initListener() {
        super.initListener();
        mToolbarRegister.setOnBackPressListener(this);
        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword = etLoginPassword.getText().toString().trim();
                checkEditContent();
            }
        });
        btnLogin.setOnClickListener(this);
    }

    private void checkEditContent() {
        EaseEditTextUtils.showRightDrawable(etLoginPassword, eyeClose);
    }

    @Override
    protected void initData() {
        super.initData();
        eyeClose = getResources().getDrawable(R.drawable.d_pwd_hide);
        eyeOpen = getResources().getDrawable(R.drawable.d_pwd_show);
        EaseEditTextUtils.changePwdDrawableRight(etLoginPassword, eyeClose, eyeOpen, null, null, null);
    }

    @Override
    public void onBackPress(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
            if (!TextUtils.isEmpty(mPassword)){
                request.password = mPassword;
                SetNicknameActivity.startAction(mContext,request);
                finish();
            }
            break;
        }
    }
}
