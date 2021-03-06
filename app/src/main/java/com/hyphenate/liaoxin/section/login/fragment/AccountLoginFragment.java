package com.hyphenate.liaoxin.section.login.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMError;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.MainActivity;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.section.base.WebViewActivity;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.section.base.BaseInitFragment;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginFragmentViewModel;
import com.hyphenate.liaoxin.section.login.viewmodels.LoginViewModel;

import java.io.IOException;

import okhttp3.Call;

public class AccountLoginFragment extends BaseInitFragment implements TextWatcher, View.OnClickListener, EaseTitleBar.OnBackPressListener, CompoundButton.OnCheckedChangeListener {

    private String TAG = "AccountLoginFragment";

    private EaseTitleBar mToolbarRegister;
    private EditText mEtLoginName;
    private EditText mEtLoginPwd;
    private EditText mEtLoginPwdConfirm;
    private Button mBtnLogin;
    private CheckBox cbSelect;
    private TextView tvAgreement;
    private String mUserName;
    private String mPwd;
    private String mPwdConfirm;
    private LoginViewModel mViewModel;
    private Drawable clear;
    private Drawable eyeOpen;
    private Drawable eyeClose;
    private LoginFragmentViewModel mFragmentViewModel;

//    =======================
    private EditText etLoginPhone;
    private String mPhone;
    private EditText etLoginPassword;
    private String mPassWord;
    private TextView btnAccount;
    private TextView tvWangjiPassword;
    private TextView tvPhoneLogin;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolbarRegister = findViewById(R.id.toolbar_register);
        mEtLoginName = findViewById(R.id.et_login_name);
        mEtLoginPwd = findViewById(R.id.et_login_pwd);
        mEtLoginPwdConfirm = findViewById(R.id.et_login_pwd_confirm);
        mBtnLogin = findViewById(R.id.btn_login);
        cbSelect = findViewById(R.id.cb_select);
        tvAgreement = findViewById(R.id.tv_agreement);
        etLoginPhone = findViewById(R.id.et_login_phone);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnAccount = findViewById(R.id.btn_account);
        tvWangjiPassword = findViewById(R.id.tv_wangji_password);
        tvPhoneLogin = findViewById(R.id.tv_phone_login);
    }

    @Override
    protected void initListener() {
        super.initListener();
        etLoginPassword.addTextChangedListener(this);
        etLoginPhone.addTextChangedListener(this);
        mEtLoginName.addTextChangedListener(this);
        mEtLoginPwd.addTextChangedListener(this);
        mEtLoginPwdConfirm.addTextChangedListener(this);
        mBtnLogin.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        tvWangjiPassword.setOnClickListener(this);
        tvPhoneLogin.setOnClickListener(this);
        mToolbarRegister.setOnBackPressListener(this);
        cbSelect.setOnCheckedChangeListener(this);
        EaseEditTextUtils.clearEditTextListener(mEtLoginName);
        EaseEditTextUtils.clearEditTextListener(etLoginPhone);
    }

    @Override
    protected void initData() {
        super.initData();
        tvAgreement.setText(getSpannable());
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        mViewModel = new ViewModelProvider(mContext).get(LoginViewModel.class);
        mViewModel.getRegisterObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<String>(true) {
                @Override
                public void onSuccess(String data) {
                    ToastUtils.showToast(getResources().getString(R.string.em_register_success));
                    onBackPress();
                }

                @Override
                public void onError(int code, String message) {
                    if(code == EMError.USER_ALREADY_EXIST) {
                        ToastUtils.showToast(R.string.demo_error_user_already_exist);
                    }else {
                        ToastUtils.showToast(message);
                    }
                }

                @Override
                public void onLoading(String data) {
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
        //??????????????????????????????????????????
        eyeClose = getResources().getDrawable(R.drawable.d_pwd_hide);
        eyeOpen = getResources().getDrawable(R.drawable.d_pwd_show);
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwd, eyeClose, eyeOpen, null, null, null);
        EaseEditTextUtils.changePwdDrawableRight(mEtLoginPwdConfirm, eyeClose, eyeOpen, null, null, null);
        EaseEditTextUtils.changePwdDrawableRight(etLoginPassword, eyeClose, eyeOpen, null, null, null);

        mFragmentViewModel = new ViewModelProvider(this).get(LoginFragmentViewModel.class);
        mFragmentViewModel.getLoginObservable().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<EaseUser>(true) {
                @Override
                public void onSuccess(EaseUser data) {
                    Log.e("login", "login success");
                    DemoHelper.getInstance().setAutoLogin(true);
                    //???????????????
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
                    dismissLoading();
                }
            });

        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkEditContent();
    }

    private void checkEditContent() {
        mPhone = etLoginPhone.getText().toString().trim();
        mPassWord = etLoginPassword.getText().toString().trim();
        //===========================================================
        mUserName = mEtLoginName.getText().toString().trim();
        mPwd = mEtLoginPwd.getText().toString().trim();
        mPwdConfirm = mEtLoginPwdConfirm.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(mEtLoginName, clear);
        EaseEditTextUtils.showRightDrawable(etLoginPhone, clear);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwd, eyeClose);
        EaseEditTextUtils.showRightDrawable(etLoginPassword, eyeClose);
        EaseEditTextUtils.showRightDrawable(mEtLoginPwdConfirm, eyeClose);
        setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm) && cbSelect.isChecked());
        btnContoul();
    }

    private void btnContoul(){
        if (isLogin(false)){
            btnAccount.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            btnAccount.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            btnAccount.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            btnAccount.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login :
                registerToHx();
                break;
            case R.id.btn_account:
                if (isLogin(true)){
                    getLogin();
                }
                break;
            case R.id.tv_wangji_password:
                mViewModel.setPageSelect(2);
                break;
            case R.id.tv_phone_login:
                onBackPress();
                break;
        }
    }

    /**
     * ??????
     */
    private void getLogin(){
        showLoading();
        RegisterBean request = new RegisterBean();
        request.telephone = mPhone;
        request.password = mPassWord;
        Log.i(TAG,"?????????"+ new Gson().toJson(request));
        HttpUtils.getInstance().post(mContext, HttpURL.LOGIN, new Gson().toJson(request), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"?????????"+ str);
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
                dismissLoading();
                super.onFailure(call, e,str);
                Log.d(TAG,"?????????");
            }
        });
    }

    /**
     * ????????????????????????
     * */
    private void getUserInfo(){
        HttpUtils.getInstance().post(mContext,HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.d(TAG,"?????????"+ str);
                UserInfoRequest request = new Gson().fromJson(str,UserInfoRequest.class);
                Log.d(TAG,"?????????  ??????id???"+request.data.huanXinId +" | telephone:"+request.data.telephone);
                if (!TextUtils.isEmpty(request.data.huanXinId) && !TextUtils.isEmpty(request.data.telephone) ){
                    Login(request.data.huanXinId,request.data.telephone);
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                super.onFailure(call, e,str);
                Log.d(TAG,"?????????");
            }
        });
    }

    /**
     * ??????
     * @param account
     * @param password
     */
    private void Login(String account,String password){
        mFragmentViewModel.login(account, password, false);
    }


    private boolean isLogin(boolean isShow){
        if (TextUtils.isEmpty(mPhone)){
            if (isShow)
                ToastUtils.showFailToast("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(mPassWord)){
            if (isShow)
                ToastUtils.showFailToast("???????????????");
            return false;
        }
        return true;
    }

    private void registerToHx() {
        if(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm)) {
            if(!TextUtils.equals(mPwd, mPwdConfirm)) {
                showToast(R.string.em_password_confirm_error);
                return;
            }
            mViewModel.register(mUserName, mPwd);
        }
    }

    private void setButtonEnable(boolean enable) {
        mBtnLogin.setEnabled(enable);
        //????????????????????????drawalbeRight???????????????
//        Drawable rightDrawable;
//        if(enable) {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_enable);
//        }else {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_unable);
//        }
//        mBtnLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    @Override
    public void onBackPress(View view) {
        onBackPress();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_select :
                setButtonEnable(!TextUtils.isEmpty(mUserName) && !TextUtils.isEmpty(mPwd) && !TextUtils.isEmpty(mPwdConfirm) && isChecked);
                break;
        }
    }

    private SpannableString getSpannable() {
        SpannableString spanStr = new SpannableString(getString(R.string.em_login_agreement));
        //???????????????
        //spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebViewActivity.actionStart(mContext, getString(R.string.em_register_service_agreement_url));
            }
        }, 2, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //spanStr.setSpan(new UnderlineSpan(), 10, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanStr.setSpan(new MyClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                WebViewActivity.actionStart(mContext, getString(R.string.em_register_privacy_agreement_url));
            }
        }, 11, spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    private abstract class MyClickableSpan extends ClickableSpan {

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.bgColor = Color.TRANSPARENT;
            ds.setColor(ContextCompat.getColor(mContext, R.color.white));
        }
    }
}
