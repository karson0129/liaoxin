package com.hyphenate.liaoxin.section.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.MainActivity;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.net.bean.LoginByPhoneBean;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;
import com.hyphenate.liaoxin.common.net.request.SendCodeRequest;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ActivationCodeBox;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginFragmentViewModel;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;

public class VerificationActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private final  String TAG = " VerificationActivity";

    private static String Request = "Request";

    private EaseTitleBar mToolbarRegister;
    private TextView tvPhoneNum;
    private ActivationCodeBox activationCode;
    private TextView tvAgain;
    private RegisterBean request;

    private LoginFragmentViewModel mFragmentViewModel;

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

        mFragmentViewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        mFragmentViewModel.getLoginObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<EaseUser>(true) {
                @Override
                public void onSuccess(EaseUser data) {
                    Log.e("login", "login success");
                    DemoHelper.getInstance().setAutoLogin(true);
                    //跳转到主页
                    MainActivity.startAction(mContext);
                    //发一个Event
                    LiveDataBus.get().with(DemoConstant.APP_LOGIN).postValue(EaseEvent.create(DemoConstant.APP_LOGIN_FINISH, EaseEvent.TYPE.MESSAGE));
                    mContext.finish();
                }

                @Override
                public void onError(int code, String message) {
                    super.onError(code, message);
                    if(code == EMError.USER_AUTHENTICATION_FAILED) {
                        ToastUtils.showToast(R.string.demo_error_user_authentication_failed);
                    }else {
                        ToastUtils.showToast(message);
                    }
                }

                @Override
                public void onLoading(EaseUser data) {
                    super.onLoading(data);
                    showLoading();
                }

                @Override
                public void hideLoading() {
                    super.hideLoading();
                    dismissLoading();

                }
            });

        });
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
//                    SetPasswordActivity.startAction(mContext,request);
//                    finish();
                    loginByPhone();
                } else {
                    //激活失败
                    ToastUtils.showFailToast("验证码有误");
                    activationCode.showTip(true, "验证码有误");
                }

            }
        });
    }

    /**
     * 手机号登录
     * */
    private void loginByPhone(){
        showLoading();
        LoginByPhoneBean bean = new LoginByPhoneBean();
        bean.telephone = request.telephone;
        bean.code = request.code;
        HttpUtils.getInstance().post(mContext, HttpURL.LOGIN_BY_CODE, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"成功："+ str);
                try {
                    BaseRequest<String> request = new Gson().fromJson(str,BaseRequest.class);
                    if (!TextUtils.isEmpty(request.data)){
                        PrefUtils.setString(mContext, UserConstant.Token,request.data);
                        getUserInfo();
                    }
                }catch (Exception e){
                    ToastUtils.showFailToast("解析失败");
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                super.onFailure(call,e,str);
                dismissLoading();
                ToastUtils.showFailToast("登录失败");
            }
        });
    }


    /**
     * 获取当前登录用户
     * */
    private void getUserInfo(){
        showLoading();
        HttpUtils.getInstance().post(mContext,HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"成功："+ str);
                UserInfoRequest request = new Gson().fromJson(str,UserInfoRequest.class);
                Log.d(TAG,"进来了  环信id："+request.data.huanXinId +" | telephone:"+request.data.telephone);
                if (!TextUtils.isEmpty(request.data.huanXinId) && !TextUtils.isEmpty(request.data.telephone) ){
                    Login(request.data.huanXinId,request.data.telephone);
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                super.onFailure(call, e,str);
                dismissLoading();
            }
        });
    }

    /**
     * 登录
     * @param account
     * @param password
     */
    private void Login(String account,String password){
        mFragmentViewModel.login(account, password, false);
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

}
