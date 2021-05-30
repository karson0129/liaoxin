package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BankRequest;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.me.adapter.BankAdapter;

import java.io.IOException;

import okhttp3.Call;

public class BankActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "BankActivity";


    private EaseTitleBar titleBar;
    private RecyclerView recycler;
    private BankAdapter adapter;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, BankActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new BankAdapter(mContext);
        adapter.setItemOnClick(new BankAdapter.itemOnClick() {
            @Override
            public void onItem() {

            }

            @Override
            public void onAdd() {
                AddBankPasswordActivity.actionStart(mContext);
            }
        });
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        getData();
    }

    /**
     * 获取银行卡
     * */
    private void getData(){
        showLoading();
        HttpUtils.getInstance().post(mContext, HttpURL.GET_CLIENT_BANKS, "", new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG,"银行:" + str);
                try {
                    BankRequest request = new Gson().fromJson(str,BankRequest.class);
                    if (request != null){
                        adapter.setData(request.data);
                    }
                }catch (Exception e){
                    ToastUtils.showToast("解析错误");
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }
}
