package com.zt.rainbowweather.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zt.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.BasicApplication;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;
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
            if(item.header.currTemp != 0){
                helper.setText(R.id.tv_temperature, String.format("%d℃", item.header.currTemp));
                SaveShare.saveValue(mContext,item.city.name,item.header.currTemp+","+ item.header.weather.iconRes);
            }else{
               String currTemp = SaveShare.getValue(mContext,item.city.name);
               if(!TextUtils.isEmpty(currTemp)){
                   String[] iconRes = currTemp.split(",");
                   helper.setText(R.id.tv_temperature, String.format("%d℃", Integer.parseInt(iconRes[0])));
                   Glide.with(mContext)
                           .load(WeatherUtils.getWeatherStatus(Integer.parseInt(iconRes[1])).iconRes)
                           .into((ImageView) helper.getView(R.id.iv_head));
               }
            }
            helper.setText(R.id.tv_address, item.city.name);
            if(item.header != null && item.header.weather != null && WeatherUtils.getWeatherStatus(item.header.weather.iconRes) != null){
                if(item.header.weather.iconRes != 0){
                     Glide.with(mContext)
                            .load(WeatherUtils.getWeatherStatus(item.header.weather.iconRes).iconRes)
                            .into((ImageView) helper.getView(R.id.iv_head));
                }
            }

            final TextView tvAddress = helper.getView(R.id.tv_address);
            if (BasicApplication.getLocatedCity() != null && item.city.isLocate.equals("1")) {

                    helper.setText(R.id.tv_temperature, String.format("%d℃", item.header.currTemp));


                helper.setText(R.id.tv_address,BasicApplication.getLocatedCity().affiliation);
//                helper.setVisible(R.id.iv, false);
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
//                helper.setVisible(R.id.iv, true);
                tvAddress.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        null, null);
            }
        }
    }

    private List<City> getCityFromSP() {
        String json = SPUtils.getInstance(ConstUtils.SP_FILE_NAME).getString("addresses");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, new TypeToken<List<City>>() {
            }.getType());
        }
        return null;
    }
}
