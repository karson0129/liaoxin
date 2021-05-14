package com.hyphenate.liaoxin.section.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.request.SendCodeRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ActivationCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class VerificationActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private final  String TAG = " VerificationActivity";

    private static String Request = "Request";

    private EaseTitleBar mToolbarRegister;
    private TextView tvPhoneNum;
    private ActivationCodeBox activationCode;
    private TextView tvAgain;
    private RegisterBean request;

    public static void startAction(Context context, RegisterBean codeRequest) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra(Request,codeRequest);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_verification;
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
        tvPhoneNum = findViewById(R.id.tv_phone_num);
        activationCode = findViewById(R.id.activationCode);
        tvAgain = findViewById(R.id.tv_again);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolbarRegister.setOnBackPressListener(this);
        //激活码输入完成
        activationCode.setInputCompleteListener(new ActivationCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {

                //回调进入 Activity
                if (!TextUtils.isEmpty(request.code) && code.equals(request.code)) {
                    //激活成功
                    ToastUtils.showSuccessToast("验证码正确");
                    activationCode.showTip(false, "");
                    SetPasswordActivity.startAction(mContext,request);
                    finish();
                } else {
                    //激活失败
                    ToastUtils.showFailToast("验证码有误");
                    activationCode.showTip(true, "验证码有误");
                }

            }
        });
    }

    @Override
    protected void initSystemFit() {
        super.initSystemFit();
    }

    @Override
    public void onBackPress(View view) {
        finish();
    }


}
