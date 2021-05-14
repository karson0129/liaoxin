package com.hyphenate.liaoxin.common.net.request;

import java.io.Serializable;

public class SendCodeRequest implements Serializable {
//    {"resultType":"object","modelType":null,"data":"3708","returnCode":0,"message":"OK","action":null}

    public String data;
    public int returnCode;
    public String message;
}
