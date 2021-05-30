package com.hyphenate.liaoxin.section.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.request.SystemBankRequst;

import java.util.List;

public class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.ItemViewHolder> {

    private Context context;
    public List<SystemBankRequst.SystemBank> list;

    public SelectBankAdapter(Context context) {
        this.context = context;
    }

    public List<SystemBankRequst.SystemBank> getList() {
        return list;
    }

    public void setList(List<SystemBankRequst.SystemBank> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private itemOnClick itemOnClick;

    public void setItemOnClick(SelectBankAdapter.itemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
        notifyDataSetChanged();
    }

    public interface itemOnClick{
        void onClick(SystemBankRequst.SystemBank bank);

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
        return list.get(position).group;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sel_bank, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).name);

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemOnClick != null){
                    itemOnClick.onClick(list.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null? 0:list.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout contact;
        public ImageView ivAvatar;
        public TextView tvName;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            contact = itemView.findViewById(R.id.contact);
        }
    }


}
