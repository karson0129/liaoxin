package com.hyphenate.liaoxin.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;

import java.io.IOException;

import okhttp3.Call;

/**
 * 确定支付 dialog
 * */
public class CoinDialog extends Dialog implements View.OnClickListener {

    private String TAG = "CoinDialog";

    private ImageView close;
    private TextView tvCoin;
    private ArrowItemView itemType;
    private LinearLayout selected;
    private TextView tvAction;
    private TextView tvBank;

    private Context mContext;

    private OnClickLin onClickLin;
    private String mCoin;

    public void setmCoin(String mCoin) {
        this.mCoin = mCoin;
    }

    public interface OnClickLin{
        void onAction();
    }

    public void setOnClickLin(OnClickLin onClickLin) {
        this.onClickLin = onClickLin;
    }

    public CoinDialog(@NonNull Context context) {
        super(context, R.style.bottom_window_dialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();

        if (!TextUtils.isEmpty(mCoin)){
            tvCoin.setText("￥"+mCoin);
        }

        initData();

        //按空白处能取消动画
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    /**
     * 获取当前用户银行
     * */
    private void initData() {
        HttpUtils.getInstance().post(mContext, HttpURL.GET_CLIENT_BANKS, "", new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.i(TAG,"银行:" + str);
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                super.onFailure(call, e, str);
            }
        });
    }

    private void initListener() {
        close.setOnClickListener(this);
        tvAction.setOnClickListener(this);
        itemType.setOnClickListener(this);
        selected.setOnClickListener(this);
    }

    private void initView() {
        setContentView(R.layout.dialog_coin);
        close = findViewById(R.id.close);
        tvCoin = findViewById(R.id.tv_coin);
        itemType = findViewById(R.id.item_phone);
        selected = findViewById(R.id.selected);
        tvAction = findViewById(R.id.tv_action);
        tvBank = findViewById(R.id.tv_bank);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                dismiss();
                break;
            case R.id.item_phone:

                break;
            case R.id.selected:

                break;
            case R.id.tv_action:
                if (onClickLin != null){
                    onClickLin.onAction();
                }
                break;
        }
    }
}
