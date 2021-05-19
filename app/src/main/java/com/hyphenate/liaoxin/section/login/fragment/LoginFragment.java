package com.hyphenate.liaoxin.section.login.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.MainActivity;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.DemoDbHelper;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.bean.SendCodeBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;
import com.hyphenate.liaoxin.common.net.request.SendCodeRequest;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.common.utils.AesUtil;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitFragment;
import com.hyphenate.liaoxin.section.login.activity.VerificationActivity;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginFragmentViewModel;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginViewModel;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginFragment extends BaseInitFragment implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener, TextView.OnEditorActionListener {

    private String TAG = "LoginFragment";

    private EditText mEtLoginName;
    private EditText mEtLoginPwd;
    private TextView mTvLoginRegister;
    private TextView mTvLoginToken;
    private TextView mTvLoginServerSet;
    private TextView mBtnLogin;
    private CheckBox cbSelect;
    private TextView tvAgreement;
    private TextView tvAccountLogin;
    private TextView tvPassword;
    private TextView tvRegister;
    private String mUserName;
    private String mPwd;
    private LoginViewModel mViewModel;
    private boolean isTokenFlag;//是否是token登录
    private LoginFragmentViewModel mFragmentViewModel;
    private Drawable clear;
    private Drawable eyeOpen;
    private Drawable eyeClose;
    private boolean isClick;
//    private TextView tvVersion;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvRegister = findViewById(R.id.register);
        tvPassword = findViewById(R.id.password);
        tvAccountLogin = findViewById(R.id.account_login);
        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mTvLoginRegister = findViewById(R.id.tv_login_register);
        mTvLoginToken = findViewById(R.id.tv_login_token);
        mTvLoginServerSet = findViewById(R.id.tv_login_server_set);
        mBtnLogin = findViewById(R.id.btn_login);
        tvAgreement = findViewById(R.id.tv_agreement);
        cbSelect = findViewById(R.id.cb_select);
//        tvVersion = findViewById(R.id.tv_version);
        // 保证切换fragment后相关状态正确
        boolean enableTokenLogin = DemoHelper.getInstance().getModel().isEnableTokenLogin();
        mTvLoginToken.setVisibility(enableTokenLogin ? View.VISIBLE : View.GONE);
        //暂时取消回显
//        if(!TextUtils.isEmpty(DemoHelper.getInstance().getCurrentLoginUser())) {
//            mEtLoginName.setText(DemoHelper.getInstance().getCurrentLoginUser());
//        }
//        tvVersion.setText("V"+ EMClient.VERSION);
        if(isTokenFlag) {
            switchLogin();
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mEtLoginName.addTextChangedListener(this);
        mEtLoginPwd.addTextChangedListener(this);
        mTvLoginRegister.setOnClickListener(this);
        mTvLoginToken.setOnClickListener(this);
        mTvLoginServerSet.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        cbSelect.setOnCheckedChangeListener(this);
        tvAccountLogin.setOnClickListener(this);
        tvPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        mEtLoginPwd.setOnEditorActionListener(this);
        EaseEditTextUtils.clearEditTextListener(mEtLoginName);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mFragmentViewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        mFragmentViewModel.getLoginObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<EaseUser>(true) {
                @Override
                public void onSuccess(EaseUser data) {
                    Log.e("login", "login success");
                    DemoHelper.getInstance().setAutoLogin(true);
                    //跳转到主页
                    MainActivity.startAction(mContext);
                    mContext.finish();
                }

                @Override
                public void onError(int code, String message) {
                    super.onError(code, message);
                    Log.e("login", "code : "+code +" | message:"+message);
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
                    LoginFragment.this.dismissLoading();
                }
            });

        });
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel = new ViewModelProvider(mContext).get(LoginViewModel.class);
        mViewModel.getRegisterObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<String>(true) {
                @Override
                public void onSuccess(String data) {
                    mEtLoginName.setText(TextUtils.isEmpty(data)?"":data);
                    mEtLoginPwd.setText("");
                }
            });

        });
        mViewModel.getMessageChangeObservable().with(DemoConstant.APP_LOGIN, EaseEvent.class).observe(this, event -> {
            if(TextUtils.equals(event.event, DemoConstant.APP_LOGIN_FINISH)) {
                mContext.finish();
            }
        });
        DemoDbHelper.getInstance(mContext).getDatabaseCreatedObservable().observe(getViewLifecycleOwner(), response -> {
            Log.i("login", "本地数据库初始化完成");
        });
    }

    @Override
    protected void initData() {
        super.initData();
        tvAgreement.setText(getSpannable());
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        //切换密码可见不可见的两张图片
        eyeClose = getResources().getDrawable(R.drawable.d_pwd_hide);
        eyeOpen = getResources().getDrawable(R.drawable.d_pwd_show);
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwd, eyeClose, eyeOpen, null, null, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login_register ://账号登录
            case R.id.account_login :
                mViewModel.clearRegisterInfo();
                mViewModel.setPageSelect(1);
                break;
            case R.id.tv_login_token:
                isTokenFlag = !isTokenFlag;
                switchLogin();
                break;
            case R.id.tv_login_server_set://忘记密码
            case R.id.password:
                mViewModel.setPageSelect(2);
                break;
            case R.id.btn_login://登录
                if (!TextUtils.isEmpty(mUserName)) {
                    hideKeyboard();
//                loginToServer();
                    getVerificationCode();
                }
//                PrefUtils.setString(mContext, UserConstant.Token,"d7ec89d18e39348b48cf75e5579030c8");
//                PrefUtils.setString(mContext, UserConstant.Token,"47ad53d34aeb87d2e727dca6f7540cd8");
//                getUserInfo();
//                mFragmentViewModel.login("RQgTiYsznVfNZqK", "18620485183", isTokenFlag);
//                mFragmentViewModel.login("QsIDZqgVTvHKayV", "13533358782", isTokenFlag);
//                mFragmentViewModel.login("sHyCbKNyzI8EDnR", "13623456789", isTokenFlag);
                break;
            case R.id.register://注册
                mViewModel.setPageSelect(3);
                break;
        }
    }

    /**
     * 获取当前登录用户
     * */
    private void getUserInfo(){
        HttpUtils.getInstance().post(mContext,HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.d(TAG,"成功："+ str);

                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserInfoRequest request = new Gson().fromJson(str,UserInfoRequest.class);
                        Log.d(TAG,"进来了  环信id："+request.data.huanXinId +" | token: "+request.data.telephone);
                        if (!TextUtils.isEmpty(request.data.huanXinId) && !TextUtils.isEmpty(request.data.telephone) ){
//                            mFragmentViewModel.login(request.data.nickName, "12345678", isTokenFlag);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                super.onFailure(call, e,str);
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"失败：");
                    }
                });
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode(){
        SendCodeBean bean = new SendCodeBean();
        bean.telephone = mUserName;
        bean.type = SendCodeBean.SendCodeType.Login;
        Log.i(TAG,"参数："+ new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext,HttpURL.SEND_CODE, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call,String str) {
                Log.d(TAG,"成功："+ str);
                try {
                    BaseRequest<String> sendCodeRequest = new Gson().fromJson(str,BaseRequest.class);
                    RegisterBean registerBean = new RegisterBean();
                    registerBean.telephone = mUserName;
                    registerBean.code = sendCodeRequest.data;
                    VerificationActivity.startAction(mContext,registerBean);
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
     * 切换登录方式
     */
    private void switchLogin() {
        mEtLoginPwd.setText("");
        if(isTokenFlag) {
            mEtLoginPwd.setHint(R.string.em_login_token_hint);
            mTvLoginToken.setText(R.string.em_login_tv_pwd);
            mEtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        }else {
            mEtLoginPwd.setHint(R.string.em_login_password_hint);
            mTvLoginToken.setText(R.string.em_login_tv_token);
            mEtLoginPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private void loginToServer() {
        if(TextUtils.isEmpty(mUserName) || TextUtils.isEmpty(mPwd)) {
            ToastUtils.showToast(R.string.em_login_btn_info_incomplete);
            return;
        }
        isClick = true;
//        mFragmentViewModel.login(mUserName, mPwd, isTokenFlag);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mUserName = mEtLoginName.getText().toString().trim();
        mPwd = mEtLoginPwd.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwd, isTokenFlag ? null : eyeClose);
        setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd));
        if (!TextUtils.isEmpty(mUserName)){
            mBtnLogin.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            mBtnLogin.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            mBtnLogin.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            mBtnLogin.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_select :
                setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && isChecked);
                break;
        }
    }

    private void setButtonEnable(boolean enable) {
//        mBtnLogin.setEnabled(enable);
        if(mEtLoginPwd.hasFocus()) {
            mEtLoginPwd.setImeOptions(enable ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_PREVIOUS);
        }else if(mEtLoginName.hasFocus()) {
            mEtLoginPwd.setImeOptions(enable ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
        }

        //同时需要修改右侧drawalbeRight对应的资源
//        Drawable rightDrawable;
//        if(enable) {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_enable);
//        }else {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_unable);
//        }
//        mBtnLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    private SpannableString getSpannable() {
        SpannableString spanStr = new SpannableString(getString(R.string.em_login_agreement));
        //设置下划线
        //spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showToast("跳转到服务条款");
            }
        }, 2, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //spanStr.setSpan(new UnderlineSpan(), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                showToast("跳转到隐私协议");
            }
        }, 11, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE) {
            if(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd)) {
                hideKeyboard();
                loginToServer();
                return true;
            }
        }
        return false;
    }

    private abstract class MyClickableSpan extends ClickableSpan {

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.bgColor = Color.TRANSPARENT;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isClick = false;
    }

}
