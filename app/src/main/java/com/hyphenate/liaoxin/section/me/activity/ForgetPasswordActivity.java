package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.net.bean.SendCodeBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.utils.PushUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ActivationCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

/**
 * 忘记密码
 * */
public class ForgetPasswordActivity extends BaseInitActivity {

    private String TAG = "ForgetPasswordActivity";

    private EaseTitleBar titleBar;
    private TextView hint;
    private ActivationCodeBox activationCodeBox;
    private TextView countdown;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        hint = findViewById(R.id.hint);
        activationCodeBox = findViewById(R.id.activationCode);
        countdown = findViewById(R.id.countdown);

        hint.setText("已发送至 +86 " + PrefUtils.getString(mContext, UserConstant.Phone,""));
    }

    @Override
    protected void initListener() {
        super.initListener();
        activationCodeBox.setInputCompleteListener(new ActivationCodeBox.InputCompleteListener() {
            @Override
            public void inputComplete(String code) {

            }
        });
        countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationCode();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        getVerificationCode();
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        showLoading();
        SendCodeBean bean = new SendCodeBean();
        bean.telephone = PrefUtils.getString(mContext, UserConstant.Phone, "");
        bean.type = SendCodeBean.SendCodeType.RetrievePassword;
        Log.i(TAG, "参数：" + new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.SEND_CODE, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG, "成功：" + str);
                ToastUtils.showSuccessToast("获取成功");
                if (timer != null) {
                    timer.start();
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });
    }

    /**
     *  计时器
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            countdown.setEnabled(false);
            countdown.setText((millisUntilFinished / 1000) + "秒后可重发");
        }

        @Override
        public void onFinish() {
            countdown.setEnabled(true);
            countdown.setText("获取验证码");
        }
    };


}
