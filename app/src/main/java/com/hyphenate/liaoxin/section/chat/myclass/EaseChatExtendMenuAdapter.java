package com.hyphenate.liaoxin.section.chat.myclass;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.adapter.EaseBaseChatExtendMenuAdapter;
import com.hyphenate.easeui.interfaces.OnItemClickListener;
import com.hyphenate.easeui.modules.chat.EaseChatExtendMenu.ChatMenuItemModel;
import com.hyphenate.liaoxin.R;

public class EaseChatExtendMenuAdapter extends EaseBaseChatExtendMenuAdapter<EaseChatExtendMenuAdapter.ViewHolder, ChatMenuItemModel> {
    private OnItemClickListener itemListener;

    @Override
    protected int getItemLayoutId() {
        return R.layout.ease_my_chat_menu_item;
    }

    @Override
    protected EaseChatExtendMenuAdapter.ViewHolder easeCreateViewHolder(View view) {
        return new EaseChatExtendMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EaseChatExtendMenuAdapter.ViewHolder holder, int position) {
        ChatMenuItemModel item = mData.get(position);
        holder.imageView.setBackgroundResource(item.image);
        holder.textView.setText(item.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(item.clickListener != null){
                    item.clickListener.onChatExtendMenuItemClick(item.id, v);
                }
                if(itemListener != null) {
                    itemListener.onItemClick(v, position);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}


