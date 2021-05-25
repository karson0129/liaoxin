package com.hyphenate.liaoxin.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hyphenate.liaoxin.R;


/**
 * */
public class NoticeTitleDialog extends Dialog {

    private TextView tvTitle;//标题
    private TextView tvContent;//内容
    private TextView tvAction;//点击
    private TextView tvCancel;//取消点击

    //下面是设置的字体 可自定义
    private String mTitle;
    private String mContent;
    private String mAction;
    private String mCancel;

    private int type = 0;

    private onItemClickListener onItemClickListener;

    public interface onItemClickListener{
        void onActionClick();//action按钮点击
        void onCancelClick();//取消点击
    }


    public NoticeTitleDialog(@NonNull Context context, @NonNull String mContent) {
        super(context, R.style.MyDialog);
        this.mContent = mContent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initListener();

        if (mContent != null){
            tvContent.setText(mContent);
        }

        if (mTitle != null){
            tvTitle.setText(mTitle);
        }

        if (mCancel != null){
            tvCancel.setText(mCancel);
        }

        if (mAction != null){
            tvAction.setText(mAction);
        }

        if (type != 0){
            switch (type){
                case 1:
                    tvAction.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvAction.setVisibility(View.VISIBLE);
                    tvCancel.setVisibility(View.GONE);
                    break;
                case 3:
                    tvAction.setVisibility(View.GONE);
                    tvCancel.setVisibility(View.VISIBLE);
                    break;
            }
        }

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    /**
     * 初始化点击
     * */
    private void initListener(){
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onActionClick();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onCancelClick();
                }
            }
        });
    }

    /**
     * 初始化控件
     * */
    private void initView(){
        setContentView(R.layout.dlg_notice_title);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvAction = findViewById(R.id.tv_action);
        tvCancel = findViewById(R.id.tv_cancel);
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }

    public void setCancel(String mCancel) {
        this.mCancel = mCancel;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOnItemClickListener(NoticeTitleDialog.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
