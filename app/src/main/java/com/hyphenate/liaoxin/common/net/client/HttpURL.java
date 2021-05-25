package com.hyphenate.liaoxin.common.net.client;


/**
 * @desc 服务器配置
 */
public class HttpURL {

    public final static String SERVER_ADDRESS = "http://8.129.61.181:22001";

    /**
     *  图片地址
     * */
    public final static String PICTURE_URL = SERVER_ADDRESS + "/api/Image/GetAffix?id=";


    //用户注册
    public final static String RESGIER_CLIENT = SERVER_ADDRESS + "/api/Client/ResgierClient";

    //发送验证码 重要:发送类型(Type) 0:登录 1:找回密码 4:修改手机号码 5:注册用户
    public final static String SEND_CODE = SERVER_ADDRESS + "/api/Client/SendCode";

    //获取当前登录用户
    public final static String GET_CURRENT_CLIENT = SERVER_ADDRESS + "/api/Client/GetCurrenClient";

    //上传图片
    public final static String UPLOAD_IMAGE = SERVER_ADDRESS + "/api/Image/UploadImage";

    //修改昵称
    public final static String MODIFY_NICKNAME = SERVER_ADDRESS + "/api/Client/ModifyNickName";

    //手机号登录
    public final static String LOGIN_BY_CODE = SERVER_ADDRESS + "/api/Client/LoginByCode";

    //账号密码登录
    public final static String LOGIN = SERVER_ADDRESS + "/api/Client/Login";

    //修改密码
    public final static String CHANGE_PASSWORD = SERVER_ADDRESS + "/api/Client/ChangePassword";

    //修改头像
    public final static String MODIFY_COVER = SERVER_ADDRESS + "/api/Client/ModifyCover";

    //基本设置修改:个性签名/震动/提醒/字体大小等字段普通更改
    public final static String MODIFY_BASE_INFO = SERVER_ADDRESS + "/api/Client/ModifyBaseInfo";

    //修改资金密码
    public final static String CHANGE_COIN_PASSWORD = SERVER_ADDRESS + "/api/Client/ChangeCoinPassword";

    //设置支付密码
    public final static String SET_COIN_PASSWORD = SERVER_ADDRESS + "/api/Client/SetCoinPassword";

    //实名认证
    public final static String REAL_NAME_AUTH = SERVER_ADDRESS + "/api/Client/RealNameAuth";

    //通过手机号找回密码
    public final static String FIND_PASSWORD_BY_PHONE  = SERVER_ADDRESS + "/api/Client/FindPasswordByPhoneRequest";

    //修改手机号
    public final static String MODIFY_CLIENT_TELEPHONE = SERVER_ADDRESS + "/api/Client/ModifyClientTelephone";

    //全局搜索添加好友(聊信号/手机号码)
    public final static String SEARCH_FRIEND = SERVER_ADDRESS + "/api/ClientRelation/GlobalSearchFriend";

    //发个人红包
    public final static String CREATE_CLIENT_RED_PACKET = SERVER_ADDRESS + "/api/RedPackets/CreateClientRedPacket";

    //获取当前登录客户的银行卡
    public final static String GET_CLIENT_BANKS = SERVER_ADDRESS + "/api/Bank/GetClientBanks";

    //设置好友备注
    public final static String SET_FRIEND_NICKNAME = SERVER_ADDRESS + "/api/ClientRelation/SetFriendNickName";

    //陌生人(有可能已经是好友/黑名单)详细
    public final static String STRANGER_DETAIL = SERVER_ADDRESS + "/api/ClientRelation/ClientStrangerDetail";
}
