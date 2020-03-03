package com.hezhihu89.adapter;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.AppBarLayout;

/**
 * @author hezhihu89
 * 创建时间 2020 年 02 月 26 日 15:42
 * 功能描述:
 */
class MyAppBarLayout extends AppBarLayout {
    public MyAppBarLayout(@NonNull Context context) {
        super(context);
    }

    public MyAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyAppBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
