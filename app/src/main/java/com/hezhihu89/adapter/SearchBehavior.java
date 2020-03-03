package com.hezhihu89.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DebugUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author hezhihu89
 * 创建时间 2020 年 02 月 25 日 11:35
 * 功能描述:
 */
class SearchBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = SearchBehavior.class.getSimpleName() + ": HEZHIHU89";

    private float mTotalHeight = 0;

    /**
     * Default constructor for instantiating Behaviors.
     */
    public SearchBehavior() {
        Log.e(TAG,"创建");
    }

    /**
     * Default constructor for inflating Behaviors from layout. The Behavior will have
     * the opportunity to parse specially defined layout parameters. These parameters will
     * appear on the child view tag.
     *
     * @param context
     * @param attrs
     */
    public SearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG,"创建");
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        RecyclerView recyclerView = (RecyclerView) dependency;
        float currentY = recyclerView.getY();
        if(mTotalHeight == 0){
            mTotalHeight = currentY;
        }

        float precent = currentY/mTotalHeight;
//        child.setAlpha(precent);

        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,
                               int type, @NonNull int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        Log.e(TAG,"child:" + child +"   target:" + target + "   dxConsumed:" + dxConsumed + "   dxUnconsumed: " + dyConsumed);
    }
}
