package com.hezhihu89.adapter.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.hezhihu89.adapter.offset.IHeaderOffsetImpl;

/**
 * @author hezhihu89
 * 创建时间 2020 年 02 月 26 日 18:10
 * 功能描述:
 */
public class Toolbar extends androidx.appcompat.widget.Toolbar implements IHeaderOffsetImpl {
    private static final String TAG = Toolbar.class.getSimpleName() + ": HEZHIHU89";

    public Toolbar(Context context) {
        super(context);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Toolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param offset 偏移
     */
    @Override
    public void offset(int offset) {
        Log.d(TAG,"偏移量：" + offset);
        ViewCompat.offsetTopAndBottom(this, offset);
    }
}
