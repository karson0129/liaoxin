package com.hyphenate.liaoxin.section.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.request.BankRequest;

import java.util.List;

public class BankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int item = 1;
    private int foot = 2;
    private int kong = 3;


    private Context context;
    private List<BankRequest.BankItem> list;

    private itemOnClick itemOnClick;

    public void setItemOnClick(BankAdapter.itemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
        notifyDataSetChanged();
    }

    public interface itemOnClick{

        void onItem();

        void onAdd();

    }

    public BankAdapter(Context context) {
        this.context = context;
    }

    public List<BankRequest.BankItem> getData() {
        return list;
    }

    public void setData(List<BankRequest.BankItem> data) {
        this.list = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == item){
            return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_item,null));
        }else if (viewType == foot){
            return new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_foot,null));
        }else if (viewType == kong){
            return new NullItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_kong,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){

            ((ItemViewHolder)holder).contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemOnClick != null){
                        itemOnClick.onItem();
                    }
                }
            });

        }else if (holder instanceof FootViewHolder){

            ((FootViewHolder)holder).linAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemOnClick != null){
                        itemOnClick.onAdd();
                    }
                }
            });

        }else if (holder instanceof NullItemViewHolder){

            ((NullItemViewHolder)holder).action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemOnClick != null){
                        itemOnClick.onAdd();
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return list == null? 1 :list.size() > 0? list.size() + 1:1;
    }

    @Override
    public int getItemViewType(int position) {
        if (list == null ||list.isEmpty()){
            return kong;
        }else {
            if (position == getItemCount() - 1){
                return foot;
            }else {
                return item;
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout contact;
        public ImageView ivBank;
        public TextView tvBank;
        public TextView tvType;
        public TextView tvQian;
        public TextView tvHou;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            contact = itemView.findViewById(R.id.contact);
            ivBank = itemView.findViewById(R.id.iv_bank);
            tvBank = itemView.findViewById(R.id.tv_bank);
            tvType = itemView.findViewById(R.id.tv_type);
            tvQian = itemView.findViewById(R.id.tv_qian);
            tvHou = itemView.findViewById(R.id.tv_hou);
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout linAdd;

        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            linAdd = itemView.findViewById(R.id.lin_add);
        }
    }


    public class NullItemViewHolder extends RecyclerView.ViewHolder{
        public TextView action;

        public NullItemViewHolder(@NonNull View itemView) {
            super(itemView);
            action = itemView.findViewById(R.id.action);
        }
    }

}
