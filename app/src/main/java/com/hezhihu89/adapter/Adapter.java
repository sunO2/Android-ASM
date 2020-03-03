package com.hezhihu89.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

/**
 * @author hezhihu89
 * 创建时间 2020 年 02 月 25 日 10:31
 * 功能描述:
 */
class Adapter extends BaseQuickAdapter<Bean, BaseViewHolder> {
    public Adapter(List<Bean> data) {
        super(R.layout.item_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Bean bean) {
        baseViewHolder.setText(R.id.text,"item"+ baseViewHolder.getAdapterPosition());
    }
}
