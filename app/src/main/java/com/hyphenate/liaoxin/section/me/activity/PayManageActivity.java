package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.common.widget.NoticeTitleDialog;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 支付管理
 */
public class PayManageActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "PayManageActivity";


    public static void actionStart(Context context) {
        Intent starter = new Intent(context, PayManageActivity.class);
        context.startActivity(starter);
    }


    private EaseTitleBar titleBar;
    private ArrowItemView itemRenzheng;
    private ArrowItemView itemChangePassword;
    private ArrowItemView itemWangjiPassword;
    private ArrowItemView itemTuikuan;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_manage;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        itemRenzheng = findViewById(R.id.item_renzheng);
        itemChangePassword = findViewById(R.id.item_change_password);
        itemWangjiPassword = findViewById(R.id.item_wangji_password);
        itemTuikuan = findViewById(R.id.item_tuikuan);
    }


    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        itemRenzheng.setOnClickListener(this);
        itemChangePassword.setOnClickListener(this);
        itemWangjiPassword.setOnClickListener(this);
        itemTuikuan.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        if (PrefUtils.getBoolean(mContext, UserConstant.isRealAuth,false)){
            itemRenzheng.getTvContent().setText("已认证");
            itemRenzheng.getTvContent().setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
        if (PrefUtils.getBoolean(mContext,UserConstant.isCoinPassword,false)){
            itemChangePassword.getTvTitle().setText("修改支付密码");
        }else {
            itemChangePassword.getTvTitle().setText("设置支付密码");
        }
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_renzheng://认证
                if (!PrefUtils.getBoolean(mContext, UserConstant.isRealAuth,false)){
                    CertificationActivity.actionStart(mContext);
                }
                break;
            case R.id.item_change_password://修改密码
                if (!PrefUtils.getBoolean(mContext, UserConstant.isRealAuth,false)){
                    showDialog();
                    return;
                }
                if (PrefUtils.getBoolean(mContext,UserConstant.isCoinPassword,false)){
                    ChangePasswordActivity.actionStart(mContext);
                }else {
                    SetPayPasswordActivity.actionStart(mContext);
                }
                break;
            case R.id.item_wangji_password://忘记密码
                ForgetPasswordActivity.actionStart(mContext);
                break;
            case R.id.item_tuikuan://退款

                break;
        }
    }


    private void showDialog(){
        NoticeTitleDialog dialog = new NoticeTitleDialog(mContext,"设置支付密码前先需实名认证，才能 设置进行支付和提现操作");
        dialog.setTitle("提示");
        dialog.setAction("前往认证");
        dialog.setCancel("取消");
        dialog.setType(1);
        dialog.setOnItemClickListener(new NoticeTitleDialog.onItemClickListener() {
            @Override
            public void onActionClick() {
                CertificationActivity.actionStart(mContext);
                dialog.dismiss();
            }

            @Override
            public void onCancelClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
