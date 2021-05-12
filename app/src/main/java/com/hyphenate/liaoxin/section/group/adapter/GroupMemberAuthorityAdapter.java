package com.hyphenate.liaoxin.section.group.adapter;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.section.contact.adapter.ContactListAdapter;

public class GroupMemberAuthorityAdapter extends ContactListAdapter {

    @Override
    public int getEmptyLayoutId() {
        return R.layout.ease_layout_default_no_data;
    }
}
