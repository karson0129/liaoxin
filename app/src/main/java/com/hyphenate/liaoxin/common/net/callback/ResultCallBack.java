package com.hyphenate.liaoxin.common.net.callback;

import android.text.TextUtils;

import com.hyphenate.liaoxin.common.utils.ToastUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;

public abstract class ResultCallBack implements LiaoxinCallBack {


    @Override
    public void onFailure(Call call, IOException e,String str) {
        if (e != null) {
            if (e instanceof SocketTimeoutException) {
                ToastUtils.showFailToast("请求超时");
            }
        }
        if (!TextUtils.isEmpty(str)){
            ToastUtils.showFailToast(str);
        }
    }
}
