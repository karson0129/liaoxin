package com.hyphenate.liaoxin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easecallkit.base.EaseCallType;
import com.hyphenate.easecallkit.ui.EaseMultipleVideoActivity;
import com.hyphenate.easecallkit.ui.EaseVideoCallActivity;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.enums.SearchType;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.net.bean.HuanXinBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.UserInfoRequest;
import com.hyphenate.liaoxin.common.permission.PermissionsManager;
import com.hyphenate.liaoxin.common.permission.PermissionsResultAction;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.PushUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.MainViewModel;
import com.hyphenate.liaoxin.section.addfriend.AddFriendActivity;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.chat.ChatPresenter;
import com.hyphenate.liaoxin.section.contact.activity.GroupContactManageActivity;
import com.hyphenate.liaoxin.section.contact.fragment.ContactListFragment;
import com.hyphenate.liaoxin.section.conversation.ConversationListFragment;
import com.hyphenate.liaoxin.section.discover.DiscoverFragment;
import com.hyphenate.liaoxin.section.contact.activity.AddContactActivity;
import com.hyphenate.liaoxin.section.contact.viewmodels.ContactsViewModel;
import com.hyphenate.liaoxin.section.group.activity.GroupPrePickActivity;
import com.hyphenate.liaoxin.section.me.AboutMeFragment;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.ui.base.EaseBaseFragment;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.EMLog;
import com.hyphenate.chat.EMUserInfo.*;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;


import okhttp3.Call;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MainActivity extends BaseInitActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private String TAG = "MainActivity";

    private int REQUEST_CODE_SCAN = 1001;

    private BottomNavigationView navView;
    private EaseTitleBar mTitleBar;
    private EaseBaseFragment mConversationListFragment, mFriendsFragment, mDiscoverFragment, mAboutMeFragment;
    private EaseBaseFragment mCurrentFragment;
    private TextView mTvMainHomeMsg, mTvMainFriendsMsg, mTvMainDiscoverMsg, mTvMainAboutMeMsg;
    private int[] badgeIds = {R.layout.demo_badge_home, R.layout.demo_badge_friends, R.layout.demo_badge_discover, R.layout.demo_badge_about_me};
    private int[] msgIds = {R.id.tv_main_home_msg, R.id.tv_main_friends_msg, R.id.tv_main_discover_msg, R.id.tv_main_about_me_msg};
    private MainViewModel viewModel;
    private boolean showMenu = true;//是否显示菜单项

    private Drawable homeIv;
    private Drawable addressBookIv;

    public static void startAction(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_main;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mCurrentFragment != null) {
            if(mCurrentFragment instanceof ContactListFragment) {
                menu.findItem(R.id.action_group).setVisible(false);
                menu.findItem(R.id.action_friend).setVisible(false);
                menu.findItem(R.id.action_search_friend).setVisible(true);
                menu.findItem(R.id.action_search_group).setVisible(true);
                menu.findItem(R.id.action_scan).setVisible(true);
                menu.findItem(R.id.action_receiving_code).setVisible(true);
            }else {
                menu.findItem(R.id.action_group).setVisible(true);
                menu.findItem(R.id.action_friend).setVisible(true);
                menu.findItem(R.id.action_scan).setVisible(true);
                menu.findItem(R.id.action_receiving_code).setVisible(true);
                menu.findItem(R.id.action_search_friend).setVisible(false);
                menu.findItem(R.id.action_search_group).setVisible(false);
            }
        }
        return showMenu;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.demo_conversation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_video :
                break;
            case R.id.action_group :
                GroupPrePickActivity.actionStart(mContext);
                break;
            case R.id.action_friend :
            case R.id.action_search_friend :
//                AddContactActivity.startAction(mContext, SearchType.CHAT);
                AddFriendActivity.startAction(mContext);
                break;
            case R.id.action_search_group :
                GroupContactManageActivity.actionStart(mContext, true);
                break;
            case R.id.action_scan :
                goScan();
                break;
            case R.id.action_receiving_code:
                showToast("收款码");
                break;
        }
        return true;
    }

    private void goScan(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 200;
            String[] permissions = {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
            };
            boolean isPermissions = true;
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    isPermissions = false;
                    this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                }
            }
            if (isPermissions){
                scan();
            }
        }else {
            scan();
        }
    }

    private void scan(){
        Intent intentScan = new Intent(mContext, CaptureActivity.class);
        /*ZxingConfig是配置类
         *可以设置是否显示底部布局，闪光灯，相册，
         * 是否播放提示音  震动
         * 设置扫描框颜色等
         * 也可以不传这个参数
         * */
        ZxingConfig config = new ZxingConfig();
        config.setPlayBeep(true);//是否播放扫描声音 默认为true
        config.setShake(true);//是否震动  默认为true
        config.setDecodeBarCode(false);//是否扫描条形码 默认为true
        config.setReactColor(R.color.white);//设置扫描框四个角的颜色 默认为淡蓝色
        config.setFrameLineColor(R.color.white);//设置扫描框边框颜色 默认无色
        config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
        intentScan.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intentScan, REQUEST_CODE_SCAN);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
        if (requestCode == 200){
            boolean isPermissions = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1){
                    isPermissions = false;
                }
            }
            if (isPermissions){
                scan();
            }else {
                ToastUtils.showFailToast("未给予权限");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                ToastUtils.showToast(content);
//                result.setText("扫描结果为：" + content);
            }
        }
    }

    /**
     * 显示menu的icon，通过反射，设置menu的icon显示
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if(menu != null) {
            if(menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    protected void initSystemFit() {
        setFitSystemForTheme(false);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        homeIv = getResources().getDrawable(R.drawable.home_more);
        addressBookIv = getResources().getDrawable(R.drawable.address_add_buddy);
        navView = findViewById(R.id.nav_view);
        mTitleBar = findViewById(R.id.title_bar_main);
        navView.setItemIconTintList(null);
        // 可以动态显示隐藏相应tab
        //navView.getMenu().findItem(R.id.em_main_nav_me).setVisible(false);
        switchToHome();
        checkIfShowSavedFragment(savedInstanceState);
        addTabBadge();
    }

    @Override
    protected void initListener() {
        super.initListener();
        navView.setOnNavigationItemSelectedListener(this);
        initFriendListener();
    }

    @Override
    protected void initData() {
        super.initData();
        initViewModel();
        requestPermissions();
        checkUnreadMsg();
        ChatPresenter.getInstance().init();
        // 获取华为 HMS 推送 token
        HMSPushHelper.getInstance().getHMSToken(this);

        //判断是否为来电推送
        if(PushUtils.isRtcCall){
            if (EaseCallType.getfrom(PushUtils.type) != EaseCallType.CONFERENCE_CALL) {
                    EaseVideoCallActivity callActivity = new EaseVideoCallActivity();
                    Intent intent = new Intent(getApplicationContext(), callActivity.getClass()).addFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                } else {
                    EaseMultipleVideoActivity callActivity = new EaseMultipleVideoActivity();
                    Intent intent = new Intent(getApplication().getApplicationContext(), callActivity.getClass()).addFlags(FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
            PushUtils.isRtcCall  = false;
        }
        initUserInfo();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(mContext).get(MainViewModel.class);
        viewModel.getSwitchObservable().observe(this, response -> {
            if(response == null || response == 0) {
                return;
            }
            if(response == R.string.em_main_title_me) {
                mTitleBar.setVisibility(View.GONE);
            }else {
                mTitleBar.setVisibility(View.VISIBLE);
                mTitleBar.setTitle(getResources().getString(response));
            }
        });

        viewModel.homeUnReadObservable().observe(this, readCount -> {
            if(!TextUtils.isEmpty(readCount)) {
                mTvMainHomeMsg.setVisibility(View.VISIBLE);
                mTvMainHomeMsg.setText(readCount);
            }else {
                mTvMainHomeMsg.setVisibility(View.GONE);
            }
        });

        //加载联系人
        ContactsViewModel contactsViewModel = new ViewModelProvider(mContext).get(ContactsViewModel.class);
        contactsViewModel.loadContactList(true);

        viewModel.messageChangeObservable().with(DemoConstant.GROUP_CHANGE, EaseEvent.class).observe(this, this::checkUnReadMsg);
        viewModel.messageChangeObservable().with(DemoConstant.NOTIFY_CHANGE, EaseEvent.class).observe(this, this::checkUnReadMsg);
        viewModel.messageChangeObservable().with(DemoConstant.MESSAGE_CHANGE_CHANGE, EaseEvent.class).observe(this, this::checkUnReadMsg);

        viewModel.messageChangeObservable().with(DemoConstant.CONVERSATION_DELETE, EaseEvent.class).observe(this, this::checkUnReadMsg);
        viewModel.messageChangeObservable().with(DemoConstant.CONTACT_CHANGE, EaseEvent.class).observe(this, this::checkUnReadMsg);
        viewModel.messageChangeObservable().with(DemoConstant.CONVERSATION_READ, EaseEvent.class).observe(this, this::checkUnReadMsg);

    }

    private void checkUnReadMsg(EaseEvent event) {
        if(event == null) {
            return;
        }
        viewModel.checkUnreadMsg();
    }

    /**
     * 添加BottomNavigationView中每个item右上角的红点
     */
    private void addTabBadge() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navView.getChildAt(0);
        int childCount = menuView.getChildCount();
        Log.e("TAG", "bottom child count = "+childCount);
        BottomNavigationItemView itemTab;
        for(int i = 0; i < childCount; i++) {
            itemTab = (BottomNavigationItemView) menuView.getChildAt(i);
            View badge = LayoutInflater.from(mContext).inflate(badgeIds[i], menuView, false);
            switch (i) {
                case 0 :
                    mTvMainHomeMsg = badge.findViewById(msgIds[0]);
                    break;
                case 1 :
                    mTvMainFriendsMsg = badge.findViewById(msgIds[1]);
                    break;
                case 2 :
                    mTvMainDiscoverMsg = badge.findViewById(msgIds[2]);
                    break;
                case 3 :
                    mTvMainAboutMeMsg = badge.findViewById(msgIds[3]);
                    break;
            }
            itemTab.addView(badge);
        }
    }

    /**
     * 用于展示是否已经存在的Fragment
     * @param savedInstanceState
     */
    private void checkIfShowSavedFragment(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            String tag = savedInstanceState.getString("tag");
            if(!TextUtils.isEmpty(tag)) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if(fragment instanceof EaseBaseFragment) {
                    replace((EaseBaseFragment) fragment, tag);
                }
            }
        }
    }

    /**
     * 申请权限
     */
    // TODO: 2019/12/19 0019 有必要修改一下
    private void requestPermissions() {
        PermissionsManager.getInstance()
                .requestAllManifestPermissionsIfNecessary(mContext, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {

                    }
                });
    }

    private void switchToHome() {
        if(mConversationListFragment == null) {
            mConversationListFragment = new ConversationListFragment();
        }
        replace(mConversationListFragment, "conversation");
    }

    private void switchToFriends() {
        if(mFriendsFragment == null) {
            mFriendsFragment = new ContactListFragment();
        }
        replace(mFriendsFragment, "contact");
    }

    private void switchToDiscover() {
        if(mDiscoverFragment == null) {
            mDiscoverFragment = new DiscoverFragment();
        }
        replace(mDiscoverFragment, "discover");
    }

    private void switchToAboutMe() {
        if(mAboutMeFragment == null) {
            mAboutMeFragment = new AboutMeFragment();
        }
        //用户信息
        fetchSelfInfo();
        replace(mAboutMeFragment, "me");
    }

    private void replace(EaseBaseFragment fragment, String tag) {
        if(mCurrentFragment != fragment) {
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            if(mCurrentFragment != null) {
                t.hide(mCurrentFragment);
            }
            mCurrentFragment = fragment;
            if(!fragment.isAdded()) {
                t.add(R.id.fl_main_fragment, fragment, tag).show(fragment).commit();
            }else {
                t.show(fragment).commit();
            }
        }
    }

    private void fetchSelfInfo(){
        String[] userId = new String[1];
        userId[0] = EMClient.getInstance().getCurrentUser();
        EMUserInfoType[] userInfoTypes = new EMUserInfoType[2];
        userInfoTypes[0] = EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfoType.AVATAR_URL;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                       EMUserInfo userInfo = userInfos.get(EMClient.getInstance().getCurrentUser());
                        //昵称
                        if(userInfo != null && userInfo.getNickName() != null &&
                                userInfo.getNickName().length() > 0){
                            EaseEvent event = EaseEvent.create(DemoConstant.NICK_NAME_CHANGE, EaseEvent.TYPE.CONTACT);
                            event.message = userInfo.getNickName();
                            LiveDataBus.get().with(DemoConstant.NICK_NAME_CHANGE).postValue(event);
                            PreferenceManager.getInstance().setCurrentUserNick(userInfo.getNickName());
                        }
                        //头像
                        if(userInfo != null && userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() > 0){

                            EaseEvent event = EaseEvent.create(DemoConstant.AVATAR_CHANGE, EaseEvent.TYPE.CONTACT);
                            event.message = userInfo.getAvatarUrl();
                            LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE).postValue(event);
                            PreferenceManager.getInstance().setCurrentUserAvatar(userInfo.getAvatarUrl());
                        }
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e("MainActivity","fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        mTitleBar.setVisibility(View.VISIBLE);
        showMenu = true;
        boolean showNavigation = false;
        switch (menuItem.getItemId()) {
            case R.id.em_main_nav_home :
                switchToHome();
                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_liaoxin));
                mTitleBar.getToolbar().setOverflowIcon(homeIv);
                showNavigation = true;
                break;
            case R.id.em_main_nav_friends :
                switchToFriends();
                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_friends));
                mTitleBar.getToolbar().setOverflowIcon(addressBookIv);
                showNavigation = true;
                invalidateOptionsMenu();
                break;
            case R.id.em_main_nav_discover :
                switchToDiscover();
                mTitleBar.setTitle(getResources().getString(R.string.em_main_title_discover));
                showNavigation = true;
                break;
            case R.id.em_main_nav_me :
                switchToAboutMe();
                mTitleBar.setTitle("");
                showMenu = false;
                showNavigation = true;
                break;
        }
        invalidateOptionsMenu();
        return showNavigation;
    }

    private void checkUnreadMsg() {
        viewModel.checkUnreadMsg();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoHelper.getInstance().showNotificationPermissionDialog();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mCurrentFragment != null) {
            outState.putString("tag", mCurrentFragment.getTag());
        }
    }

    /**
     *  获取用户信息
     * */
    private void initUserInfo(){
        HttpUtils.getInstance().post(mContext,HttpURL.GET_CURRENT_CLIENT, "", new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                Log.i(TAG,"自己的用户信息:"+str);
                UserInfoRequest request = new Gson().fromJson(str,UserInfoRequest.class);
                PrefUtils.setString(mContext, UserConstant.ClientId,request.data.clientId);
                PrefUtils.setString(mContext, UserConstant.Phone,request.data.telephone);
                PrefUtils.setBoolean(mContext, UserConstant.isCoinPassword,request.data.isSetCoinPassword);
                PrefUtils.setBoolean(mContext, UserConstant.isRealAuth,request.data.isRealAuth);
                upUserInfo(request.data.clientId);
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                super.onFailure(call, e,str);
            }
        });
    }

    /**
     * 上传到环信里面
     * */
    private void upUserInfo(String str){
        HuanXinBean bean = new HuanXinBean();
        bean.clientId = str;
        EMClient.getInstance().userInfoManager().updateOwnInfoByAttribute(EMUserInfoType.EXT, new Gson().toJson(bean), new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                EMLog.d(TAG, "updateOwnInfoByAttribute :" + value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.d(TAG, "updateOwnInfoByAttribute  error:" + error + " errorMsg:" + errorMsg);
            }
        });
    }

    /***
     * 注册
     * 应用通过此接口设置回调，获得联系人变化
     */
    private void initFriendListener(){
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            /**
             * 收到好友邀请
             * @param username 发起加为好友用户的名称
             * @param reason   对方发起好友邀请时发出的文字性描述
             */
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
            }


            /**
             * 好友请求被同意
             * @param username
             */
            @Override
            public void onFriendRequestAccepted(String username) {

            }


            /**
             * 好友请求被拒绝
             * @param username
             */
            @Override
            public void onFriendRequestDeclined(String username) {

            }

            /**
             * 被删除时回调此方法
             * @param username 删除的联系人
             */
            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
            }

            /**
             * 增加联系人时回调此方法
             * @param username 增加的联系人
             */
            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
            }
        });
    }
}
