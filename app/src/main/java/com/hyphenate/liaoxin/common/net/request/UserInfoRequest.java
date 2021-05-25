package com.hyphenate.liaoxin.common.net.request;

import java.util.List;

public class UserInfoRequest {
    public UserInfo data;

    public class UserInfo{
//        clientId	string($uuid)
//        equiments	[...]
//        cover	string($uuid)
//        nullable: true
//        头像
//
//        huanXinId	string
//        nullable: true
//        环信的Id
//
//        huanxinToken	string
//        nullable: true
//        环信token
//
//        liaoxinNumber	string
//        nullable: true
//        聊信号
//
//        nickName	string
//        nullable: true
//        昵称
//
//        coin	number($double)
//        余额
//
//        telephone	string
//        nullable: true
//        手机号码
//
//        gender	integer($int32)
//        性别 1:男 0:女
//
//        email	string
//        nullable: true
//        电子邮箱
//
//        characterSignature	string
//        nullable: true
//        个性签名
//
//        areaCode	string
//        nullable: true
//        地区
//
//        addMeNeedChecked	boolean
//        添加我为好友时需要验证.
//
//                isSetCoinPassword	boolean
//                是否已经设置了资金密码
//
//        showFriendCircle	integer($int32)
//        显示朋友圈时间范围(枚举值:ShowFriendCircleEnum)
//
//        upadteMind	boolean
//                更新提醒
//
//        fontSize	integer($int32)
//        设置->字体大小
//
//        handFree	boolean
//                听筒模式
//
//        wifiVideoPlay	boolean
//                移动网络下视频自动播放
//
//        newMessageNotication	boolean
//                新消息通知
//
//        videoMessageNotication	boolean
//                音频通话提醒
//
//        showMessageNotication	boolean
//                通知显示消息内容
//
//        appOpenWhileSound	boolean
//                打开时声音
//
//        openWhileShake	boolean
//                打开时震动


        public String clientId;
        public List<Equiments> equiments;
        public String cover;
        public String huanXinId;
        public String huanxinToken;
        public String liaoxinNumber;
        public String nickName;
        public double coin;
        public String telephone;
        public int gender;
        public String email;
        public String characterSignature;
        public String areaCode;
        public boolean addMeNeedChecked;
        public boolean isSetCoinPassword;
        public int showFriendCircle;
        public boolean upadteMind;
        public int fontSize;
        public boolean handFree;
        public boolean wifiVideoPlay;
        public boolean newMessageNotication;
        public boolean videoMessageNotication;
        public boolean showMessageNotication;
        public boolean appOpenWhileSound;
        public boolean openWhileShake;
    }


    public class Equiments{
        public String name;
        public String type;
        public String lastLoginDate;

        @Override
        public String toString() {
            return "Equiments{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    ", lastLoginDate='" + lastLoginDate + '\'' +
                    '}';
        }
    }

}
