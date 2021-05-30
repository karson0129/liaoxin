package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.BindBankBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.SystemBankRequst;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;


/**
 * 添加银行卡
 */
public class AddBankActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, TextWatcher {

    private String TAG = "AddBankActivity";

    private EaseTitleBar titleBar;
    private EditText etName;
    private EditText etCode;
    private TextView tvType;
    private EditText etPhone;
    private TextView action;

    private String mName;
    private String mCode;
    private String mType;
    private String mPhone;
    private String mPassword;

    private Drawable clear;

    public static void actionStart(Context context,String password) {
        Intent starter = new Intent(context, AddBankActivity.class);
        starter.putExtra("password",password);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bank;
    }


    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mPassword = getIntent().getStringExtra("password");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        etName = findViewById(R.id.et_name);
        etCode = findViewById(R.id.et_code);
        tvType = findViewById(R.id.tv_type);
        etPhone = findViewById(R.id.et_phone);
        action = findViewById(R.id.action);
    }


    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        etName.addTextChangedListener(this);
        etCode.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        EaseEditTextUtils.clearEditTextListener(etName);
        EaseEditTextUtils.clearEditTextListener(etCode);
        EaseEditTextUtils.clearEditTextListener(etPhone);
        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSelectBank();
            }
        });
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contoal(true)){
                    bindBank();
                }
            }
        });
    }

    private void bindBank(){
        showLoading();
        BindBankBean bean = new BindBankBean();
        bean.cardNumber = mCode;
        bean.systemBankdId = mType;
        bean.coinPassword = mPassword;
        Log.i(TAG,"参数："+new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.BIND_BANK, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                finish();
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });

    }

    private void goSelectBank(){
        Intent intent = new Intent(mContext,SelectBankActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            SystemBankRequst.SystemBank bank = (SystemBankRequst.SystemBank) data.getSerializableExtra("bank");
            if (bank != null){
                tvType.setText(bank.name);
                mType = bank.systemBankId;
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);
        EaseEditTextUtils.showRightDrawable(etPhone, clear);
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void afterTextChanged(Editable editable) {
        mName = etName.getText().toString().trim();
        mCode = etCode.getText().toString().trim();
        mPhone = etPhone.getText().toString().trim();

        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);
        EaseEditTextUtils.showRightDrawable(etPhone, clear);
        if (contoal(false)){
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            action.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            action.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    private boolean contoal(boolean isShow){
        if (TextUtils.isEmpty(mName)){
            if (isShow)
            ToastUtils.showToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(mCode)){
            if (isShow)
            ToastUtils.showToast("请输入卡号");
            return false;
        }
        if (TextUtils.isEmpty(mType)){
            if (isShow)
                ToastUtils.showToast("请选择银行");
            return false;
        }
        if (TextUtils.isEmpty(mPhone)){
            if (isShow)
            ToastUtils.showToast("请输入电话");
            return false;
        }
        return true;
    }
}
