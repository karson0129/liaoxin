package com.hyphenate.liaoxin.common.net.client;


/**
 * @desc 服务器配置
 */
public class HttpURL {

    public final static String SERVER_ADDRESS = "http://8.129.61.181:22001";

    //用户注册
    public final static String RESGIER_CLIENT = SERVER_ADDRESS + "/api/Client/ResgierClient";

    //发送验证码 重要:发送类型(Type) 0:登录 1:找回密码 4:修改手机号码 5:注册用户
    public final static String SEND_CODE = SERVER_ADDRESS + "/api/Client/SendCode";

    //获取当前登录用户
    public final static String GET_CURRENT_CLIENT = SERVER_ADDRESS + "/api/Client/GetCurrenClient";

}
