package com.hyphenate.liaoxin.common.net.bean;

public class SendCodeBean extends BaseRequestBean {

//    "telephone": "string",
//            "type": 0  重要:发送类型(Type) 0:登录 1:找回密码 4:修改手机号码 5:注册用户

    public String telephone;
    public int type;


    @Override
    public String toString() {
        return "SendCodeBean{" +
                "telephone='" + telephone + '\'' +
                ", type=" + type +
                '}';
    }



    public  class SendCodeType{

        public final static int Login = 0;

        public final static int RetrievePassword = 1;

        public final static int ModifyMobilePhoneNumber = 4;

        public final static int Registered = 5;

    }
}
