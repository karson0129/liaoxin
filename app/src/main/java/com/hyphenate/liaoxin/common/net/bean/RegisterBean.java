package com.hyphenate.liaoxin.common.net.bean;

public class RegisterBean extends BaseRequestBean {

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
