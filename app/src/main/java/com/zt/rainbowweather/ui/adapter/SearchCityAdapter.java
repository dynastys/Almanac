package com.zt.rainbowweather.ui.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zt.weather.R;
import com.zt.rainbowweather.entity.City;

import java.util.List;


/**
 * Created by hcg on 2018/5/6.
 */

public class SearchCityAdapter extends BaseQuickAdapter<City, BaseViewHolder> {
    public SearchCityAdapter(@Nullable List<City> data) {
        super(R.layout.item_search_city, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, City item) {
        Log.e("onError", "convert: " );
        helper.setText(R.id.tv_address, String.format("%s - %s", item.name, item.affiliation));
    }
}
