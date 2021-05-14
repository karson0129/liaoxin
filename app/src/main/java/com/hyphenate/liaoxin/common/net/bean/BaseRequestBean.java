package com.hyphenate.liaoxin.common.net.bean;

import java.io.Serializable;

public class BaseRequestBean implements Serializable {


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
