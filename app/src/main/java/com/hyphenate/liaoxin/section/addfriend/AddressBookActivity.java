package com.hyphenate.liaoxin.section.addfriend;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.enums.SearchType;
import com.hyphenate.liaoxin.common.net.bean.MyContacts;
import com.hyphenate.liaoxin.common.utils.ContactUtils;
import com.hyphenate.liaoxin.common.utils.ToastUtils;
import com.hyphenate.liaoxin.section.addfriend.adapter.AddressBookAdapter;
import com.hyphenate.liaoxin.section.addfriend.adapter.StarDecoration;
import com.hyphenate.liaoxin.section.base.BaseInitActivity;
import com.hyphenate.liaoxin.section.contact.activity.AddContactActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AddressBookActivity extends BaseInitActivity implements EaseTitleBar.OnBackPressListener {

    private String TAG = "AddressBookActivity";

    private EaseTitleBar titleBar;
    private EditText etQuery;
    private RecyclerView recyclerView;
    private AddressBookAdapter adapter;
    private ArrayList<MyContacts> contacts;
    private String mSearch;

    public static void startAction(Context context) {
        Intent intent = new Intent(context, AddressBookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_book;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        titleBar = findViewById(R.id.title_bar_main);
        etQuery = findViewById(R.id.query);
        recyclerView = findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new StarDecoration(mContext));
        adapter = new AddressBookAdapter(mContext);
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void initListener() {
        super.initListener();
        titleBar.setOnBackPressListener(this);
        etQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearch = etQuery.getText().toString().trim();
                if (TextUtils.isEmpty(mSearch)){
                    adapter.setList(contacts);
                }
            }
        });

        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    hideKeyboard();
                    mSearch = etQuery.getText().toString().trim();
                    if (!TextUtils.isEmpty(mSearch) && contacts != null){
                        adapter.setList(getSearchList(mSearch));
                    }else {
                        adapter.setList(contacts);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private ArrayList<MyContacts> getSearchList(String str){
        ArrayList<MyContacts> list = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            if ((!TextUtils.isEmpty(contacts.get(i).getName()) && contacts.get(i).getName().contains(str)) ||
                    (!TextUtils.isEmpty(contacts.get(i).getPhone()) && contacts.get(i).getPhone().contains(str)) ){
                list.add(contacts.get(i));
            }
        }
        return list;
    }

    @Override
    protected void initData() {
        super.initData();
        get();
    }

    private void get(){
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 200;
            String[] permissions = {
                    android.Manifest.permission.READ_CONTACTS,
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
                getAddressBook();
            }
        }else {
            getAddressBook();
        }
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
                getAddressBook();
            }else {
                ToastUtils.showFailToast("未给予权限");
            }
        }
    }

    private void getAddressBook(){
        contacts = ContactUtils.getAllContacts(mContext);
        Collections.sort(contacts, new PinyinComparator());
        adapter.setList(contacts);
    }


    @Override
    public void onBackPress(View view) {
        onBackPressed();
    }

    private class PinyinComparator implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            return sort((MyContacts)o1,(MyContacts)o2);
        }

        private int sort(MyContacts lhs, MyContacts rhs) {
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

}
