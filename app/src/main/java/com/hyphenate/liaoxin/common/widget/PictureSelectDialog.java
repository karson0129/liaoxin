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
 * 图片选择 Dialog
 *
 * @author karson
 * @version 创建时间：2020/2/18
 * */
public class PictureSelectDialog extends Dialog {

    private TextView tvCamera;
    private TextView tvPhoto;
    private TextView tvCancel;

    private onPictureSelectListener mPictureSelectListener;

    public interface onPictureSelectListener{
        void onCamera();//去拍照
        void onPhoto();//从相册选择图片
    }

    public PictureSelectDialog(@NonNull Context context) {
        super(context, R.style.bottom_window_dialog);
    }

    public void setOnPictureSelectListener(onPictureSelectListener mPictureSelectListener) {
        this.mPictureSelectListener = mPictureSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();

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

    private void initListener(){
        //取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        //拍照
        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPictureSelectListener != null){
                    mPictureSelectListener.onCamera();
                }
                dismiss();
            }
        });
        //从相册选择图片
        tvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPictureSelectListener != null){
                    mPictureSelectListener.onPhoto();
                }
                dismiss();
            }
        });

    }

    private void initView(){
        setContentView(R.layout.dialog_picture_select);
        tvCamera = findViewById(R.id.tv_take_camera);
        tvPhoto = findViewById(R.id.tv_take_photo);
        tvCancel = findViewById(R.id.tv_cancel);
    }

}
