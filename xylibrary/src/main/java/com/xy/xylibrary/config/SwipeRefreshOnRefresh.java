package com.xy.xylibrary.config;

import android.app.Activity;

import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;


public class SwipeRefreshOnRefresh {

    /*刷新*/
    public void SwipeRefresh(Activity context, SuperEasyRefreshLayout shopListSwipeRefresh, final SwipeRefreshListener swipeRefreshListener) {
        if (shopListSwipeRefresh != null) {
            shopListSwipeRefresh.setOnRefreshListener(new SuperEasyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    swipeRefreshListener.onRefresh();
                }

                @Override
                public void onDropHeight(float overscrollTop) {
                    swipeRefreshListener.onDropHeight(overscrollTop);
                }
            });


        }
    }


}
