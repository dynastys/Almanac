package com.zt.rainbowweather.presenter.atlas;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.zt.weather.R;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.background.DataBean;
import com.zt.rainbowweather.entity.background.SkinTheme;
import com.zt.rainbowweather.presenter.request.BackgroundRequest;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.ToastUtils;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;

/**
 * 图集推荐
 * */
public class RecommendLogic implements RequestSyntony<SkinTheme>,SwipeRefreshListener {

    private static RecommendLogic recommendLogic;
    private Activity context;
    private BaseAdapter baseAdapter;
    private List<DataBean> results = new ArrayList<>();
    //    private SuperEasyRefreshLayout atlasListSwipeRefresh;
    private RecyclerView shopListRecycler;
    private int page_no = 1;
    private List<DataBean> save = new ArrayList<>();

    public static RecommendLogic getRecommendLogic() {
        if (recommendLogic == null) {
            synchronized (RecommendLogic.class){
                if (recommendLogic == null) {
                    recommendLogic = new RecommendLogic();
                }
            }
        }
        return recommendLogic;
    }

    public void getData(){
        BackgroundRequest.getBackgroundRequest().getBackdropThemesData(context,RecommendLogic.this);
    }

    public void InitView(Activity context,RecyclerView shopListRecycler,SuperEasyRefreshLayout atlasListSwipeRefresh,boolean b){
        this.context = context;
        this.shopListRecycler = shopListRecycler;
//        this.atlasListSwipeRefresh = atlasListSwipeRefresh;
        try {
            shopListRecycler.setLayoutManager(new GridLayoutManager(context, 2));
//        //成功获取更多数据（可以直接往适配器添加数据）
            baseAdapter = new BaseAdapter<>(R.layout.grad_item, results, (viewHolder, item) -> {
                try {
                    save = LitePal.where("cover = ?", item.getCover()).find(DataBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(!TextUtils.isEmpty(SaveShare.getValue(context,"IDBg")) && item.getBackdrop_theme_id() == Integer.parseInt(SaveShare.getValue(context,"IDBg"))){
                    viewHolder.setText(R.id.download,"使用中");
                }else {
                    if(save != null && save.size() > 0){
                        viewHolder.setText(R.id.download,"使用");
                    }else{
                        viewHolder.setText(R.id.download,"使用");
                    }
                }
                viewHolder.setText(R.id.grad_item_details,item.getName());
                if(!TextUtils.isEmpty(item.getCover())){
                    GlideUtil.getGlideUtil().setImages(context, item.getCover(), viewHolder.getView(R.id.grad_item_image));
                }
                viewHolder.getView(R.id.download).setOnClickListener(v -> {
                    TextView textView = viewHolder.getView(R.id.download);
                    switch (textView.getText().toString()){
                        case "使用":
                            if(item.getCover() != null){
                                List<DataBean> resultsBeans = new ArrayList<>();
    //                            try {
    //                                resultsBeans = LitePal.where("cover = ?",item.getCover()).find(DataBean.class);
    //                            } catch (Exception e) {
    //                                e.printStackTrace();
    //                            }
    //                            if(resultsBeans.size() > 0){
                                SaveShare.saveValue(context,"INUSE",item.getCover());
                                SaveShare.saveValue(context,"IDBg",item.getBackdrop_theme_id()+"");
                                SaveShare.saveValue(context,"backdrop_theme_id",item.getBackdrop_theme_id()+"");
                                ConstUtils.ISBG = true;
    //                            }
                                baseAdapter.notifyDataSetChanged();
                            }
                            break;
                        case "添加":
                            if(save == null || save.size() == 0){
                                viewHolder.setText(R.id.download,"使用");
                                List<DataBean> list = new ArrayList<>();
                                list.add(item);
                                LitePal.saveAll(list);
                            }
                            ToastUtils.showShort("已添加");
                            break;
                        case "使用中":
                            ToastUtils.showShort("使用中");
                            break;
                    }
                });
                if(!TextUtils.isEmpty(SaveShare.getValue(context,item.getCover()))){
                    viewHolder.setText(R.id.likenum_int,"14");
                }else{
                    viewHolder.setText(R.id.likenum_int,"13");

                }
                viewHolder.getView(R.id.give_a_like).setOnClickListener(v -> {
                    if(TextUtils.isEmpty(SaveShare.getValue(context,item.getCover()))){
                        viewHolder.setText(R.id.likenum_int,"14");
                        SaveShare.saveValue(context,item.getCover(),item.getCover());

                    }else{
                        ToastUtils.showShort("已点赞");
                    }
                });

            });
            baseAdapter.setOnItemClickListener((adapter, view, position) -> {
            });
            shopListRecycler.setAdapter(baseAdapter);
            if(b){
    //            // 滑动最后一个Item的时候回调onLoadMoreRequested方法
    //            baseAdapter.setOnLoadMoreListener(() -> getData(), shopListRecycler);
    //            SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
    //            swipeRefreshOnRefresh.SwipeRefresh(context,atlasListSwipeRefresh,RecommendLogic.this);
                getData();
            }else{
                getResults();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getResults() {
        try {
            save = LitePal.findAll(DataBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseAdapter.replaceData(save);

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(SkinTheme skinTheme) {
        results.addAll(skinTheme.getData());
        baseAdapter.setNewData(skinTheme.getData());
//        if (atlasListSwipeRefresh != null) {
//            atlasListSwipeRefresh.setRefreshing(false);
//        }
//        if (skinTheme.getData() != null && skinTheme.getData().size() != 0) {
////                          NoListBg.setVisibility(View.GONE);
//            if(page_no == 1){
//                baseAdapter.setNewData(skinTheme.getData());
//            }else{
//                //成功获取更多数据（可以直接往适配器添加数据）
//                baseAdapter.addData(skinTheme.getData());
//            }
//            //加载更多完成
//            baseAdapter.loadMoreComplete();
//            page_no++;
//        } else {
//            //加载完成
//            baseAdapter.loadMoreEnd();
//
//        }
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
