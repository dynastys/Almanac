package com.zt.rainbowweather.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chenguang.weather.R;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.utils.WeatherUtils;

import java.util.List;


/**
 * Created by hcg on 2018/5/14.
 */

public class AddressAdapter extends BaseItemDraggableAdapter<AddressBean, BaseViewHolder> {
    public AddressAdapter(List<AddressBean> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddressBean item) {
        if (item.city.name.equals("定位失败")) {
            helper.setText(R.id.tv_address, item.city.name);
        } else {
            helper.setText(R.id.tv_temperature, String.format("%d℃", item.header.currTemp))
                    .setText(R.id.tv_address, item.city.name);
            if(item.header != null && item.header.weather != null && WeatherUtils.getWeatherStatus(item.header.weather.iconRes) != null){
                Glide.with(mContext)
                        .load(WeatherUtils.getWeatherStatus(item.header.weather.iconRes).iconRes)
                        .into((ImageView) helper.getView(R.id.iv_head));
            }

            final TextView tvAddress = helper.getView(R.id.tv_address);
            if (BasicApplication.getLocatedCity() != null && item.city.isLocate.equals("1")) {
                helper.setText(R.id.tv_temperature, String.format("%d℃", item.header.currTemp))
                        .setText(R.id.tv_address,BasicApplication.getLocatedCity().affiliation);
                helper.setVisible(R.id.iv, false);
                Glide.with(mContext)
                        .load(R.mipmap.icon_locate)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable
                                    Transition<? super Drawable> transition) {
                                tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                        resource, null);
                            }
                        });
            } else {
                helper.setVisible(R.id.iv, true);
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        null, null);
            }
        }
    }
}
