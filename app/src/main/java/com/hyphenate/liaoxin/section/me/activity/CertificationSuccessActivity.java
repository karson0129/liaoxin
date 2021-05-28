package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 认证成功
 * */
public class CertificationSuccessActivity extends BaseInitActivity {

    private String TAG = "CertificationSuccessActivity";

    private TextView action;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, CertificationSuccessActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification_success;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        action = findViewById(R.id.action);
    }

    @Override
    protected void initListener() {
        super.initListener();
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
