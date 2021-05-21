package com.hyphenate.liaoxin.section.addfriend.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.bean.MyContacts;

import java.util.ArrayList;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.ItemViewHolder>{

    private ArrayList<MyContacts> list;

    private Context context;

    private onItemClickLin onItemClickLin;

    private interface onItemClickLin{

    }

    public AddressBookAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<MyContacts> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickLin(AddressBookAdapter.onItemClickLin onItemClickLin) {
        this.onItemClickLin = onItemClickLin;
        notifyDataSetChanged();
    }

    /**
     * 判断 是不是 组的第一个item
     * @param position
     * @return
     */
    public boolean isGroupHeader(int position){
        if (position == 0){
            return true;
        }else {
            String currentGroupName = getGroupName(position);
            String preGroupName = getGroupName(position - 1);
            if (preGroupName.equals(currentGroupName)){
                return false;
            }else {
                return true;
            }
        }
    }

    public String getGroupName(int position){
        return list.get(position).getGroup();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.demo_item_search_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (list.get(position) != null){
            if (!TextUtils.isEmpty(list.get(position).name)){
                holder.tv_search_name.setText(list.get(position).name);
            }
            if (!TextUtils.isEmpty(list.get(position).phone)){
                holder.tv_search_user_id.setText(list.get(position).phone);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout constraintLayout;
        private EaseImageView imageView;
        private TextView tv_search_name;
        private TextView tv_search_user_id;
        private Button btn_search_add;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.list_itease_layout);
            imageView = itemView.findViewById(R.id.iv_search_user_icon);
            tv_search_name = itemView.findViewById(R.id.tv_search_name);
            tv_search_user_id = itemView.findViewById(R.id.tv_search_user_id);
            btn_search_add = itemView.findViewById(R.id.btn_search_add);

        }
    }

}
