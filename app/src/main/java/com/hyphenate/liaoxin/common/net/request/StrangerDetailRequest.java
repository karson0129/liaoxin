package com.hyphenate.liaoxin.common.net.request;

public class StrangerDetailRequest {

    public StrangerDetail data;

    public class StrangerDetail{
//        clientId	string($uuid)
//        好友的唯一id
//
//        huanxinId	string
//        nullable: true
//        环信id
//
//        liaoxinNumber	string
//        nullable: true
//        聊信Id
//
//        characterSignature	string
//        nullable: true
//        个性签名
//
//        cover	string($uuid)
//        nullable: true
//        添加客户头像id
//
//        source	string
//        nullable: true
//        来自于
//
//        mutipleGroupCnt	integer($int32)
//        共同群聊
//
//        nickName	string
//        nullable: true
//        客户昵称
//
//        clientRemark	string
//        nullable: true
//        客户昵称备注(自定义)
//
//        friendShipType	integer($int32)
//        关系 0:好友 1:黑名单 2:陌生人

        public String clientId;
        public String huanxinId;
        public String liaoxinNumber;
        public String characterSignature;
        public String cover;
        public String source;
        public int mutipleGroupCnt;
        public String nickName;
        public String clientRemark;
        public int friendShipType;
    }

}
