package com.hyphenate.liaoxin.section.addfriend;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.enums.SearchType;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.common.widget.MyQrCodeDialog;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.contact.activity.AddContactActivity;
import com.hyphenate.util.EMLog;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.util.Map;

/***
 * 添加好友
 */
public class AddFriendActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, View.OnClickListener {

    private String TAG = "AddFriendActivity";

    private int REQUEST_CODE_SCAN = 1002;

    private EaseTitleBar titleBar;
    private LinearLayout linSearch;
    private LinearLayout linCode;
    private ArrowItemView itemMobile;
    private ArrowItemView itemScan;

    private MyQrCodeDialog dialog;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, AddFriendActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar_main);
        linSearch = findViewById(R.id.tv_search);
        linCode = findViewById(R.id.my_code);
        itemMobile = findViewById(R.id.item_mobile);
        itemScan = findViewById(R.id.item_scan);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        linSearch.setOnClickListener(this);
        linCode.setOnClickListener(this);
        itemMobile.setOnClickListener(this);
        itemScan.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search://搜索
                AddContactActivity.startAction(mContext, SearchType.CHAT);
                break;
            case R.id.my_code://二维码
                showMyCode();
                break;
            case R.id.item_mobile://通讯录
                AddressBookActivity.startAction(mContext);
                break;
            case R.id.item_scan://扫一扫
                goScan();
                break;
        }
    }

    private void showMyCode(){
        String[] userId = new String[1];
        userId[0] = EMClient.getInstance().getCurrentUser();
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
                        Log.i(TAG,"EMUserInfo: "+new Gson().toJson(userInfo));

                        if (dialog == null){
                            dialog = new MyQrCodeDialog(mContext);
                        }
                        dialog.setIconUrl(userInfo.getAvatarUrl());
                        dialog.setmUsername(userInfo.getNickName());
                        dialog.setUrl(userInfo.getUserId());
                        dialog.show();
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG,"fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });
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
}
