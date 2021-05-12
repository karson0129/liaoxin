package com.hyphenate.liaoxin.section.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ActivationCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class VerificationActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private EaseTitleBar mToolbarRegister;
    private TextView tvPhoneNum;
    private ActivationCodeBox activationCode;
    private TextView tvAgain;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, VerificationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_verification;
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
                if (code.equals("1234")) {
                    //激活成功
                    ToastUtils.showSuccessToast("激活成功");
                    activationCode.showTip(false, "");
                } else {
                    //激活失败
                    ToastUtils.showFailToast("激活失败");
                    activationCode.showTip(true, "激活失败");
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
