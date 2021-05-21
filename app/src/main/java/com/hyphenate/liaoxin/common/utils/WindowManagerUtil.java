package com.hyphenate.liaoxin.common.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 */
public class WindowManagerUtil {

    // private Context mContext;
    private static Activity act;
    private static DisplayMetrics metric = new DisplayMetrics();

    // public WindowManagerUtil(Context con)
    // {
    // mContext = con;
    // metric = new DisplayMetrics();
    // Activity act = (Activity)mContext;
    // act.getWindowManager().getDefaultDisplay().getMetrics(metric);
    // }

    /**
     * 获取屏幕宽度
     *
     * @return 返回宽度
     */
    public static int getScreenWidth(Context context) {
        act = (AppCompatActivity) context;
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        act = null;
        return metric.widthPixels; // 屏幕宽度（像素）
    }

    /**
     * 获取屏幕高度
     *
     * @return 返回高度
     */
    public static int getScreenHeight(Context context) {
        act = (AppCompatActivity) context;
        act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        act = null;
        return metric.heightPixels; // 屏幕高度（像素）
    }

    /**
     * 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
     *
     * @param child
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 获取当前项目版本号
     *
     * @param context
     * @return int versionCode
     */
    public static int getPackageVersionCode(Context context) {
        PackageManager packmanager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo info = packmanager.getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (NameNotFoundException e) {
            return versionCode;
        }
        return versionCode;
    }

    /**
     * 获取当前项目版本名称
     *
     * @param context
     * @return int versionCode
     */
    public static String getPackageVersionName(Context context) {
        PackageManager packmanager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo info = packmanager.getPackageInfo(
                    context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            return versionName;
        }
        return versionName;
    }

    /**
     * @param listView
     * @return
     * @throws
     * @Description:计算item高度一样的listview高度
     * @Title: CommonUtil
     * @author: Gift
     * @date 2014-3-5 上午11:11:28 备注
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        try {

            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);

        } catch (Exception e) {
            e.toString();
        }
    }

    // 遍历设置字体
    public static void changeViewSize(ViewGroup viewGroup, int screenWidth,
                                      int screenHeight) {// 传入Activity顶层Layout,屏幕宽,屏幕高
        int adjustFontSize = adjustFontSize(screenWidth, screenHeight);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof LinearLayout) {
                changeViewSize((LinearLayout) v, screenWidth, screenHeight);
            } else if (v instanceof RelativeLayout) {
                changeViewSize((RelativeLayout) v, screenWidth, screenHeight);
            } else if (v instanceof Button) {// 按钮加大这个一定要放在TextView上面，因为Button也继承了TextView
                ((Button) v).setTextSize(adjustFontSize + 2);
            } else if (v instanceof TextView) {
                // if (v.getId() == R.areaId.title_msg) {// 顶部标题
                // ((TextView) v).setTextSize(adjustFontSize + 4);
                // } else {
                // }
                ((TextView) v).setTextSize(adjustFontSize);
            }
        }
    }

    // 获取字体大小
    public static int adjustFontSize(int screenWidth, int screenHeight) {
        screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        /**
         * 1. 在视图的 onsizechanged里获取视图宽度，一般情况下默认宽度是320，所以计算一个缩放比率 rate = (float)
         * w/320 w是实际宽度 2.然后在设置字体尺寸时 paint.setTextSize((int)(8*rate));
         * 8是在分辨率宽为320 下需要设置的字体大小 实际字体大小 = 默认字体大小 x rate
         */
        int rate = (int) (6 * (float) screenWidth / 320); // 我自己测试这个倍数比较适合，当然你可以测试后再修改
        return rate < 15 ? 15 : rate; // 字体太小也不好看的
    }

    public static boolean isScreenLocked(Context c) {
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c
                .getSystemService(c.KEYGUARD_SERVICE);
        return !mKeyguardManager.inKeyguardRestrictedInputMode();
    }

}
