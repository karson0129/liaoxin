package com.hyphenate.liaoxin.section.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.liaoxin.DemoHelper;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.easeui.manager.EaseThreadManager;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.section.base.BaseInitFragment;
import com.hyphenate.liaoxin.section.dialog.DemoDialogFragment;
import com.hyphenate.liaoxin.section.dialog.SimpleDialogFragment;
import com.hyphenate.liaoxin.section.login.activity.LoginActivity;
import com.hyphenate.liaoxin.section.me.activity.AboutHxActivity;
import com.hyphenate.liaoxin.section.me.activity.DeveloperSetActivity;
import com.hyphenate.liaoxin.section.me.activity.FeedbackActivity;
import com.hyphenate.liaoxin.section.me.activity.PayActivity;
import com.hyphenate.liaoxin.section.me.activity.SetIndexActivity;
import com.hyphenate.liaoxin.section.me.activity.UserDetailActivity;
import com.hyphenate.easeui.model.EaseEvent;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AboutMeFragment extends BaseInitFragment implements View.OnClickListener {
    static private String TAG =  "AboutMeFragment";
    private ConstraintLayout clUser;
    private ImageView avatar;
    private ArrowItemView itemCommonSet;
    private ArrowItemView itemFeedback;
    private ArrowItemView itemAboutHx;
    private ArrowItemView itemDeveloperSet;
    private ArrowItemView itemCommonPay;
    private ArrowItemView itemCommonTeam;
    private Button mBtnLogout;
    private TextView nickName_view;
    private TextView userId_view;
    private ImageView myCode;
    private EMUserInfo userInfo;
    @Override
    protected int getLayoutId() {
        return R.layout.demo_fragment_about_me;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        clUser = findViewById(R.id.cl_user);
        nickName_view = findViewById(R.id.tv_nickName);
        userId_view = findViewById(R.id.tv_userId);
        avatar = findViewById(R.id.avatar);
        itemCommonSet = findViewById(R.id.item_common_set);
        itemFeedback = findViewById(R.id.item_feedback);
        itemAboutHx = findViewById(R.id.item_about_hx);
        itemDeveloperSet = findViewById(R.id.item_developer_set);
        mBtnLogout = findViewById(R.id.btn_logout);
        itemCommonPay = findViewById(R.id.item_common_pay);
        itemCommonTeam = findViewById(R.id.item_common_team);
        myCode = findViewById(R.id.my_code);
        userId_view.setText("聊信号：" + DemoHelper.getInstance().getCurrentUser());
    }

    @Override
    protected void initListener() {
        super.initListener();
        mBtnLogout.setOnClickListener(this);
        clUser.setOnClickListener(this);
        itemCommonSet.setOnClickListener(this);
        itemFeedback.setOnClickListener(this);
        itemAboutHx.setOnClickListener(this);
        itemDeveloperSet.setOnClickListener(this);
        itemCommonPay.setOnClickListener(this);
        itemCommonTeam.setOnClickListener(this);
        myCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout :
                logout();
                break;
            case R.id.cl_user:
                if(userInfo != null){
                    UserDetailActivity.actionStart(mContext,userInfo.getNickName(),userInfo.getAvatarUrl());
                }else{
                    UserDetailActivity.actionStart(mContext,null,null);
                }
                break;
            case R.id.item_common_set://设置
                SetIndexActivity.actionStart(mContext);
                break;
            case R.id.item_feedback:
                FeedbackActivity.actionStart(mContext);
                break;
            case R.id.item_about_hx:
                AboutHxActivity.actionStart(mContext);
                break;
            case R.id.item_developer_set:
                DeveloperSetActivity.actionStart(mContext);
                break;
            case R.id.item_common_pay://支付
                PayActivity.actionStart(mContext);
                break;
            case R.id.item_common_team://聊信团队

                break;
            case R.id.my_code:
                ToastUtils.showSuccessToast("二维码");
                break;
        }
    }

    private void logout() {
        new SimpleDialogFragment.Builder(mContext)
                .setTitle(R.string.em_login_out_hint)
                .showCancelButton(true)
                .setOnConfirmClickListener(R.string.em_dialog_btn_confirm, new DemoDialogFragment.OnConfirmClickListener() {
                    @Override
                    public void onConfirmClick(View view) {
                        DemoHelper.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                LoginActivity.startAction(mContext);
                                mContext.finish();
                            }

                            @Override
                            public void onError(int code, String error) {
                                EaseThreadManager.getInstance().runOnMainThread(()-> showToast(error));
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                    }
                })
                .show();
    }


    @Override
    public void initData(){
        super.initData();
        addLiveDataObserver();
    }


    protected void addLiveDataObserver() {
        LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                ImageLoad.intoRoundedCorners(mContext, HttpURL.PICTURE_URL + event.message, (int) EaseCommonUtils.dip2px(mContext, 4),avatar);
                if(userInfo != null){
                    userInfo.setAvatarUrl(event.message);
                }
            }
        });
        LiveDataBus.get().with(DemoConstant.NICK_NAME_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                nickName_view.setText("" + event.message);
                userId_view.setText("聊信号：" + EMClient.getInstance().getCurrentUser());
                if(userInfo != null){
                    userInfo.setNickName(event.message);
                }
            }
        });
    }
}
