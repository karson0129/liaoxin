package com.hyphenate.liaoxin.common.net.request;

public class SearchRequest {

    public int returnCode;
    public SearchUser data;
    public String message;


    public class SearchUser{
//        data":{"clientId":"0002eea0-79fc-45b2-8133-fc1aec8aa07d","huanxinId":"ZDeVge9RdnxBdVC",
//                "liaoxinNumber":"WSwTLnukfgkNUSa","cover":null,"friendShipType":2,"nickName":"testdata184"}
        public String clientId;
        public String huanxinId;
        public String liaoxinNumber;
        public String cover;
        public int friendShipType;
        public String nickName;


        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getHuanxinId() {
            return huanxinId;
        }

        public void setHuanxinId(String huanxinId) {
            this.huanxinId = huanxinId;
        }

        public String getLiaoxinNumber() {
            return liaoxinNumber;
        }

        public void setLiaoxinNumber(String liaoxinNumber) {
            this.liaoxinNumber = liaoxinNumber;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }


        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
