package com.hyphenate.liaoxin.section.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.MyContacts;
import com.hyphenate.liaoxin.common.net.callback.ResultCallBack;
import com.hyphenate.liaoxin.common.net.client.HttpURL;
import com.hyphenate.liaoxin.common.net.client.HttpUtils;
import com.hyphenate.liaoxin.common.net.request.SystemBankRequst;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.me.adapter.SelBankDecoration;
import com.hyphenate.liaoxin.section.me.adapter.SelectBankAdapter;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;


/**
 * 选择银行卡
 * */
public class SelectBankActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "SelectBankActivity";

    private EaseTitleBar titleBar;
    private RecyclerView recycler;
    private SelectBankAdapter adapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar);
        recycler = findViewById(R.id.recycler);

    }


    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
//        recycler.addItemDecoration(new SelBankDecoration(mContext));
        adapter = new SelectBankAdapter(mContext);
        adapter.setItemOnClick(new SelectBankAdapter.itemOnClick() {
            @Override
            public void onClick(SystemBankRequst.SystemBank bank) {
                Intent intent = new Intent();
                intent.putExtra("bank",bank);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        super.initData();
        getList();
    }


    private void getList(){
        showLoading();
        HttpUtils.getInstance().post(mContext, HttpURL.GET_SYSTEM_BANK, "", new ResultCallBack() {
            @Override
            public void onSuccessResponse(Call call, String str) {
                dismissLoading();
                try {

                    SystemBankRequst requst = new Gson().fromJson(str,SystemBankRequst.class);

                    if (requst != null){
                        conList(requst.data);
                    }

                }catch (Exception e){
                    ToastUtils.showToast("解析失败");
                }
            }

            @Override
            public void onFailure(Call call, IOException e, String str) {
                dismissLoading();
                super.onFailure(call, e, str);
            }
        });

    }

    private void conList(List<SystemBankRequst.SystemBank> data){
        if (data != null){
            for (SystemBankRequst.SystemBank systemBank : data) {
                systemBank.group = getPingYin(systemBank.name);
            }

            Collections.sort(data, new PinyinComparator());
            adapter.setList(data);
        }
    }



    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    private class PinyinComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return sort((SystemBankRequst.SystemBank)o1,(SystemBankRequst.SystemBank)o2);
        }

        private int sort(SystemBankRequst.SystemBank lhs, SystemBankRequst.SystemBank rhs) {
            // 获取ascii值
            int lhs_ascii = lhs.group.toUpperCase().charAt(0);
            int rhs_ascii = rhs.group.toUpperCase().charAt(0);
            // 判断若不是字母，则排在字母之后
            if (lhs_ascii < 65 || lhs_ascii > 90)
                return 1;
            else if (rhs_ascii < 65 || rhs_ascii > 90)
                return -1;
            else
                return lhs.group.compareTo(rhs.group);
        }
    }


    public static String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z_]+$");
        Matcher matcher = p.matcher(inputString.substring(0, 1));
        if (matcher.find()) {
            char[] input = inputString.trim().toCharArray();
            String output = "";
            try {
                for (int i = 0; i < input.length; i++) {
                    if (Character.toString(input[i]).matches(
                            "[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                                input[i], format);
                        output += temp[0];
                    } else
                        output += Character.toString(input[i]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
            return output.substring(0,1).toUpperCase();
        } else {
            return "#";
        }
    }
}
