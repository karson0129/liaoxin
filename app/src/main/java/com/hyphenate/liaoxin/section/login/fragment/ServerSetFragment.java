package com.hyphenate.liaoxin.section.login.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.model.DemoServerSetBean;
import com.hyphenate.liaoxin.common.net.bean.FindPasswordBean;
import com.hyphenate.liaoxin.common.net.bean.SendCodeBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitFragment;
import com.hyphenate.liaoxin.section.dialog.DemoDialogFragment;
import com.hyphenate.liaoxin.section.dialog.SimpleDialogFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;

public class ServerSetFragment extends BaseInitFragment implements EaseTitleBar.OnBackPressListener, CompoundButton.OnCheckedChangeListener, TextWatcher, View.OnClickListener {

    private String TAG = "ServerSetFragment";

    private EaseTitleBar mToolbarServer;
    private Switch mSwitchServer;
    private TextView mEtServerHint;
    private EditText mEtAppkey;
    private Switch mSwitchSpecifyServer;
    private EditText mEtServerAddress;
    private EditText mEtServerPort;
    private EditText mEtServerRest;
    private Switch mSwitchHttpsSet;
    private Button mBtnReset;
    private Button mBtnServer;
    private Group mGroupServerSet;

    private String mServerAddress;
    private String mServerPort;
    private String mRestServerAddress;
    private String mAppkey;
    private boolean mCustomServerEnable;
    private boolean mCustomSetEnable;
//    ==========================================================

    private Drawable clear;
    private EditText etLoginName;
    private String mPhone;
    private TextView tvVerificationCode;
    private EditText etVerificationCode;
    private String mVerificationCode;
    private EditText etNewPassword;
    private String mPassword;
    private TextView btnAction;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_server_set;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        btnAction = findViewById(R.id.btn_action);
        etNewPassword = findViewById(R.id.et_new_password);
        etVerificationCode = findViewById(R.id.et_verification_code);
        tvVerificationCode = findViewById(R.id.verification_code);
        etLoginName = findViewById(R.id.et_login_name);
        mToolbarServer = findViewById(R.id.toolbar_server);
        mSwitchServer = findViewById(R.id.switch_server);
        mEtServerHint = findViewById(R.id.et_server_hint);
        mEtAppkey = findViewById(R.id.et_appkey);
        mSwitchSpecifyServer = findViewById(R.id.switch_specify_server);
        mEtServerAddress = findViewById(R.id.et_server_address);
        mEtServerPort = findViewById(R.id.et_server_port);
        mEtServerRest = findViewById(R.id.et_server_rest);
        mSwitchHttpsSet = findViewById(R.id.switch_https_set);
        mBtnReset = findViewById(R.id.btn_reset);
        mBtnServer = findViewById(R.id.btn_server);
        mGroupServerSet = findViewById(R.id.group_server_set);

        checkServerSet();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolbarServer.setOnBackPressListener(this);
        mSwitchServer.setOnCheckedChangeListener(this);
        mSwitchSpecifyServer.setOnCheckedChangeListener(this);
        mEtAppkey.addTextChangedListener(this);
        mEtServerAddress.addTextChangedListener(this);
        mEtServerPort.addTextChangedListener(this);
        mEtServerRest.addTextChangedListener(this);
        mBtnReset.setOnClickListener(this);
        mBtnServer.setOnClickListener(this);
        //==============================================?
        etLoginName.addTextChangedListener(this);
        EaseEditTextUtils.clearEditTextListener(etLoginName);
        EaseEditTextUtils.clearEditTextListener(etVerificationCode);
        EaseEditTextUtils.clearEditTextListener(etNewPassword);
        tvVerificationCode.setOnClickListener(this);
        btnAction.setOnClickListener(this);
        etVerificationCode.addTextChangedListener(this);
        etNewPassword.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etLoginName, clear);
        EaseEditTextUtils.showRightDrawable(etVerificationCode, clear);
        EaseEditTextUtils.showRightDrawable(etNewPassword, clear);
    }

    /**
     * ?????????????????????
     */
    private void checkServerSet() {
        boolean isInited = DemoHelper.getInstance().isSDKInit();

        //???????????????????????????????????????????????????????????????
        mCustomSetEnable = DemoHelper.getInstance().getModel().isCustomSetEnable();
        mCustomServerEnable = DemoHelper.getInstance().getModel().isCustomServerEnable();
        mSwitchServer.setChecked(mCustomSetEnable);
        mSwitchSpecifyServer.setChecked(mCustomServerEnable);
        mSwitchHttpsSet.setChecked(DemoHelper.getInstance().getModel().getUsingHttpsOnly());
        String appkey = DemoHelper.getInstance().getModel().getCutomAppkey();
        mEtAppkey.setText((!DemoHelper.getInstance().getModel().isCustomAppkeyEnabled() || TextUtils.isEmpty(appkey)) ? "":appkey);
        String imServer = DemoHelper.getInstance().getModel().getIMServer();
        mEtServerAddress.setText(TextUtils.isEmpty(imServer) ? "" : imServer);
        int imServerPort = DemoHelper.getInstance().getModel().getIMServerPort();
        mEtServerPort.setText(imServerPort == 0 ? "" : imServerPort+"");
        String restServer = DemoHelper.getInstance().getModel().getRestServer();
        mEtServerRest.setText(TextUtils.isEmpty(restServer) ? "" : restServer);
        mGroupServerSet.setVisibility(mSwitchServer.isChecked() ? View.VISIBLE : View.GONE);
        setResetButtonVisible(mSwitchServer.isChecked(), isInited);
        //??????????????????
        mEtServerHint.setVisibility(isInited ? View.VISIBLE : View.GONE);
        mEtAppkey.setEnabled(!isInited);
        mSwitchSpecifyServer.setEnabled(!isInited);
        mEtServerAddress.setEnabled(!isInited && mCustomServerEnable);
        mEtServerPort.setEnabled(!isInited && mCustomServerEnable);
        mEtServerRest.setEnabled(!isInited && mCustomServerEnable);
        mSwitchHttpsSet.setEnabled(!isInited && mCustomServerEnable);
        checkButtonEnable();
    }

    /**
     * ???????????????????????????button????????????
     * @param isChecked
     * @param isInited
     */
    private void setResetButtonVisible(boolean isChecked, boolean isInited) {
        mBtnReset.setVisibility(isChecked ? (isInited ? View.GONE : View.VISIBLE) : View.GONE);
    }

    @Override
    public void onBackPress(View view) {
        onBackPress();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_server :
                mCustomSetEnable = isChecked;
                DemoHelper.getInstance().getModel().enableCustomSet(isChecked);
                DemoHelper.getInstance().getModel().enableCustomAppkey(!TextUtils.isEmpty(mEtAppkey.getText().toString().trim()) && isChecked);
                mGroupServerSet.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                setResetButtonVisible(isChecked, DemoHelper.getInstance().isSDKInit());
                break;
            case R.id.switch_specify_server :
                DemoHelper.getInstance().getModel().enableCustomServer(isChecked);
                mCustomServerEnable = isChecked;
                mEtServerAddress.setEnabled(isChecked);
                mEtServerPort.setEnabled(isChecked);
                mEtServerRest.setEnabled(isChecked);
                mSwitchHttpsSet.setEnabled(isChecked);
                checkButtonEnable();
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mPhone = etLoginName.getText().toString().trim();
        mVerificationCode = etVerificationCode.getText().toString().trim();
        mPassword = etNewPassword.getText().toString().trim();
        mAppkey = mEtAppkey.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(etLoginName, clear);
        EaseEditTextUtils.showRightDrawable(etVerificationCode, clear);
        EaseEditTextUtils.showRightDrawable(etNewPassword, clear);
        DemoHelper.getInstance().getModel().enableCustomAppkey(!TextUtils.isEmpty(mAppkey) && mSwitchServer.isChecked());
        checkButtonEnable();
        btnContoul();
    }

    private void btnContoul(){
        if (isLogin(false)){
            btnAction.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            btnAction.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            btnAction.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            btnAction.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    private void checkButtonEnable() {
        mAppkey = mEtAppkey.getText().toString().trim();
        if(mCustomServerEnable) {
            mServerAddress = mEtServerAddress.getText().toString().trim();
            mServerPort = mEtServerPort.getText().toString().trim();
            mRestServerAddress = mEtServerRest.getText().toString().trim();
            setButtonEnable(!TextUtils.isEmpty(mServerAddress)
                    && !TextUtils.isEmpty(mAppkey)
                    && !TextUtils.isEmpty(mServerPort)
                    && !TextUtils.isEmpty(mRestServerAddress));
        }else {
            setButtonEnable(!TextUtils.isEmpty(mAppkey));
        }
        boolean isInited = DemoHelper.getInstance().isSDKInit();
        //??????sdk??????????????????????????????????????????????????????????????????
        if(isInited) {
            mBtnServer.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_server) {
            saveServerSet();
        }else if(v.getId() == R.id.btn_reset) {
            new SimpleDialogFragment.Builder(mContext)
                    .setTitle(R.string.em_server_set_dialog_reset)
                    .setOnConfirmClickListener(new DemoDialogFragment.OnConfirmClickListener() {
                        @Override
                        public void onConfirmClick(View view) {
                            DemoServerSetBean set = DemoHelper.getInstance().getModel().getDefServerSet();
                            mEtAppkey.setText(set.getAppkey());
                            mEtServerAddress.setText(set.getImServer());
                            mEtServerPort.setText(set.getImPort()+"");
                            mEtServerRest.setText(set.getRestServer());
                        }
                    })
                    .showCancelButton(true)
                    .show();
        }
        switch (v.getId()){
            case R.id.verification_code://???????????????
                if (!TextUtils.isEmpty(mPhone)){
                    getVerificationCode();
                }
                break;
            case R.id.btn_action:
                if (isLogin(true)){
                    getFindPassword();
                }
                break;
        }
    }

    private boolean isLogin(boolean isShow){
        if (TextUtils.isEmpty(mPhone)){
            if (isShow)
                ToastUtils.showFailToast("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(mVerificationCode)){
            if (isShow)
                ToastUtils.showFailToast("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(mPassword)){
            if (isShow)
                ToastUtils.showFailToast("???????????????");
            return false;
        }
        return true;
    }

    /**
     * ????????????
     */
    private void getFindPassword(){
        showLoading();
        FindPasswordBean bean = new FindPasswordBean();
        bean.telephone = mPhone;
        bean.code = mVerificationCode;
        bean.newPassword = mPassword;
        Log.i(TAG,"?????????"+ new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.FIND_PASSWORD_BY_PHONE, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                ToastUtils.showSuccessToast("????????????");
                onBackPress();
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call,e,str);
                Log.i(TAG,"??????");
            }
        });
    }

    /**
     * ???????????????
     */
    private void getVerificationCode(){
        showLoading();
        SendCodeBean bean = new SendCodeBean();
        bean.telephone = mPhone;
        bean.type = SendCodeBean.SendCodeType.RetrievePassword;
        Log.i(TAG,"?????????"+ new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.SEND_CODE, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"?????????"+ str);
                ToastUtils.showSuccessToast("????????????");
                if (timer != null){
                    timer.start();
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e,str);
                Log.d(TAG,"?????????"+ e.toString());
                ToastUtils.showFailToast("????????????");
            }
        });
    }

    /**
     *  ?????????
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvVerificationCode.setEnabled(false);
            tvVerificationCode.setText((millisUntilFinished / 1000) + "???????????????");
        }

        @Override
        public void onFinish() {
            tvVerificationCode.setEnabled(true);
            tvVerificationCode.setText("???????????????");
        }
    };

    private void saveServerSet() {
        if(mCustomServerEnable) {
            //?????????????????????????????????????????????????????????????????????
            if(TextUtils.isEmpty(mAppkey)) {
                showToast(R.string.em_server_set_appkey_empty_hint);
                return;
            }
            if(TextUtils.isEmpty(mServerAddress)) {
                showToast(R.string.em_server_set_im_server_empty_hint);
                return;
            }
            if(TextUtils.isEmpty(mServerPort)) {
                showToast(R.string.em_server_set_im_port_empty_hint);
                return;
            }
            if(TextUtils.isEmpty(mRestServerAddress)) {
                showToast(R.string.em_server_set_rest_server_empty_hint);
                return;
            }
        }
        // ????????????
        if(!TextUtils.isEmpty(mAppkey)) {
            DemoHelper.getInstance().getModel().enableCustomAppkey(mSwitchServer.isChecked());
            DemoHelper.getInstance().getModel().setCustomAppkey(mAppkey);
        }
        if(!TextUtils.isEmpty(mServerAddress)) {
            DemoHelper.getInstance().getModel().setIMServer(mServerAddress);
        }
        if(!TextUtils.isEmpty(mServerPort)) {
            DemoHelper.getInstance().getModel().setIMServerPort(Integer.valueOf(mServerPort));
        }
        if(!TextUtils.isEmpty(mRestServerAddress)) {
            DemoHelper.getInstance().getModel().setRestServer(mRestServerAddress);
        }
        DemoHelper.getInstance().getModel().enableCustomServer(mCustomServerEnable);
        DemoHelper.getInstance().getModel().setUsingHttpsOnly(mSwitchHttpsSet.isChecked());

        //??????????????????????????????????????????
        onBackPress();
    }

    private void setButtonEnable(boolean enable) {
        Log.e("TAG", "setButtonEnable = "+enable);
        mBtnServer.setEnabled(enable);
        //????????????????????????drawalbeRight???????????????
//        Drawable rightDrawable;
//        if(enable) {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_enable);
//        }else {
//            rightDrawable = ContextCompat.getDrawable(mContext, R.drawable.demo_login_btn_right_unable);
//        }
//        mBtnServer.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }
}
