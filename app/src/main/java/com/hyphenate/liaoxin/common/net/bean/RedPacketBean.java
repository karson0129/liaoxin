package com.hyphenate.liaoxin.common.net.bean;

public class RedPacketBean extends BaseRequestBean {
//    "senderClientId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//            "receiverClientId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
//            "money": 0,
//            "type": 0,
//            "greeting": "string",
//            "coinPassword": "string",
//            "clientBankId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"

//    senderClientId	string($uuid)
//    发红包者ClientId
//
//    receiverClientId	string($uuid)
//    收红包者ClientId
//
//    money	number($double)
//    红包总金额
//
//    type	integer($int32)
//0:红包;1:转账
//
//    greeting	string
//    nullable: true
//    祝福语
//
//    coinPassword	string
//    nullable: true
//    钱包密码
//
//    clientBankId	string($uuid)
//    nullable: true

    public String senderClientId;
    public String receiverClientId;
    public Double money;
    public int type;
    public String greeting;
    public String coinPassword;
    public String clientBankId;

}
