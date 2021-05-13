package com.hyphenate.liaoxin.common.net.bean;

public class BaseRequestBean {


    public String equimentType = android.os.Build.MODEL;
    public String equimentName = android.os.Build.DEVICE;


    @Override
    public String toString() {
        return "BaseRequestBean{" +
                "equimentType='" + equimentType + '\'' +
                ", equimentName='" + equimentName + '\'' +
                '}';
    }
}
