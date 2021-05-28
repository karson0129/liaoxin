package com.hyphenate.liaoxin.section.me.activity;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

public class BankActivity extends BaseInitActivity {

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, BankActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank;
    }
}
