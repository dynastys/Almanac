package com.zt.rainbowweather.presenter.atlas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zt.weather.R;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.GlideUtil;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.background.Background;
import com.zt.rainbowweather.presenter.request.BackgroundRequest;
import com.zt.rainbowweather.ui.activity.ClassifyActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassifyLogic implements RequestSyntony<Background>,SwipeRefreshListener {

    private static ClassifyLogic classifyLogic;
    private Activity context;
    private BaseAdapter baseAdapter;
    private List<Background.ResultsBean> results = new ArrayList<>();
    private SuperEasyRefreshLayout atlasListSwipeRefresh;
    private RecyclerView shopListRecycler;
    private int page_no;

    public static ClassifyLogic getClassifyLogic() {
        if (classifyLogic == null) {
            synchronized (ClassifyLogic.class){
                if (classifyLogic == null) {
                    classifyLogic = new ClassifyLogic();
                }
            }
        }
        return classifyLogic;
    }

    public void getData(){
        BackgroundRequest.getBackgroundRequest().getPictureData(context,ClassifyLogic.this);
    }

    public void InitView(Activity context,RecyclerView shopListRecycler,SuperEasyRefreshLayout atlasListSwipeRefresh){
        this.context = context;
        this.shopListRecycler = shopListRecycler;
        this.atlasListSwipeRefresh = atlasListSwipeRefresh;

        try {
            shopListRecycler.setLayoutManager(new LinearLayoutManager(context));
            baseAdapter = new BaseAdapter<>(R.layout.classify_list_item, results, (viewHolder, item) -> {
                viewHolder.setText(R.id.classify_title,item.getType()).setText(R.id.classify_title_tv,item.getType());
                GlideUtil.getGlideUtil().setImages(context, item.getUrl(), viewHolder.getView(R.id.classify_image));

            });
            baseAdapter.setOnItemClickListener((adapter, view, position) -> {
                Intent intent = new Intent(context,ClassifyActivity.class);
                context.overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
                context.startActivity(intent);
            });
//        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
//        baseAdapter.setOnLoadMoreListener(() -> getData(), shopListRecycler);
            shopListRecycler.setAdapter(baseAdapter);
            SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
            swipeRefreshOnRefresh.SwipeRefresh(context,atlasListSwipeRefresh,ClassifyLogic.this);
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Background background) {
        try {
            results.addAll(background.getResults());
            //成功获取更多数据（可以直接往适配器添加数据）
            baseAdapter.addData(background.getResults());
            if (atlasListSwipeRefresh != null) {
                atlasListSwipeRefresh.setRefreshing(false);
            }
            if (background.getResults() != null && background.getResults().size() != 0) {
    //                          NoListBg.setVisibility(View.GONE);
                if(page_no == 1){
                    baseAdapter.replaceData(background.getResults());
                }else{
                    //成功获取更多数据（可以直接往适配器添加数据）
                    baseAdapter.addData(background.getResults());
                }
                //加载更多完成
                baseAdapter.loadMoreComplete();
                page_no++;
            } else {
                //加载完成
                baseAdapter.loadMoreEnd();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        page_no = 1;
        results.clear();
        getData();
    }

    @Override
    public void onDropHeight(float overscrollTop) {

    }
}
