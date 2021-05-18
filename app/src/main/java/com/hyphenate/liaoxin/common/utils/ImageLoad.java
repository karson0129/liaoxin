package com.hyphenate.liaoxin.common.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.liaoxin.R;
import com.hyphenate.liaoxin.common.net.client.HttpURL;

/**
 * 图片加载
 */
public class ImageLoad {


    /***
     *  加载圆弧
     * @param context
     * @param url
     * @param roundingRadius
     * @param imageView
     */
    public static void intoRoundedCorners(Context context, String url, int roundingRadius, ImageView imageView){
        Glide.with(context).load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(roundingRadius))).into(imageView);//四周都是圆角的圆角矩形图片。
    }

    /**
     * 加载原型
     * @param context
     * @param url
     * @param imageView
     */
    public static void intoCircleCro(Context context, String url,ImageView imageView){
        Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);//标准圆形图片。
    }

    public static void into(Context context,String url,float sizeMultiplier,ImageView imageView){

//        Glide.with(context).load(url).placeholder(R.drawable.em_login_logo).into(imageView);

        Glide.with(context)
                //放弃图片质量提高加载速度
                .asBitmap()
                .format(DecodeFormat.PREFER_RGB_565)
                .load(url)
                .placeholder(R.drawable.em_login_logo)
//                .error(R.drawable.placeholder)
//                .fallback(R.drawable.placeholder)
                // 取消动画，防止第一次加载不出来
                .dontAnimate()
                //加载缩略图
                .thumbnail(sizeMultiplier)
                //指定加载大小
                .override(imageView.getWidth(), imageView.getHeight())
                .into(imageView);
    }

}
