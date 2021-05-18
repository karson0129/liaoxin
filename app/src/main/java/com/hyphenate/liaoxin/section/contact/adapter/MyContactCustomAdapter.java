package com.hyphenate.liaoxin.section.contact.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.adapter.EaseBaseRecyclerViewAdapter;
import com.hyphenate.easeui.modules.contact.adapter.EaseContactCustomAdapter;
import com.hyphenate.easeui.modules.contact.model.EaseContactCustomBean;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.MyContactBean;

/***
 * 联系人header
 */
public class MyContactCustomAdapter extends EaseBaseRecyclerViewAdapter<MyContactBean> {

    private int mNewFriend = 0;

    @Override
    public ViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contact_header, parent, false);
        return new ItemViewHolder(view);
    }

    public void addItem(int id, int image, String name) {
        MyContactBean bean = new MyContactBean();
        bean.setId(id);
        bean.setResourceId(image);
        bean.setName(name);
        this.addData(bean);
    }

    public void setNewFriend(int mNewFriend) {
        this.mNewFriend = mNewFriend;
        notifyDataSetChanged();
    }

    private class ItemViewHolder extends ViewHolder<MyContactBean>{
        private ImageView mAvatar;
        private TextView mMsg;
        private TextView mName;
        private ConstraintLayout clUser;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void initView(View itemView) {
            mAvatar = findViewById(R.id.avatar);
            mMsg = findViewById(R.id.msg);
            mName = findViewById(R.id.name);
            clUser = findViewById(R.id.cl_user);
        }

        @Override
        public void setData(MyContactBean item, int position) {
            mName.setText(item.getName());
            if (mNewFriend > 0 && item.getName().equals("新的好友")){
                mMsg.setVisibility(View.VISIBLE);
                mMsg.setText(""+mNewFriend);
            }else {
                mMsg.setVisibility(View.GONE);
            }

            if(item.getResourceId() != 0) {
                mAvatar.setImageResource(item.getResourceId());
            }else if(TextUtils.isEmpty(item.getImage())) {
                Glide.with(itemView.getContext()).load(item.getImage()).into(mAvatar);
            }
        }
    }


}





