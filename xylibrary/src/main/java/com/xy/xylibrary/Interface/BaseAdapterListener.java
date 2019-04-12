package com.xy.xylibrary.Interface;

import com.chad.library.adapter.base.BaseViewHolder;

public interface BaseAdapterListener<T> {

    void convertView(BaseViewHolder viewHolder, T item);


}
