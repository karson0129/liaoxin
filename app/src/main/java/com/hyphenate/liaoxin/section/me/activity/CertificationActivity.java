package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 认证
 * */
public class CertificationActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, TextWatcher, View.OnClickListener {

    private String TAG = "CertificationActivity";

    private EaseTitleBar titleBar;
    private EditText etName;
    private EditText etCode;
    private ImageView ivZhengmian;
    private ImageView ivFanmian;
    private TextView action;

    private Drawable clear;

    private String mName;
    private String mCode;
    private String mZheng;
    private String mFan;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, CertificationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        etName = findViewById(R.id.et_name);
        etCode = findViewById(R.id.et_code);
        ivZhengmian = findViewById(R.id.iv_zhengmian);
        ivFanmian = findViewById(R.id.iv_fanmian);
        action = findViewById(R.id.action);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        etName.addTextChangedListener(this);
        etCode.addTextChangedListener(this);
        ivFanmian.setOnClickListener(this);
        ivZhengmian.setOnClickListener(this);
        action.setOnClickListener(this);
        EaseEditTextUtils.clearEditTextListener(etName);
        EaseEditTextUtils.clearEditTextListener(etCode);
    }

    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mName = etName.getText().toString().trim();
        mCode = etCode.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);

        if (contsol(false)){
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
        }else {
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
        }
    }

    private boolean contsol(boolean isShow){
        if (!TextUtils.isEmpty(mName)){
            if (isShow)
            ToastUtils.showFailToast("请输入姓名");
            return false;
        }
        if (!TextUtils.isEmpty(mCode)){
            if (isShow)
            ToastUtils.showFailToast("请输入身份证号");
            return false;
        }
        if (!TextUtils.isEmpty(mZheng)){
            if (isShow)
            ToastUtils.showFailToast("请上传身份证正面");
            return false;
        }
        if (!TextUtils.isEmpty(mFan)){
            if (isShow)
            ToastUtils.showFailToast("请上传身份证反面");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_zhengmian://正面

                break;

            case R.id.iv_fanmian://反面

                break;
            case R.id.action://提交
                if (contsol(true)){
                    CertificationSuccessActivity.actionStart(mContext);
                }
                break;
        }
    }


}
