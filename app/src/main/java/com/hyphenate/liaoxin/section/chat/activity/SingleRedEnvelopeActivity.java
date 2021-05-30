package com.hyphenate.liaoxin.section.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.easeui.constants.EaseConstant;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.common.widget.CoinDialog;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleRedEnvelopeActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener, TextWatcher, View.OnClickListener {

    private String TAG = "SingleRedEnvelopeActivity";

    private EaseTitleBar titleBar;
    private EditText etJine;//金额
    private EditText etHint;//备注
    private TextView tvShow;
    private TextView tvAction;

    private String mJine;
    private String mHint = "恭喜发财，大吉大利";

    private CoinDialog coinDialog;

    private String oderClientId;

    public static void actionStart(Context context,String oderClientId) {
        Intent intent = new Intent(context, SingleRedEnvelopeActivity.class);
        intent.putExtra("oderClientId",oderClientId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_single_red_envelope;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar_main);
        etJine = findViewById(R.id.et_jine);
        etHint = findViewById(R.id.et_hint);
        tvShow = findViewById(R.id.tv_show);
        tvAction = findViewById(R.id.tv_action);
    }

    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        etJine.addTextChangedListener(this);
        etHint.addTextChangedListener(this);
        tvAction.setOnClickListener(this);
        InputFilter[] filters={new CashierInputFilter()};
        etJine.setFilters(filters); //设置金额输入的过滤器，保证只能输入金额类型
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mJine = etJine.getText().toString().trim();
        mHint = etHint.getText().toString().trim();
        if (TextUtils.isEmpty(mJine)){
            tvShow.setText("0.00");
        }else {
            tvShow.setText(""+mJine);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_action:
                if (!TextUtils.isEmpty(mJine) && Double.valueOf(mJine) > 0){
                    showCoinDialog();
                }else {
                    ToastUtils.showToast("请输入金额");
                }
                break;
        }
    }

    private void showCoinDialog(){
        if (coinDialog == null){
            coinDialog = new CoinDialog(mContext);
        }
        coinDialog.setmCoin(mJine);
        coinDialog.setOnClickLin(new CoinDialog.OnClickLin() {
            @Override
            public void onAction() {

            }
        });
        coinDialog.show();
    }

    /**
     * 过滤用户输入只能为金额格式
     */
    public class CashierInputFilter implements InputFilter {
        Pattern mPattern;

        //输入的最大金额
        private static final int MAX_VALUE = Integer.MAX_VALUE;
        //小数点后的位数
        private static final int POINTER_LENGTH = 2;

        private static final String POINTER = ".";

        private static final String ZERO = "0";

        public CashierInputFilter() {
            mPattern = Pattern.compile("([0-9]|\\.)*");
        }

        /**
         * @param source    新输入的字符串
         * @param start     新输入的字符串起始下标，一般为0
         * @param end       新输入的字符串终点下标，一般为source长度-1
         * @param dest      输入之前文本框内容
         * @param dstart    原内容起始坐标，一般为0
         * @param dend      原内容终点坐标，一般为dest长度-1
         * @return          输入内容
         */
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String sourceText = source.toString();
            String destText = dest.toString();

            //验证删除等按键
            if (TextUtils.isEmpty(sourceText)) {
                return "";
            }

            Matcher matcher = mPattern.matcher(source);
            //已经输入小数点的情况下，只能输入数字
            if(destText.contains(POINTER)) {
                if (!matcher.matches()) {
                    return "";
                } else {
                    if (POINTER.equals(source.toString())) {  //只能输入一个小数点
                        return "";
                    }
                }

                //验证小数点精度，保证小数点后只能输入两位
                int index = destText.indexOf(POINTER);
                int length = dend - index;

                if (length > POINTER_LENGTH) {
                    return dest.subSequence(dstart, dend);
                }
            } else {
                /**
                 * 没有输入小数点的情况下，只能输入小数点和数字
                 * 1. 首位不能输入小数点
                 * 2. 如果首位输入0，则接下来只能输入小数点了
                 */
                if (!matcher.matches()) {
                    return "";
                } else {
                    if ((POINTER.equals(source.toString())) && TextUtils.isEmpty(destText)) {  //首位不能输入小数点
                        return "";
                    } else if (!POINTER.equals(source.toString()) && ZERO.equals(destText)) { //如果首位输入0，接下来只能输入小数点
                        return "";
                    }
                }
            }

            //验证输入金额的大小
            double sumText = Double.parseDouble(destText + sourceText);
            if (sumText > MAX_VALUE) {
                return dest.subSequence(dstart, dend);
            }

            return dest.subSequence(dstart, dend) + sourceText;
        }
    }
}
