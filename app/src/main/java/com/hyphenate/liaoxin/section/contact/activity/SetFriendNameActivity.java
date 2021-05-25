package com.hyphenate.liaoxin.section.contact.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

/**
 * 好友备注
 * */
public class SetFriendNameActivity extends BaseInitActivity implements View.OnClickListener, TextWatcher {

    private String TAG = "SetFriendNameActivity";
    private EaseTitleBar titleBar;
    private EditText inputNickName;
    private Button saveNickName;
    private String nickName;

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_offline_push;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        inputNickName = (EditText) findViewById(R.id.et_input_nickname);
        saveNickName = (Button) findViewById(R.id.btn_save);
        titleBar.setTitle("备注");
        inputNickName.setHint("请输入备注");
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(new EaseTitleBar.OnBackPressListener() {
            @Override
            public void onBackPress(View view) {
                onBackPressed();
            }
        });
        saveNickName.setOnClickListener(this);
        inputNickName.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                if (TextUtils.isEmpty(nickName)){
                    ToastUtils.showToast("请输入备注");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("beizhu",nickName);
                setResult(Activity.RESULT_OK,intent);
                finish();
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
        nickName = inputNickName.getText().toString().trim();
    }
}
