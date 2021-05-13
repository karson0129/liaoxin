package com.hyphenate.liaoxin.common.net.client;

import android.content.Context;
import android.provider.Settings.Secure;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.liaoxin.common.net.bean.BaseRequestBean;

import java.security.MessageDigest;

public class JsonUitls {
    private static String uuid = null;

    /**
     * 获取 android 设备唯一id
     */
    public static String getUUID(Context context) {
        if (uuid == null) {
            uuid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        }

        return uuid;
    }

    /**
     * 32位MD5加密
     * @param string -- 待加密内容
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
