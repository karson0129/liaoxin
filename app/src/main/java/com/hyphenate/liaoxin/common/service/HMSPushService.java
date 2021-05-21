package com.hyphenate.liaoxin.common.service;

import com.huawei.hms.push.HmsMessageService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.EMLog;

public class HMSPushService extends HmsMessageService {

    @Override
    public void onNewToken(String token) {
        if(token != null && !token.equals("")){
            //没有失败回调，假定token失败时token为null
//            huawei hms push is available!
//                    get huawei hms push token:
//            register huawei hms push token fail!
//                    service register huawei hms push token success token:0860241031837445300011621600CN01
            EMLog.d("HWHMSPush", "service register huawei hms push token success token:" + token);
            EMClient.getInstance().sendHMSPushTokenToServer(token);
        }else{
            EMLog.e("HWHMSPush", "service register huawei hms push token fail!");
        }
    }

}
