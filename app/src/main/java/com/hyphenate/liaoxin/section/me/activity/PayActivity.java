package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 支付
 * */
public class PayActivity extends BaseInitActivity implements  EaseTitleBar.OnBackPressListener,View.OnClickListener {

    private String TAG = "PayActivity";

    private EaseTitleBar titleBar;
    private TextView tvPay;//显示余额
    private TextView tvMingXi;//明细
    private TextView tvChongZhi;//充值
    private TextView tvTiXian;//体现
    private LinearLayout linShouKuanMa;//收款码

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, PayActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        tvPay = findViewById(R.id.tv_pay);
        tvMingXi = findViewById(R.id.tv_mingxi);
        tvChongZhi = findViewById(R.id.tv_chongzhi);
        tvTiXian = findViewById(R.id.tv_tixian);
        linShouKuanMa = findViewById(R.id.tv_shoukuanma);
        titleBar.setRightImageResource(R.drawable.home_details);

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        tvPay.setOnClickListener(this);
        tvMingXi.setOnClickListener(this);
        tvChongZhi.setOnClickListener(this);
        tvTiXian.setOnClickListener(this);
        linShouKuanMa.setOnClickListener(this);
        titleBar.getRightImage().setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_pay://余额

                break;
            case R.id.tv_mingxi://明细

                break;
            case R.id.tv_chongzhi://充值

                break;
            case R.id.tv_tixian://提现
                WithdrawActivity.actionStart(mContext);
                break;
            case R.id.tv_shoukuanma://收款码

                break;
            case R.id.right_image:
                PayManageActivity.actionStart(mContext);
                break;
        }
    }
}
