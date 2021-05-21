package com.hyphenate.liaoxin.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.zxing.WriterException;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.liaoxin.DemoApplication;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.WindowManagerUtil;
import com.yzq.zxinglibrary.encode.CodeCreator;

/**
 * 展示我的QRCODE
 * */
public class MyQrCodeDialog extends Dialog {

    private ImageView ivIcon;
    private TextView tvUsername;
    private ImageView ivCode;

    private String iconUrl;
    private String mUsername;
    private String url;


    
    public MyQrCodeDialog(@NonNull Context context) {
        super(context, R.style.bottom_window_dialog);
    }

    public MyQrCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MyQrCodeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();


        //按空白处能取消动画
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        if (params != null) {
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }
    }

    private void initData() {
        if (iconUrl != null){
            ImageLoad.into(getContext(),iconUrl,0.4f,ivIcon);
        }
        if (!TextUtils.isEmpty(mUsername)){
            tvUsername.setText(mUsername);
        }
        if (!TextUtils.isEmpty(url)){
            Bitmap bitmap = null;
            try {
                /*
                 * contentEtString：字符串内容
                 * w：图片的宽
                 * h：图片的高
                 * logo：不需要logo的话直接传null
                 * */

                Bitmap logo = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.liaoxin_logo);
                bitmap = CodeCreator.createQRCode(url, 400, 400, logo);
                Glide.with(getContext()).load(bitmap).into(ivCode);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private void initView(){
        setContentView(R.layout.dialog_my_code);
        ivIcon = findViewById(R.id.icon);
        tvUsername = findViewById(R.id.tv_username);
        ivCode = findViewById(R.id.iv_code);
    }
}
