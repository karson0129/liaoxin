package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.BaseRequestBean;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.client.JsonUitls;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class AccountSecurityActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "AccountSecurityActivity";

    private EaseTitleBar titleBar;
    private ArrowItemView itemEquipments;
    private ArrowItemView itemNum;
    private ArrowItemView itemPhone;
    private ArrowItemView itemChangePassword;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, AccountSecurityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_account_security;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        itemEquipments = findViewById(R.id.item_equipments);
        itemNum = findViewById(R.id.item_num);
        itemPhone = findViewById(R.id.item_phone);
        itemChangePassword = findViewById(R.id.item_change_password);
    }

    @Override
    protected void initData() {
        super.initData();
        itemNum.getTvContent().setText("ID：123");
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        itemEquipments.setOnClickListener(this);
        itemNum.setOnClickListener(this);
        itemPhone.setOnClickListener(this);
        itemChangePassword.setOnClickListener(this);
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_equipments ://多端多设备管理
                MultiDeviceActivity.actionStart(mContext);
                break;
            case R.id.item_num:

                break;
            case R.id.item_phone://绑定手机号

                break;
            case R.id.item_change_password://修改登录密码

                break;
        }
    }
}
