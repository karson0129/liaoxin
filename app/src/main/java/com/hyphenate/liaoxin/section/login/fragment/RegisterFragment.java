package com.hyphenate.liaoxin.section.login.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMUserInfo;
import com.hyphenate.easeui.model.EaseEvent;
import com.hyphenate.easeui.utils.EaseEditTextUtils;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.constant.DemoConstant;
import com.hyphenate.liaoxin.common.constant.UserConstant;
import com.hyphenate.liaoxin.common.db.PrefUtils;
import com.hyphenate.liaoxin.common.livedatas.LiveDataBus;
import com.hyphenate.liaoxin.common.net.bean.RegisterBean;
import com.hyphenate.liaoxin.common.net.bean.SendCodeBean;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.BaseRequest;
import com.hyphenate.liaoxin.common.net.request.PicRequest;
import com.hyphenate.liaoxin.common.utils.ImageLoad;
import com.hyphenate.liaoxin.common.utils.PictureUtils;
import com.hyphenate.liaoxin.common.utils.PreferenceManager;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.PictureSelectDialog;
import com.hyphenate.liaoxin.section.base.BaseInitFragment;
import com.hyphenate.liaoxin.section.login.activity.VerificationActivity;
import com.hyphenate.util.EMLog;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;


/***
 * ??????
 *
 */
public class RegisterFragment extends BaseInitFragment implements EaseTitleBar.OnBackPressListener, View.OnClickListener, TextWatcher, PictureSelectDialog.onPictureSelectListener {

    private String TAG = "RegisterFragment";

    private EaseTitleBar mToolbarServer;
    private ImageView ivHeader;
    private TextView tvHeader;
    private EditText etNickname;
    private EditText etLoginPhone;
    private EditText etVerificationCode;
    private EditText etLoginPassword;
    private TextView btnAction;
    private TextView tvVerCode;

    private PictureSelectDialog dialog;
    private Drawable clear;

    private String mFilePath;
    private String mIcon;
    private String mNickName;
    private String mPhone;
    private String mVerCode;
    private String mPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mToolbarServer = findViewById(R.id.toolbar_server);
        ivHeader = findViewById(R.id.iv_header);
        tvHeader = findViewById(R.id.tv_header);
        etNickname = findViewById(R.id.et_nickname);
        etLoginPhone = findViewById(R.id.et_login_phone);
        etVerificationCode = findViewById(R.id.et_verification_code);
        etLoginPassword = findViewById(R.id.et_login_password);
        btnAction = findViewById(R.id.btn_action);
        tvVerCode = findViewById(R.id.tv_ver_code);
        dialog = new PictureSelectDialog(mContext);
        dialog.setOnPictureSelectListener(this);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolbarServer.setOnBackPressListener(this);
        ivHeader.setOnClickListener(this);
        tvHeader.setOnClickListener(this);
        etNickname.addTextChangedListener(this);
        etLoginPhone.addTextChangedListener(this);
        etVerificationCode.addTextChangedListener(this);
        etLoginPassword.addTextChangedListener(this);
        btnAction.setOnClickListener(this);
        tvVerCode.setOnClickListener(this);

        EaseEditTextUtils.clearEditTextListener(etNickname);
        EaseEditTextUtils.clearEditTextListener(etLoginPhone);
        EaseEditTextUtils.clearEditTextListener(etVerificationCode);
        EaseEditTextUtils.clearEditTextListener(etLoginPassword);
    }

    @Override
    protected void initData() {
        super.initData();
        clear = getResources().getDrawable(R.drawable.d_clear);
        EaseEditTextUtils.showRightDrawable(etNickname, clear);
        EaseEditTextUtils.showRightDrawable(etLoginPhone, clear);
        EaseEditTextUtils.showRightDrawable(etVerificationCode, clear);
        EaseEditTextUtils.showRightDrawable(etLoginPassword, clear);
    }


    @Override
    public void onBackPress(View view) {
        onBackPress();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_header://??????
            case R.id.tv_header:
                if (dialog!= null && !dialog.isShowing()){
                    dialog.show();
                }
                break;
            case R.id.tv_ver_code://???????????????
                if (!TextUtils.isEmpty(mPhone)){
                    getVerificationCode();
                }
                break;
            case R.id.btn_action://??????
                if (isLogin(true)){
                    getRegister();
                }
                break;
        }
    }

    private boolean isLogin(boolean isShow){
        if (TextUtils.isEmpty(mIcon)){
            if (isShow)
            ToastUtils.showFailToast("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(mNickName)){
            if (isShow)
            ToastUtils.showFailToast("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(mPhone)){
            if (isShow)
            ToastUtils.showFailToast("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(mVerCode)){
            if (isShow)
                ToastUtils.showFailToast("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(mPassword)){
            if (isShow)
                ToastUtils.showFailToast("???????????????");
            return false;
        }
        return true;
    }

    /**
     * ??????
     */
    private void getRegister(){
        showLoading();
        RegisterBean request = new RegisterBean();
        request.telephone = mPhone;
        request.nickName = mNickName;
        request.code = mVerCode;
        request.password = mPassword;
        request.cover = mIcon;
        Log.i(TAG,"?????????"+ new Gson().toJson(request));
        HttpUtils.getInstance().post(mContext,HttpURL.RESGIER_CLIENT, new Gson().toJson(request), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"?????????"+ str);
                try {
                    BaseRequest<String> request = new Gson().fromJson(str,BaseRequest.class);
                    if (!TextUtils.isEmpty(request.data)){
                        ToastUtils.showSuccessToast("????????????");
                        onBackPress();
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call, e,str);
                Log.d(TAG,"?????????");
            }
        });
    }


    /**
     * ???????????????
     */
    private void getVerificationCode(){
        showLoading();
        SendCodeBean bean = new SendCodeBean();
        bean.telephone = mPhone;
        bean.type = SendCodeBean.SendCodeType.Registered;
        Log.i(TAG,"?????????"+ new Gson().toJson(bean));
        HttpUtils.getInstance().post(mContext, HttpURL.SEND_CODE, new Gson().toJson(bean), new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.d(TAG,"?????????"+ str);
                ToastUtils.showSuccessToast("????????????");
                if (timer != null){
                    timer.start();
                }
            }

            @Override
            public void onFailure(Call call, IOException e,String str) {
                dismissLoading();
                super.onFailure(call, e,str);
                Log.d(TAG,"?????????"+ e.toString());
                ToastUtils.showFailToast("????????????");
            }
        });
    }


    /***
     * =============================================================================================
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mNickName = etNickname.getText().toString().trim();
        mPhone = etLoginPhone.getText().toString().trim();
        mVerCode = etVerificationCode.getText().toString().trim();
        mPassword = etLoginPassword.getText().toString().trim();

        EaseEditTextUtils.showRightDrawable(etNickname, clear);
        EaseEditTextUtils.showRightDrawable(etLoginPhone, clear);
        EaseEditTextUtils.showRightDrawable(etVerificationCode, clear);
        EaseEditTextUtils.showRightDrawable(etLoginPassword, clear);

        btnContoul();
    }
    /***
     * =============================================================================================
     */

    private void btnContoul(){
        if (isLogin(false)){
            btnAction.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_0189ff));
            btnAction.setTextColor(mContext.getResources().getColor(R.color.white));
        }else {
            btnAction.setBackground(mContext.getResources().getDrawable(R.drawable.shape_ride_8_f2f2f2));
            btnAction.setTextColor(mContext.getResources().getColor(R.color.color_AAAAAA));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }
    }


    /**
     *  ?????????
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            tvVerCode.setEnabled(false);
            tvVerCode.setText((millisUntilFinished / 1000) + "???????????????");
        }

        @Override
        public void onFinish() {
            tvVerCode.setEnabled(true);
            tvVerCode.setText("???????????????");
        }
    };

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
                if (mContext.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
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
                if (mContext.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 3 && resultCode == Activity.RESULT_OK)){
            controlPhoto(data);
        }else if ((requestCode == 4 && resultCode == Activity.RESULT_OK)){
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
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    mIcon = HttpURL.PICTURE_URL + request.id;
                    ImageLoad.into(mContext,HttpURL.PICTURE_URL + mIcon,0.5f,ivHeader);
                    btnContoul();
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
        showLoading();
        HttpUtils.getInstance().uploadImage(mContext, HttpURL.UPLOAD_IMAGE, filePath, new ResultCallBack() {

            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                Log.i(TAG, "?????????" + str);
                PicRequest request = new Gson().fromJson(str,PicRequest.class);
                if (request != null && !TextUtils.isEmpty(request.id)){
                    mIcon = HttpURL.PICTURE_URL + request.id;
                    ImageLoad.into(mContext,HttpURL.PICTURE_URL + mIcon,0.5f,ivHeader);
                    btnContoul();
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

}
