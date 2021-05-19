package com.hyphenate.liaoxin.section.login.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.MainActivity;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;
import com.hyphenate.liaoxin.common.net.request.SendCodeRequest;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginFragmentViewModel;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 设置昵称
 * */
public class SetNicknameActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "SetNicknameActivity";

    private static String Request = "Request";

    private LoginFragmentViewModel mFragmentViewModel;

    private EaseTitleBar mToolbarRegister;
    private EditText etLoginPassword;
    private TextView btnLogin;
    private TextView tvTitle;
    private Drawable clear;

    private String mNickName;

    private RegisterBean request;

    public static void startAction(Context context,RegisterBean request) {
        Intent intent = new Intent(context, SetNicknameActivity.class);
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
        tvTitle.setText("设置昵称");
        etLoginPassword.setHint("请输入昵称");
        btnLogin.setText("完成");
        etLoginPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT);
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
                mNickName = etLoginPassword.getText().toString().trim();
                EaseEditTextUtils.showRightDrawable(etLoginPassword, clear);
            }
        });
        btnLogin.setOnClickListener(this);
        EaseEditTextUtils.clearEditTextListener(etLoginPassword);
    }


    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etLoginPassword, clear);

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
    public void onBackPress(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if (!TextUtils.isEmpty(mNickName)){
                    getRegister();
                }
                break;
        }
    }

    /**
     * 注册
     */
    private void getRegister(){
        request.nickName = mNickName;
        Log.i(TAG,"参数："+ new Gson().toJson(request));
        HttpUtils.getInstance().post(mContext,HttpURL.RESGIER_CLIENT, new Gson().toJson(request), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.d(TAG,"成功："+ str);
                try {
                    BaseRequest<String> request = new Gson().fromJson(str,BaseRequest.class);
                    if (!TextUtils.isEmpty(request.data)){
                        PrefUtils.setString(mContext, UserConstant.Token,request.data);
                        getUserInfo();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                super.onFailure(call, e,str);
                Log.d(TAG,"失败："+ e.toString());
            }
        });
    }

    /**
     * 获取当前登录用户
     * */
    private void getUserInfo(){
        HttpUtils.getInstance().post(mContext,HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
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
                Log.d(TAG,"失败：");
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
}
