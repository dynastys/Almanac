package com.xy.xylibrary.ui.activity.task;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.ScrollLinearLayoutManager;
import com.xy.xylibrary.ui.activity.login.UserActiveInfo;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.CustomHorizontalProgresNoNum;

import java.util.List;

/**
 * 获取金币明细和提现
 * */
public class LookOverDetailLogic {

    private static LookOverDetailLogic lookOverDetailLogic;

    public static LookOverDetailLogic getLookOverDetailLogic() {
        if (lookOverDetailLogic == null) {
            synchronized (LookOverDetailLogic.class){
                if (lookOverDetailLogic == null) {
                    lookOverDetailLogic = new LookOverDetailLogic();
                }
            }
        }
        return lookOverDetailLogic;
    }

    /*获取金币明细*/
    public void setGoldDetail(Context context, RecyclerView listRecycler, final List<UserActiveInfo.DataBean.UserActiveVmsBean> beans){
        try {
            ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(context);
            setScrollEnable.setScrollEnable(false);
            listRecycler.setLayoutManager(setScrollEnable);
            BaseAdapter baseAdapter = new BaseAdapter<>(R.layout.recycler_item_look_gold, beans, new BaseAdapterListener<UserActiveInfo.DataBean.UserActiveVmsBean>() {
                @Override
                public void convertView(BaseViewHolder viewHolder, UserActiveInfo.DataBean.UserActiveVmsBean item) {
                    viewHolder.setText(R.id.look_gold_title_tv,  item.getActiveType());
                    viewHolder.setText(R.id.look_gold_details,  item.getName());
                    viewHolder.setText(R.id.look_gold_money,  "+"+item.getGold());
                    viewHolder.setText(R.id.look_gold_activate,  "+"+item.getActive());
                    viewHolder.setText(R.id.look_gold_time, Utils.times(item.getUpdateTime()));
                }
            });
            baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                }
            });
            listRecycler.addItemDecoration(new DividerItemDecoration(context, 1));
            listRecycler.setAdapter(baseAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
