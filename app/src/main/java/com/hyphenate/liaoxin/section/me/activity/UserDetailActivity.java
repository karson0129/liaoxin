package com.hyphenate.liaoxin.section.me.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.PicRequest;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.PermissionsUtils;
import com.hyphenate.liaoxin.common.utils.PictureUtils;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.ArrowItemView;
import com.hyphenate.liaoxin.common.widget.PictureSelectDialog;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.util.EMLog;
import com.hyphenate.chat.EMUserInfo.*;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import okhttp3.Call;

public class UserDetailActivity extends BaseInitActivity implements View.OnClickListener, PictureSelectDialog.onPictureSelectListener {
    static private String TAG =  "UserDetailActivity";
    private EaseTitleBar titleBar;
    private ArrowItemView itemNickname;
    private EaseImageView headImageView;
    private String headImageUrl = null;
    private String nickName;
    private LinearLayout itemArrow;
    private LinearLayout itemCode;
    private ArrowItemView itemLiaoxinhao;
    private ArrowItemView itemSex;
    private ArrowItemView itemQianming;
    private PictureSelectDialog dialog;

    private String mFilePath;

    public static void actionStart(Context context,String nickName,String url) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("imageUrl",url);
        intent.putExtra("nickName",nickName);
        context.startActivity(intent);
    }

    /**
     * init intent
     * @param intent
     */
    @Override
    protected void initIntent(Intent intent){
        if(intent != null){
            headImageUrl = intent.getStringExtra("imageUrl");
            nickName = intent.getStringExtra("nickName");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_activity_user_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        itemQianming = findViewById(R.id.item_qianming);
        itemSex = findViewById(R.id.item_sex);
        itemCode = findViewById(R.id.item_code);
        itemLiaoxinhao = findViewById(R.id.item_liaoxinhao);
        itemArrow = findViewById(R.id.item_arrow);
        titleBar = findViewById(R.id.title_bar);
        itemNickname = findViewById(R.id.item_nickname);
        headImageView = findViewById(R.id.tv_headImage_view);
        dialog = new PictureSelectDialog(this);
        dialog.setOnPictureSelectListener(this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(new EaseTitleBar.OnBackPressListener() {
            @Override
            public void onBackPress(View view) {
                onBackPressed();
            }
        });
        itemNickname.setOnClickListener(this);
        itemArrow.setOnClickListener(this);
        itemLiaoxinhao.setOnClickListener(this);
        itemCode.setOnClickListener(this);
        itemSex.setOnClickListener(this);
        itemQianming.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_nickname://??????
                Intent intent = new Intent(mContext, OfflinePushNickActivity.class);
                intent.putExtra("nickName",nickName);
                startActivityForResult(intent, 2);
                break;
            case R.id.item_arrow://??????
//                Intent intent2 = new Intent(mContext, ChooseHeadImageActivity.class);
//                intent2.putExtra("headUrl",headImageUrl);
//                startActivityForResult(intent2, 1);
                //????????? ??????Dialog??????show???show
                if (dialog!= null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.item_liaoxinhao://?????????

                break;
            case R.id.item_code://?????????

                break;
            case R.id.item_sex://??????

                break;
            case R.id.item_qianming://??????

                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        if(headImageUrl != null && headImageUrl.length()> 0){
            ImageLoad.into(mContext,headImageUrl,0.5f,headImageView);
        }
        if(headImageUrl == null || nickName == null){
            intSelfDate();
        }

        //????????????????????????
        addLiveDataObserver();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == 1 && resultCode == RESULT_OK)) {
            if(data != null) {
                headImageUrl = data.getStringExtra("headImage");
                ImageLoad.into(mContext,headImageUrl,0.5f,headImageView);
            }
        }else if((requestCode == 2 && resultCode == RESULT_OK)) {
            if(data != null) {
                nickName = data.getStringExtra("nickName");
            }
        }else if ((requestCode == 3 && resultCode == RESULT_OK)){
            controlPhoto(data);
        }else if ((requestCode == 4 && resultCode == RESULT_OK)){
            Log.i(TAG," path:"+ mFilePath);
            if (!TextUtils.isEmpty(mFilePath)){
                controlPhotoPath(mFilePath);
            }
        }
    }

    /**
     * ?????? ?????? ??????
     * */
    private void controlPhotoPath(String data) {
        if (data == null) {
            return;
        }
        Log.i(TAG, "???????????????" + data);
        showLoading();
        HttpUtils.getInstance().uploadImage(mContext, HttpURL.UPLOAD_IMAGE, data, new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG, "?????????" + str);
//                {"id":"bd889bde-b6b6-4a0f-a279-e51549ffd941"}
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    upDataArrow(HttpURL.PICTURE_URL + request.id);
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call,e,str);
                Log.i(TAG, "??????");
            }
        });
    }

    /**
     * ?????? ?????? ??????
     * */
    private void controlPhoto(Intent data) {
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        String filePath = PictureUtils.getRealFilePath(mContext, uri);
        Log.i(TAG, "???????????????" + filePath);
//        ImageLoad.into(mContext,filePath,0.5f,headImageView);
        showLoading();
        HttpUtils.getInstance().uploadImage(mContext, HttpURL.UPLOAD_IMAGE, filePath, new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG, "?????????" + str);
//                {"id":"bd889bde-b6b6-4a0f-a279-e51549ffd941"}
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    upDataArrow(HttpURL.PICTURE_URL + request.id);
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call,e,str);
                Log.i(TAG, "??????");
            }
        });
    }

    /**
     * ????????????
     * */
    private void upDataArrow(String selectHeadUrl){
        EMClient.getInstance().userInfoManager().updateOwnInfoByAttribute(EMUserInfoType.AVATAR_URL, selectHeadUrl, new EMValueCallBack<String>() {
            @Override
            public void onSuccess(String value) {
                EMLog.d(TAG, "updateOwnInfoByAttribute :" + value);
                showToast(R.string.demo_head_image_update_success);
                PreferenceManager.getInstance().setCurrentUserAvatar(selectHeadUrl);
                EaseEvent event = EaseEvent.create(DemoConstant.AVATAR_CHANGE, EaseEvent.TYPE.CONTACT);
                //???????????????????????????
                event.message = selectHeadUrl;
                LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE).postValue(event);
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.d(TAG, "updateOwnInfoByAttribute  error:" + error + " errorMsg:" + errorMsg);
                showToast(R.string.demo_head_image_update_failed);
            }
        });
    }

    private void intSelfDate(){
        String[] userId = new String[1];
        userId[0] = EMClient.getInstance().getCurrentUser();
        EMUserInfoType [] userInfoTypes = new EMUserInfoType[5];
        userInfoTypes[0] = EMUserInfoType.NICKNAME;
        userInfoTypes[1] = EMUserInfoType.AVATAR_URL;
        userInfoTypes[2] = EMUserInfoType.GENDER;
        userInfoTypes[3] = EMUserInfoType.SIGN;
        userInfoTypes[4] = EMUserInfoType.EXT;
        EMClient.getInstance().userInfoManager().fetchUserInfoByAttribute(userId, userInfoTypes,new EMValueCallBack<Map<String, EMUserInfo>>() {
            @Override
            public void onSuccess(Map<String, EMUserInfo> userInfos) {
                runOnUiThread(new Runnable() {
                    public void run() {
                       EMUserInfo userInfo = userInfos.get(EMClient.getInstance().getCurrentUser());
//                        EMUserInfo: {"avatarUrl":"","birth":"","email":"","ext":"","gender":0,"nickName":"","phoneNumber":"","signature":"","userId":"qsidzqgvtvhkayv"}
                       Log.i(TAG,"EMUserInfo: "+new Gson().toJson(userInfo));

                        //??????
                        if(userInfo != null && userInfo.getNickName() != null &&
                                userInfo.getNickName().length() > 0){
                            nickName = userInfo.getNickName();
                            itemNickname.getTvContent().setText(nickName);
                            PreferenceManager.getInstance().setCurrentUserNick(nickName);
                        }
                        //??????
                        if(userInfo != null && userInfo.getAvatarUrl() != null && userInfo.getAvatarUrl().length() > 0){
                            headImageUrl = userInfo.getAvatarUrl();
                            ImageLoad.into(mContext,headImageUrl,0.5f,headImageView);
                            PreferenceManager.getInstance().setCurrentUserAvatar(headImageUrl);
                        }
                        //??????  ??????(?????????0 ,1?????????,2?????????,???????????????)
                        if (userInfo != null){
                            int gender = userInfo.getGender();
                            itemSex.getTvContent().setText(gender == 0? "?????????":gender == 1?"???":"???");
                        }
                        //??????
                        if (userInfo != null && !TextUtils.isEmpty(userInfo.getSignature()) ){
                            itemQianming.getTvContent().setText(userInfo.getSignature());
                        }
                        if (!TextUtils.isEmpty(userInfo.getUserId())){
                            itemLiaoxinhao.getTvContent().setText(userInfo.getUserId());
                        }
                    }
                });
            }

            @Override
            public void onError(int error, String errorMsg) {
                EMLog.e(TAG,"fetchUserInfoByIds error:" + error + " errorMsg:" + errorMsg);
            }
        });

    }

    protected void addLiveDataObserver() {
        LiveDataBus.get().with(DemoConstant.AVATAR_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                ImageLoad.into(mContext,event.message,0.5f,headImageView);
            }
        });
        LiveDataBus.get().with(DemoConstant.NICK_NAME_CHANGE, EaseEvent.class).observe(this, event -> {
            if (event != null) {
                nickName = event.message;
                itemNickname.getTvContent().setText(nickName);
            }
        });
    }

    /**
     * ??????????????????
     * */
    @Override
    public void onCamera() {
        takePhoto();
    }

    /**
     * ?????????????????????
     * */
    @Override
    public void onPhoto() {
        takePicture();
    }


    /**
     * ??????????????????
     */
    private void takePhoto() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 200;
            String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
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
                openCamera();
            }
        }else {
            openCamera();
        }
    }

    /**
     * ????????????
     */
    private void takePicture() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 100;
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                choosePhoto();
            }
        }else {
            choosePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        //requestCode??????requestPermissions()??????????????????
        //permission??????requestPermissions()??????????????????
        //grantResults????????????0???????????????-1????????????
        if (requestCode == 100){
            boolean isPermissions = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1){
                    isPermissions = false;
                }
            }
            if (isPermissions){
                choosePhoto();
            }else {
                ToastUtils.showFailToast("???????????????");
            }
        }
        if (requestCode == 200){
            boolean isPermissions = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1){
                    isPermissions = false;
                }
            }
            if (isPermissions){
                openCamera();
            }else {
                ToastUtils.showFailToast("???????????????");
            }
        }
    }

    /**
     * ?????????
     * */
    private void choosePhoto(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, 3);
    }

    private Uri mImageUri;

    // ??????????????????????????????
    private void openCamera() {
        //????????????????????????
        mFilePath = Environment.getExternalStorageDirectory() + File.separator + "demo" + File.separator;

        //??????????????????????????????????????????
        File cameraFolder = new File(mFilePath);
        if (!cameraFolder.exists()) {
            cameraFolder.mkdirs();
        }

        //?????????????????????????????????
        mFilePath = mFilePath + System.currentTimeMillis() +".jpg";

        File mOutImage = new File(mFilePath);

        //?????????7.0?????? ????????????uir??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageUri = FileProvider.getUriForFile(mContext, "com.hyphenate.liaoxin.fileProvider", mOutImage);
        } else {
            //????????????????????????????????????
            mImageUri = Uri.fromFile(mOutImage);
        }
        //????????????
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, 4);

    }

}
