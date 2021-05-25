package com.hyphenate.liaoxin.section.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.db.DemoDbHelper;
import com.hyphenate.liaoxin.common.enums.SearchType;
import com.hyphenate.liaoxin.common.interfaceOrImplement.OnResourceParseCallback;
import com.hyphenate.liaoxin.common.net.bean.HuanXinBean;
import com.hyphenate.liaoxin.common.net.bean.SearchBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.SearchRequest;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.contact.adapter.AddContactAdapter;
import com.hyphenate.liaoxin.section.contact.viewmodels.AddContactViewModel;
import com.hyphenate.liaoxin.section.me.activity.UserDetailActivity;
import com.hyphenate.liaoxin.section.search.SearchActivity;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.EMLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class AddContactActivity extends SearchActivity implements EaseTitleBar.OnBackPressListener, AddContactAdapter.OnItemAddClickListener {

    private String TAG = "AddContactActivity";

    private AddContactViewModel mViewModel;
    private SearchType mType;

    public static void startAction(Context context, SearchType type) {
        Intent intent = new Intent(context, AddContactActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Intent intent) {
        super.initIntent(intent);
        mType = (SearchType) getIntent().getSerializableExtra("type");
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar.setTitle(getString(R.string.em_search_add_contact));
        query.setHint(getString(R.string.em_search_add_contact_hint));
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel = new ViewModelProvider(mContext).get(AddContactViewModel.class);
        mViewModel.getAddContact().observe(this, response -> {
            parseResource(response, new OnResourceParseCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    showToast(getResources().getString(R.string.em_add_contact_send_successful));
                }
            });

        });
        //获取本地的好友列表
        List<String> localUsers = null;
        if(DemoDbHelper.getInstance(mContext).getUserDao() != null) {
            localUsers = DemoDbHelper.getInstance(mContext).getUserDao().loadAllUsers();
        }
        for (int i = 0;i < localUsers.size();i++){
            Log.i(TAG,"id:"+localUsers.get(i));
        }
//        ((AddContactAdapter)adapter).addLocalContacts(localUsers);
        getLocalContacts(localUsers);

        ((AddContactAdapter)adapter).setOnItemAddClickListener(this);
    }

    private void getLocalContacts(List<String> stringList){
        String[] userId = stringList.toArray(new String[stringList.size()]);
        EMUserInfo.EMUserInfoType[] userInfoTypes = new EMUserInfo.EMUserInfoType[4];
        userInfoTypes[0] = EMUserInfo.EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfo.EMUserInfoType.AVATAR_URL;
        userInfoTypes[2] = EMUserInfo.EMUserInfoType.GENDER;
        userInfoTypes[3] = EMUserInfo.EMUserInfoType.SIGN;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMUserInfo userInfo = userInfos.get(EMClient.getInstance().getCurrentUser());
//                        EMUserInfo: {"avatarUrl":"","birth":"","email":"","ext":"","gender":0,"nickName":"","phoneNumber":"","signature":"","userId":"qsidzqgvtvhkayv"}
                        Log.i(TAG,"EMUserInfo: "+new Gson().toJson(userInfo));

                        List<EaseUser> userList = new ArrayList<>();

                        for (Map.Entry<String, EMUserInfo> entry : userInfos.entrySet()) {
                            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                            EMUserInfo info = entry.getValue();

                            EaseUser easeUser = new EaseUser();
                            easeUser.setUsername(info.getUserId());
                            easeUser.setNickname(info.getNickName());
                            easeUser.setAvatar(info.getAvatarUrl());
                            easeUser.setBirth(info.getBirth());
                            easeUser.setGender(info.getGender());
                            easeUser.setEmail(info.getEmail());
                            easeUser.setExt(info.getExt());
                        }
                        ((AddContactAdapter)adapter).addLocalContacts(userList);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG,"fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });
    }

    @Override
    protected EaseBaseRecyclerViewAdapter getAdapter() {
        return new AddContactAdapter();
    }

    @Override
    public void searchMessages(String query) {
        // you can search the user from your app server here.
//        if(!TextUtils.isEmpty(query)) {
//            if (adapter.getData() != null && !adapter.getData().isEmpty()) {
//                adapter.clearData();
//            }
//            adapter.addData(query);
//        }

        getSearchFriend(query);
    }

    @Override
    protected void onChildItemClick(View view, int position) {
        // 跳转到好友页面
//        String item = (String) adapter.getItem(position);
//        EaseUser user = new EaseUser(item);
        EaseUser user = (EaseUser) adapter.getItem(position);
        if (user.getUsername().toLowerCase().equals(EMClient.getInstance().getCurrentUser().toLowerCase())){
            UserDetailActivity.actionStart(mContext,null,null);
        }else {
            ContactDetailActivity.actionStart(mContext, user, isFri(user.getUsername().toLowerCase()));
        }
    }

    /**
     * 判断是否是朋友
     * */
    private boolean isFri(String str){
        boolean isFri = false;
        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            for (String username : usernames) {
                if (str.equals(username.toLowerCase())){
                    isFri = true;
                }
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return isFri;
    }



    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onItemAddClick(View view, int position) {
        // 添加好友
        mViewModel.addContact((String) adapter.getItem(position), getResources().getString(R.string.em_add_contact_add_a_friend));
    }

    private void getSearchFriend(String str){
        showLoading();
        SearchBean bean = new SearchBean();
        bean.searchText = str;
        Log.i(TAG,"参数："+new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.SEARCH_FRIEND, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG,"搜索："+str);
//                {"resultType":"object","modelType":null,"data":{"clientId":"0002eea0-79fc-45b2-8133-fc1aec8aa07d","huanxinId":"ZDeVge9RdnxBdVC",
//                "liaoxinNumber":"WSwTLnukfgkNUSa","cover":null,"friendShipType":2,"nickName":"testdata184"},"returnCode":0,"message":"OK","action":null}
                try {
                    SearchRequest request = new Gson().fromJson(str,SearchRequest.class);
                    if (request != null){
//                        getSearchUserInfo(request.data.getHuanxinId().toLowerCase());

                        EaseUser user = new EaseUser();
                        if (!TextUtils.isEmpty(request.data.huanxinId)){
                            user.setUsername(request.data.huanxinId.toLowerCase());
                        }
                        if (!TextUtils.isEmpty(request.data.nickName)){
                            user.setNickname(request.data.nickName);
                        }
                        if (!TextUtils.isEmpty(request.data.cover)){
                            user.setAvatar(request.data.cover);
                        }
//                        关系 0:好友 1:黑名单 2:陌生人
                        user.setContact(request.data.friendShipType == 2? 3:request.data.friendShipType);
                        if (!TextUtils.isEmpty(request.data.clientId)){
                            HuanXinBean bean = new HuanXinBean();
                            bean.clientId = request.data.clientId;
                            user.setExt(new Gson().toJson(bean));
                        }

                        if (adapter.getData() != null && !adapter.getData().isEmpty()) {
                            adapter.clearData();
                        }
                        adapter.addData(user);

                    }
                }catch (Exception e){
                    ToastUtils.showFailToast("解析错误");
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);

            }
        });
    }

    /**
     * 获取环信信息
     * */
    private void getSearchUserInfo(String uid){
        String[] userId = new String[1];
        userId[0] = uid;
        EMClient.getInstance().userInfoManager().fetchUserInfoByUserId(userId,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMUserInfo userInfo = userInfos.get(uid);
//                        EMUserInfo: {"avatarUrl":"","birth":"","email":"","ext":"","gender":0,"nickName":"","phoneNumber":"","signature":"","userId":"qsidzqgvtvhkayv"}
                        Log.i(TAG,"EMUserInfo: "+new Gson().toJson(userInfo));

                        EaseUser user = new EaseUser();
                        if (!TextUtils.isEmpty(userInfo.getUserId())){
                            user.setUsername(userInfo.getUserId());
                        }
                        if (!TextUtils.isEmpty(userInfo.getNickName())){
                            user.setNickname(userInfo.getNickName());
                        }
                        if (!TextUtils.isEmpty(userInfo.getAvatarUrl())){
                            user.setAvatar(userInfo.getAvatarUrl());
                        }

                        if (adapter.getData() != null && !adapter.getData().isEmpty()) {
                            adapter.clearData();
                        }
                        adapter.addData(user);
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG,"fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });
    }
}
