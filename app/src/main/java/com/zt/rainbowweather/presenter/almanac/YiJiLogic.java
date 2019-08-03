package com.zt.rainbowweather.presenter.almanac;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zt.weather.R;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.ui.activity.YiJiTypeDetailsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YiJiLogic implements BaseQuickAdapter.OnItemClickListener{

//    private List<String> list = new ArrayList<>();
//    private List<String> list2 = new ArrayList<>();

    private String[] suitables = new String[]{"生活类","婚姻类","建筑类"};
    private String[] avoids = new String[]{"搬家","到岗就职","栽种植物"};
    private static YiJiLogic yiJiLogic;
    private Context context;

    public static YiJiLogic getYiJiLogic() {
        if (yiJiLogic == null) {
            synchronized (YiJiLogic.class){
                if (yiJiLogic == null) {
                    yiJiLogic = new YiJiLogic();
                }
            }
        }
        return yiJiLogic;
    }

    public void initData(Context context,RecyclerView recyclerView,List<HuangLi.DataBean.Bean> Beans){
        this.context = context;
//        list.addAll(Arrays.asList(suitables));
//        list2.addAll(Arrays.asList(avoids));
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.fragment_yi_ji_item, Beans, (viewHolder, item) -> {
                viewHolder.setText(R.id.yi_ji_item_title,item.getType()+"");
                RecyclerView recyclerView1 = viewHolder.getView(R.id.yi_ji_item_recycler);
                recyclerView1.setLayoutManager(new GridLayoutManager(context, 4));
                BaseAdapter baseAdapter1 = new BaseAdapter<>(R.layout.yi_ji_item_recycler_item, item.getValues(), (viewHolder1, item1) -> {
                    viewHolder1.setText(R.id.yi_ji_item_recycler_tv,item1);
                });
                baseAdapter1.setOnItemClickListener(this);
                recyclerView1.setAdapter(baseAdapter1);
            });
            recyclerView.setAdapter(baseAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//        Intent intent = new Intent(context, YiJiTypeDetailsActivity.class);
//        context.startActivity(intent);
    }
}
