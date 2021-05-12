package com.hyphenate.liaoxin.common.net.callback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public interface LiaoxinCallBack  {

    void onSuccessResponse(Call call, Response response);


    void onFailure(Call call, IOException e);
}
