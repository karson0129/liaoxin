package com.hyphenate.liaoxin.common.net.callback;

import com.hyphenate.liaoxin.common.utils.ToastUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Response;

public abstract class ResultCallBack implements LiaoxinCallBack {


    @Override
    public void onSuccessResponse(Call call, Response response) {

    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (e instanceof SocketTimeoutException){
            ToastUtils.showFailToast("请求超时");
        }
    }
}
