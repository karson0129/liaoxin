package com.hyphenate.liaoxin.section.me.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.liaoxin.R;

public class SelBankDecoration extends RecyclerView.ItemDecoration {

    private int groupHeaderHeight;
    private Paint headPaint;

    private Paint textPaint;

    private Rect textRect;

    public SelBankDecoration(Context context) {
//        groupHeaderHeight = dp2px(context,100);
        groupHeaderHeight = (int) EaseCommonUtils.dip2px(context, 38);
        headPaint = new Paint();
        headPaint.setColor(context.getResources().getColor(R.color.color_F6f6f6));

        textPaint = new Paint();
        textPaint.setColor(context.getResources().getColor(R.color.color_AAAAAA));
        textPaint.setTextSize(EaseCommonUtils.dip2px(context, 13));
        textPaint.setAntiAlias(true);//设置抗锯齿

        textRect = new Rect();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() instanceof SelectBankAdapter){
            SelectBankAdapter adapter = (SelectBankAdapter) parent.getAdapter();
            //当前屏幕的item个数
            int count = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < count; i++) {
                //获取对应的View
                View view = parent.getChildAt(i);
                //获取VIew的布局位置
                int position = parent.getChildLayoutPosition(view);
                //是否 头部
                boolean isGroupHeader = adapter.isGroupHeader(position);
                if (view.getTop() - groupHeaderHeight - parent.getPaddingTop() >= 0){
                    if (isGroupHeader){
                        c.drawRect(left,view.getTop() - groupHeaderHeight,right,view.getTop(),headPaint);
                        String groupName = adapter.getGroupName(position);
                        textPaint.getTextBounds(groupName,0,groupName.length(),textRect);
                        c.drawText(groupName,left + 20,view.getTop() - groupHeaderHeight/2 + textRect.height() /2,
                                textPaint);
                    }else {
                        c.drawRect(left,view.getTop() - 1,right,view.getTop(),headPaint);
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (parent.getAdapter() instanceof SelectBankAdapter){
            SelectBankAdapter adapter = (SelectBankAdapter) parent.getAdapter();
            //返回可见区域内的第一个item的position
            int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            //获取对应position的view
            View itemView = parent.findViewHolderForAdapterPosition(position).itemView;
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = parent.getPaddingTop();
            //当第二个是组的头部的时候
            boolean isGroupHeader = adapter.isGroupHeader(position + 1);
            if (isGroupHeader) {
                int bottom = Math.min(groupHeaderHeight, itemView.getBottom() - parent.getPaddingTop());
                c.drawRect(left, top, right, top + bottom, headPaint);
                String groupName = adapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                c.drawText(groupName, left + 20, top + bottom - groupHeaderHeight / 2 + textRect.height() / 2, textPaint);
            } else {
                c.drawRect(left, top, right, top + groupHeaderHeight, headPaint);
                String groupName = adapter.getGroupName(position);
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                c.drawText(groupName, left + 20, top + groupHeaderHeight / 2 + textRect.height() / 2, textPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getAdapter() instanceof SelectBankAdapter){
            SelectBankAdapter adapter = (SelectBankAdapter) parent.getAdapter();
            int position = parent.getChildAdapterPosition(view);
            boolean isGroupHeader = adapter.isGroupHeader(position);

            if (isGroupHeader){
                outRect.set(0,groupHeaderHeight,0,0);
            }else {
                outRect.set(0,1,0,0);
            }
        }
    }

    private int dp2px(Context context, int i) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (i * scale * 0.5f);
    }

}
