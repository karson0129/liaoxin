package com.hyphenate.liaoxin.common.net.bean;

import java.io.Serializable;

public class RegisterBean extends BaseRequestBean implements Serializable {

    public String telephone;
    public String code;
    public String nickName;
    public String password;

    @Override
    public String toString() {
        return "RegisterBean{" +
                "telephone='" + telephone + '\'' +
                ", code='" + code + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
