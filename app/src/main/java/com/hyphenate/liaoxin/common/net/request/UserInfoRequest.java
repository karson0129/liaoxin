package com.hyphenate.liaoxin.common.net.request;

import java.util.List;

public class UserInfoRequest {
    public UserInfo data;

    public class UserInfo{

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
