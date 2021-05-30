package com.hyphenate.liaoxin.common.utils;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class BankUtils {


    private static BankUtils utils;

    private static List<Bank> list;


    private BankUtils(){
        initClient();
    }

    public static BankUtils getInstance(){
        if (utils == null){
            synchronized (BankUtils.class){
                if (utils == null){
                    utils = new BankUtils();
                }
            }
        }
        return utils;
    }


    private void initClient(){
        list = new ArrayList<>();
        list.add(new Bank("北部湾银行", R.drawable.lanbuwanyinhang,2));
        list.add(new Bank("北京银行", R.drawable.beijingyinhang,1));
        list.add(new Bank("渤海银行", R.drawable.bohaiyinhang,2));
        list.add(new Bank("成都农商银行", R.drawable.chengdounongshangyinhang,3));
        list.add(new Bank("成都银行", R.drawable.chengdouyinhang,1));
        list.add(new Bank("承德银行", R.drawable.chengdeyinhang,1));
        list.add(new Bank("稠州银行", R.drawable.chouzhouyinhagn,1));
        list.add(new Bank("大连银行", R.drawable.dalianyinhang,1));
        list.add(new Bank("德州银行", R.drawable.dezhouyinhang,1));
        list.add(new Bank("东营银行", R.drawable.dongyingyinhang,1));
        list.add(new Bank("福建农信", R.drawable.fujiannongxin,3));
        list.add(new Bank("抚顺银行", R.drawable.fushunyinhang,1));
        list.add(new Bank("富邦华一银行", R.drawable.fubanghuayiyinhang,3));
        list.add(new Bank("甘肃银行", R.drawable.gansuyinhang,4));
        list.add(new Bank("工商银行", R.drawable.gongshangyinhang,1));
        list.add(new Bank("广东南粤银行", R.drawable.guangdongnanyueyinhang,1));
        list.add(new Bank("广东农信银行", R.drawable.guangdongnongxinyinhang,4));
        list.add(new Bank("广发银行", R.drawable.guangfayinhang,1));
        list.add(new Bank("广州银行", R.drawable.guangzhouyinhang,1));
        list.add(new Bank("桂林银行", R.drawable.guilinyinhang,2));
        list.add(new Bank("哈尔滨银行", R.drawable.haerbinyinhang,1));
        list.add(new Bank("汉口银行", R.drawable.hankouyinhang,2));
        list.add(new Bank("杭州银行", R.drawable.hangzhouyinhang,2));
        list.add(new Bank("河北银行", R.drawable.heneiyinhang,3));
        list.add(new Bank("湖北农信", R.drawable.hubeinongxin,2));
        list.add(new Bank("湖南农信", R.drawable.hunannongxin,1));
        list.add(new Bank("华润银行", R.drawable.huarunyinhang,4));
        list.add(new Bank("华夏银行", R.drawable.huaxiayinhang,1));
        list.add(new Bank("黄河农商", R.drawable.huanghenongshaung,1));
        list.add(new Bank("徽商银行", R.drawable.heshuangyinhang,1));
        list.add(new Bank("吉林银行", R.drawable.jilinyinhang,1));
        list.add(new Bank("建设银行", R.drawable.jiansheyinhang,2));
        list.add(new Bank("江苏农信", R.drawable.jiangsunongxin,4));
        list.add(new Bank("江苏银行", R.drawable.jiangsuyinhang,2));
        list.add(new Bank("江西银行", R.drawable.jiangxiyinhang,3));
        list.add(new Bank("江阴农商", R.drawable.jiangyingnongshang,3));
        list.add(new Bank("交通银行", R.drawable.jiaotongyinhang,2));
        list.add(new Bank("锦州银行", R.drawable.jingzhouyinhang,1));
        list.add(new Bank("晋商银行", R.drawable.jinshangyinhang,1));
        list.add(new Bank("九江银行", R.drawable.jiujiangyinhang,1));
        list.add(new Bank("昆仑银行", R.drawable.kunlunyinhang,4));
        list.add(new Bank("兰州银行", R.drawable.lanzhouyinhang,2));
        list.add(new Bank("柳州银行", R.drawable.liuzhouyinhang,1));
        list.add(new Bank("洛阳银行", R.drawable.luoyangyinhang,2));
        list.add(new Bank("蒙商银行", R.drawable.mengshangyinhang,2));
        list.add(new Bank("民泰银行", R.drawable.mingtaiyinhang,1));
        list.add(new Bank("南京银行", R.drawable.nanjingyinhang,1));
        list.add(new Bank("内蒙古银行", R.drawable.neimengguyinhang,1));
        list.add(new Bank("宁波银行", R.drawable.ningboyinhang,4));
        list.add(new Bank("宁夏银行", R.drawable.ningxiayinhang,1));
        list.add(new Bank("平安银行", R.drawable.pinganyinhang,4));
        list.add(new Bank("平顶山银行", R.drawable.pingdingshanyinhang,1));
        list.add(new Bank("浦发银行", R.drawable.pufayinhang,2));
        list.add(new Bank("齐鲁银行", R.drawable.qiluyinhang,4));
        list.add(new Bank("齐商银行", R.drawable.qishangyinhang,1));
        list.add(new Bank("青海银行", R.drawable.qinghaiyinhang,1));
        list.add(new Bank("山西省农信", R.drawable.shanxishengnongxin,3));
        list.add(new Bank("上海银行", R.drawable.shanghaiyinhang,2));
        list.add(new Bank("深圳农商银行", R.drawable.shenzhennongshang,2));
        list.add(new Bank("盛京银行", R.drawable.chengjingyinhang,1));
        list.add(new Bank("顺德农村商业银行", R.drawable.shundenongcun,2));
        list.add(new Bank("苏州农商银行", R.drawable.suzhounongshang,1));
        list.add(new Bank("苏州银行", R.drawable.suzhouyinhang,3));
        list.add(new Bank("台州银行", R.drawable.taizhouyinhang,2));
        list.add(new Bank("泰隆商行", R.drawable.tailongshanghang,4));
        list.add(new Bank("唐山银行", R.drawable.tangshanyinhang,4));
        list.add(new Bank("天津银行", R.drawable.tianjingyinhang,2));
        list.add(new Bank("威海银行", R.drawable.weihaiyinhang,2));
        list.add(new Bank("潍坊银行", R.drawable.weifangyinhang,1));
        list.add(new Bank("温州银行", R.drawable.wenzhouyinhang,4));
        list.add(new Bank("无锡农商", R.drawable.wuxinongshang,1));
        list.add(new Bank("西安银行", R.drawable.xianyinhang,2));
        list.add(new Bank("兴业银行", R.drawable.xingyeyinhang,2));
        list.add(new Bank("邮储银行", R.drawable.youchuyinhang,3));
        list.add(new Bank("长春发展农村商业银行", R.drawable.changchunfazhan,4));
        list.add(new Bank("长沙银行", R.drawable.changshayinhang,1));
        list.add(new Bank("招商银行", R.drawable.zhaoshangyinhang,1));
        list.add(new Bank("浙江农信", R.drawable.zejiangnongxin,3));
        list.add(new Bank("浙商银行", R.drawable.zeshangyinhang,1));
        list.add(new Bank("郑州银行", R.drawable.zhengzhouyinhang,4));
        list.add(new Bank("中国光大银行", R.drawable.zhongguoguangda,4));
        list.add(new Bank("中国民生银行", R.drawable.zhongguominsheng,3));
        list.add(new Bank("中国银行", R.drawable.zhongguoyinhang,1));
        list.add(new Bank("中信银行", R.drawable.zhongxinyinhang,1));
    }

    public List<Bank> getList() {
        return list;
    }


    public Bank getItem(String str){
        for (Bank bank : list) {
            if (bank.name.equals(str)){
                return bank;
            }
        }
        return null;
    }


    public class Bank{
        private String name;
        private int darwable;
        private int type;

        public Bank(String name, int darwable, int type) {
            this.name = name;
            this.darwable = darwable;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getDarwable() {
            return darwable;
        }

        public void setDarwable(int darwable) {
            this.darwable = darwable;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Bank{" +
                    "name='" + name + '\'' +
                    ", darwable=" + darwable +
                    ", type=" + type +
                    '}';
        }
    }

}
