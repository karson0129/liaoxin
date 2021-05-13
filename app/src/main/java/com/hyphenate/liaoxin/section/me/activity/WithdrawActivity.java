package com.hyphenate.liaoxin.section.me.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;


/**
 * 提现
 * */
public class WithdrawActivity extends BaseInitActivity implements  EaseTitleBar.OnBackPressListener,View.OnClickListener{

    private String TAG = "WithdrawActivity";

    private EaseTitleBar titleBar;
    private ImageView ivBank;
    private TextView tvBank;
    private EditText etPay;
    private TextView tvYue;//显示余额
    private TextView btnAction;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        ivBank = findViewById(R.id.iv_bank);
        tvBank = findViewById(R.id.tv_bank);
        etPay = findViewById(R.id.et_pay);
        tvYue = findViewById(R.id.tv_yue);
        btnAction = findViewById(R.id.btn_login);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        ivBank.setOnClickListener(this);
        tvBank.setOnClickListener(this);
        btnAction.setOnClickListener(this);
        etPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_bank:// 银行
            case R.id.tv_bank:

                break;
            case R.id.btn_login:

                break;
        }
    }
}
