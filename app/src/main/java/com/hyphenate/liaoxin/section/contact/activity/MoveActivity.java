package com.hyphenate.liaoxin.section.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.SetFriendRemarkBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.StrangerDetailRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.IOException;

import okhttp3.Call;

/**
 * 更多
 * */
public class MoveActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "MoveActivity";

    private EaseTitleBar titleBar;
    private ArrowItemView itemQun;
    private ArrowItemView itemLaiyuan;

    private String clientId;

    public static void actionStart(Context context, String clientId) {
        Intent intent = new Intent(context, MoveActivity.class);
        intent.putExtra("clientId", clientId);
        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        clientId = getIntent().getStringExtra("clientId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_move;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar_contact_detail);
        itemQun = findViewById(R.id.item_qun);
        itemLaiyuan = findViewById(R.id.item_laiyuan);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        itemQun.setOnClickListener(this);
        itemLaiyuan.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    private void getData(){
        showLoading();
        SetFriendRemarkBean bean = new SetFriendRemarkBean();
        bean.clientId = clientId;
        Log.i(TAG,"参数："+new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.STRANGER_DETAIL, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.i(TAG,"详情："+str);
                try {
                    StrangerDetailRequest request = new Gson().fromJson(str,StrangerDetailRequest.class);
                    if (request.data != null){

                        if (!TextUtils.isEmpty(request.data.source)){
                            itemLaiyuan.getTvContent().setText(request.data.source);
                        }

                        itemQun.getTvContent().setText(request.data.mutipleGroupCnt+"个");
                    }
                }catch (Exception e){
                    ToastUtils.showToast("解析错误");
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                super.onFailure(call, e, str);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_qun://群

                break;
            case R.id.item_laiyuan://来源

                break;
        }
    }
}
