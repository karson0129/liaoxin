package com.hyphenate.liaoxin.section.me.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.AuthBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.PicRequest;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.PictureUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.PictureSelectDialog;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;

/**
 * 认证
 * */
public class CertificationActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, TextWatcher, View.OnClickListener, PictureSelectDialog.onPictureSelectListener {

    private String TAG = "CertificationActivity";

    private EaseTitleBar titleBar;
    private EditText etName;
    private EditText etCode;
    private ImageView ivZhengmian;
    private ImageView ivFanmian;
    private TextView action;

    private Drawable clear;

    private String mName;
    private String mCode;
    private String mZheng;
    private String mFan;

    private int type = 0;//1 是正面  2 反面
    private PictureSelectDialog dialog;
    private Uri mImageUri;
    private String mFilePath;

    public static void actionStart(Context context) {
        Intent starter = new Intent(context, CertificationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        etName = findViewById(R.id.et_name);
        etCode = findViewById(R.id.et_code);
        ivZhengmian = findViewById(R.id.iv_zhengmian);
        ivFanmian = findViewById(R.id.iv_fanmian);
        action = findViewById(R.id.action);
        dialog = new PictureSelectDialog(this);

    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        etName.addTextChangedListener(this);
        etCode.addTextChangedListener(this);
        ivFanmian.setOnClickListener(this);
        ivZhengmian.setOnClickListener(this);
        action.setOnClickListener(this);
        dialog.setOnPictureSelectListener(this);
        EaseEditTextUtils.clearEditTextListener(etName);
        EaseEditTextUtils.clearEditTextListener(etCode);
    }

    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);
    }

    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mName = etName.getText().toString().trim();
        mCode = etCode.getText().toString().trim();
        EaseEditTextUtils.showRightDrawable(etName, clear);
        EaseEditTextUtils.showRightDrawable(etCode, clear);

        change();
    }

    private void change(){
        if (contsol(false)){
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            action.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            action.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            action.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    private boolean contsol(boolean isShow){
        if (TextUtils.isEmpty(mName)){
            if (isShow)
            ToastUtils.showFailToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(mCode)){
            if (isShow)
            ToastUtils.showFailToast("请输入身份证号");
            return false;
        }
        if (TextUtils.isEmpty(mZheng)){
            if (isShow)
            ToastUtils.showFailToast("请上传身份证正面");
            return false;
        }
        if (TextUtils.isEmpty(mFan)){
            if (isShow)
            ToastUtils.showFailToast("请上传身份证反面");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_zhengmian://正面
                type = 1;
                //空判断 如果Dialog不为show才show
                if (dialog!= null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.iv_fanmian://反面
                type = 2;
                //空判断 如果Dialog不为show才show
                if (dialog!= null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.action://提交
                if (contsol(true)){
                    update();
                }
                break;
        }
    }

    /**
     * 上传
     * */
    private void update(){
        showLoading();
        AuthBean bean = new AuthBean();
        bean.realName = mName;
        bean.uniqueNo = mCode;
        bean.frontCover = mZheng;
        bean.backCover = mFan;
        Log.i(TAG,"参数："+new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.REAL_NAME_AUTH, new Gson().toJson(bean), new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG,"str :"+str);
                CertificationSuccessActivity.actionStart(mContext);
                finish();
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 3 && resultCode == RESULT_OK)){
            controlPhoto(data);
        }else if ((requestCode == 4 && resultCode == RESULT_OK)){
            if (!TextUtils.isEmpty(mFilePath)){
                controlPhotoPath(mFilePath);
            }
        }
    }

    /**
     * 显示 相册 图片
     * */
    private void controlPhotoPath(String data) {
        if (data == null) {
            return;
        }
        Log.i(TAG, "相册数据：" + data);
        showLoading();
        HttpUtils.getInstance().uploadImage(mContext, HttpURL.UPLOAD_IMAGE, data, new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG, "成功：" + str);
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    if (type == 1){
                        mZheng = request.id;
                        ImageLoad.into(mContext,data,0.5f,ivZhengmian);
                    }else if (type == 2){
                        mFan = request.id;
                        ImageLoad.into(mContext,data,0.5f,ivFanmian);
                    }
                }
                change();
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call,e,str);
                Log.i(TAG, "失败");
            }
        });
    }

    /**
     * 显示 相册 图片
     * */
    private void controlPhoto(Intent data) {
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        String filePath = PictureUtils.getRealFilePath(mContext, uri);
        Log.i(TAG, "相册数据：" + filePath);
        showLoading();
        HttpUtils.getInstance().uploadImage(mContext, HttpURL.UPLOAD_IMAGE, filePath, new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG, "成功：" + str);
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    if (type == 1){
                        mZheng = request.id;
                        ImageLoad.into(mContext,filePath,0.5f,ivZhengmian);
                    }else if (type == 2){
                        mFan = request.id;
                        ImageLoad.into(mContext,filePath,0.5f,ivFanmian);
                    }
                }
                change();
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call,e,str);
                Log.i(TAG, "失败");
            }
        });
    }

    /**
     * （接口）拍照
     * */
    @Override
    public void onCamera() {
        takePhoto();
    }

    /**
     * （接口）去相册
     * */
    @Override
    public void onPhoto() {
        takePicture();
    }

    /**
     * 拍摄证件照片
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
     * 相册照片
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
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
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
                ToastUtils.showFailToast("未给予权限");
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
                ToastUtils.showFailToast("未给予权限");
            }
        }
    }


    /**
     * 去相册
     * */
    private void choosePhoto(){
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, 3);
    }



    // 拍照后存储并显示图片
    private void openCamera() {
        //获得项目缓存路径
        mFilePath = Environment.getExternalStorageDirectory() + File.separator + "demo" + File.separator;

        //如果目录不存在则必须创建目录
        File cameraFolder = new File(mFilePath);
        if (!cameraFolder.exists()) {
            cameraFolder.mkdirs();
        }

        //根据时间随机生成图片名
        mFilePath = mFilePath + System.currentTimeMillis() +".jpg";

        File mOutImage = new File(mFilePath);

        //如果是7.0以上 那么就把uir包装
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageUri = FileProvider.getUriForFile(mContext, "com.hyphenate.liaoxin.fileProvider", mOutImage);
        } else {
            //否则就用老系统的默认模式
            mImageUri = Uri.fromFile(mOutImage);
        }
        //启动相机
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, 4);

    }
}
