package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.net.bean.ChangeCoinPasswordBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.PasswordCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

public class ChangePasswordActivity3 extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "ChangePasswordActivity3";

    private EaseTitleBar titleBar;
    private PasswordCodeBox passwordCodeBox;
    private TextView hint;
    private TextView tv_name;
    private TextView action;

    private String mOldCode;
    private String mNewCode;
    private String mCode;

    public static void actionStart(Context context, String oldCode,String newCode) {
        Intent starter = new Intent(context, ChangePasswordActivity3.class);
        starter.putExtra("oldCode",oldCode);
        starter.putExtra("newCode",newCode);
        context.startActivity(starter);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mOldCode = getIntent().getStringExtra("oldCode");
        mNewCode = getIntent().getStringExtra("newCode");
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
        action = findViewById(R.id.action);

        action.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mCode)){
                    changePassword();
                }else {
                    ToastUtils.showToast("请输入密码");
                }
            }
        });
        passwordCodeBox.setInputCompleteListener(new PasswordCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {
                if (!TextUtils.isEmpty(code) && code.equals(mNewCode)){
                    mCode = code;
                    action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
                    action.setTextColor(mContext.getResources().getColor(R.color.white));
                }else {
                    ToastUtils.showFailToast("两次密码不对");
                }
            }
        });
    }

    /**
     *  获取用户信息
     * */
    private void changePassword(){
        showLoading();
        ChangeCoinPasswordBean bean = new ChangeCoinPasswordBean();
        bean.newCoinPsssword = mCode;
        bean.oldCoinPassword = mOldCode;
        Log.i(TAG,"参数：" + new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.CHANGE_COIN_PASSWORD, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG,"修改支付密码:"+str);
                ToastUtils.showSuccessToast("修改成功");
                finish();
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e,str);
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
