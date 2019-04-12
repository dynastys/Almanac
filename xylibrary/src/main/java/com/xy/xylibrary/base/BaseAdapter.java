package com.xy.xylibrary.base;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xy.xylibrary.Interface.BaseAdapterListener;

import java.util.List;

/**
* 适配器基类
* */
public class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    private BaseAdapterListener<T> baseAdapterListener;

    public BaseAdapter(int layout, List<T> lists, BaseAdapterListener<T> baseAdapterListener) {
        super(layout, lists);
        this.baseAdapterListener = baseAdapterListener;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, T item) {
        baseAdapterListener.convertView(viewHolder,item);
    }

}