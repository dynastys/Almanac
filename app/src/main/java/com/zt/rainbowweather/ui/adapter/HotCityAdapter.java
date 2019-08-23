package com.zt.rainbowweather.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zt.weather.R;
import com.zt.rainbowweather.entity.City;

import java.util.List;


/**
 * zw
 */

public class HotCityAdapter extends BaseQuickAdapter<City, BaseViewHolder> {
    public HotCityAdapter(@Nullable List<City> data) {
        super(R.layout.item_hot_city, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, City item) {
        TextView tvName = helper.getView(R.id.tv_name);
        if (item.isLocate.equals("1")) {
            if(TextUtils.isEmpty(item.name)){
                tvName.setText("点击重新定位");
                tvName.setTextColor(mContext.getResources().getColor(R.color.selector));//selector
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_locate);
                tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }else{
                tvName.setText("定位");
                tvName.setTextColor(Color.parseColor("#2397f0"));
                Drawable drawable = mContext.getResources().getDrawable(R.mipmap.icon_locate);
                tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            }
        } else {
            tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            tvName.setText(item.name);
        }
        if (item.isChecked.equals("1")) {
            tvName.setTextColor(Color.parseColor("#2397f0"));
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_black));
        }
    }
}
