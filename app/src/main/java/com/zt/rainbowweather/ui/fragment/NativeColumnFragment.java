package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.zt.weather.R;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.background.IsUserLight;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.utils.Util;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 原生资讯页面（暂时不用）
 * */
public class NativeColumnFragment extends BaseFragment implements SwipeRefreshListener, RequestSyntony<Article>, AdProtogenesisListener {

    @BindView(R.id.recycler_layout_list)
    RecyclerView listRecycler;
    @BindView(R.id.swipe_refresh)
    SuperEasyRefreshLayout swipeRefresh;
    @BindView(R.id.stick_commodity)
    ImageView stickCommodity;
    Unbinder unbinder;

    private String text;
    private boolean ISfirst = false;
    private List<Article.DataBean> beans = new ArrayList<>();
    private BaseAdapter baseAdapter;
    private int AdSize = 4;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, Article.DataBean> adMap = new HashMap<>();
    private int Size;
    private ScrollLinearLayoutManager scrollLinearLayoutManager;
    private int pageindex = 1;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_native_column;
    }

    @Override
    protected void initData(View view) {
        beans.clear();
        adMap.clear();
        scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        listRecycler.setLayoutManager(scrollLinearLayoutManager);
        baseAdapter = new BaseAdapter<>(R.layout.item_advise_detail_one, beans, (viewHolder, item) -> {
            if(viewHolder.getAdapterPosition() > 8){
                stickCommodity.setVisibility(View.VISIBLE);
            }else{
                stickCommodity.setVisibility(View.GONE);
            }
            if (viewHolder.getAdapterPosition() > Size && viewHolder.getAdapterPosition() % 5 == 0) {
                Size = viewHolder.getAdapterPosition();
                // 获得信息流广告对象
                Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "2b937c83-dd4f-4936-b7e5-3b66dbae0cca", "67C53558D3E3485EA681EA21735A5003", this);
            }
            switch (item.getList_show_type()) {
                case 0:
                case 1:
                    viewHolder.getView(R.id.iv_title_rel).setVisibility(View.VISIBLE);
                    GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title), 0);
                    viewHolder.setText(R.id.tv_title, item.getTitle())
                            .setText(R.id.tv_from, item.getFrom_name());
                    break;
                case 2:
                    viewHolder.getView(R.id.iv_title_r_rel).setVisibility(View.VISIBLE);
                    GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_title_r), 0);
                    viewHolder.setText(R.id.tv_title_r, item.getTitle())
                            .setText(R.id.tv_from_r, item.getFrom_name());
                    break;
                case 4:
                    viewHolder.getView(R.id.tv_title_x_lin).setVisibility(View.VISIBLE);
                    GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_x1), 0);
                    GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(1), viewHolder.getView(R.id.iv_image_x2), 0);
                    GlideUtil.getGlideUtil().setImages(getActivity(), item.getArticle_imgs().get(2), viewHolder.getView(R.id.iv_image_x3), 0);
                    viewHolder.setText(R.id.tv_title_x, item.getTitle())
                            .setText(R.id.tv_from_x, item.getFrom_name());
                    break;
                case 3:
                    viewHolder.getView(R.id.tv_title_d_lin).setVisibility(View.VISIBLE);
                    GlideUtil.getGlideUtil().setImages(getActivity(),item.getArticle_imgs().get(0), viewHolder.getView(R.id.iv_image_d), 0);
                    viewHolder.setText(R.id.tv_title_d, item.getTitle())
                            .setText(R.id.tv_from_d, item.getFrom_name());

                    break;
            }
            if (item.nativelogic != null) {
                item.nativelogic.AdShow(null);
            }

        });
        baseAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if (adMap.get(position) != null) {
                adMap.get(position).nativelogic.OnClick(null);
            } else {
                AdviseMoreDetailActivity.startActivity(getActivity(), beans.get(position).getTitle(), beans.get(position).getHtml_url());
            }
        });
        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        baseAdapter.setOnLoadMoreListener(() -> NewsRequest.getNewsRequest().getNewsListData(getActivity(), text, pageindex, 20, NativeColumnFragment.this), listRecycler);
        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        listRecycler.setItemAnimator(new DefaultItemAnimator());
//        listRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        listRecycler.setAdapter(baseAdapter);
        if (SaveShare.getValue(getActivity(), "Channelid1").equals(text)) {
            beans.clear();
            NewsRequest.getNewsRequest().getNewsListData(getActivity(), text, pageindex, 20, NativeColumnFragment.this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && listRecycler != null && !ISfirst && !SaveShare.getValue(getActivity(), "Channelid1").equals(text)) {
            ISfirst = true;
            showLoadingDialog("");
            beans.clear();
            NewsRequest.getNewsRequest().getNewsListData(getActivity(), text, pageindex, 20, NativeColumnFragment.this);
            EventBus.getDefault().post(new IsUserLight(false));
        }
    }

    @Override
    protected void initListener() {
        SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
        swipeRefreshOnRefresh.SwipeRefresh(getActivity(), swipeRefresh, NativeColumnFragment.this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bundle args = getArguments();
//          获取栏目号（有时候获取不到，需要第二次获取）
            text = args != null ? args.getString("text") : "1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        beans.clear();
        adMap.clear();
        NewsRequest.getNewsRequest().getNewsListData(getActivity(), text, pageindex, 20, NativeColumnFragment.this);
    }

    @Override
    public void onDropHeight(float overscrollTop) {
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(Article article) {
        try {
            if (swipeRefresh != null) {
                swipeRefresh.setRefreshing(false);
            }
            if (article.getData() != null && article.getData().size() > 0) {
                if (beans.size() == 0) {
                    pageindex = 1;
                    baseAdapter.setNewData(article.getData());
                } else {
                    pageindex++;
                    baseAdapter.addData(article.getData());
                }
                beans.addAll(article.getData());
                dismissLoadingDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载更多完成
                        baseAdapter.loadMoreComplete();
                    }
                }, 100);
            } else {
                //加载完成
                baseAdapter.loadMoreEnd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onADReady(Native aNative, NativeAd nativeAd) {
        try {
            Article.DataBean dateBean = new Article.DataBean();
            dateBean.nativelogic = nativeAd;
            dateBean.setFrom_name("广告");
            dateBean.setList_show_type(1);
            dateBean.setArticle_imgs(aNative.infoIcon);
            dateBean.setTitle(aNative.title + "\n" + aNative.desc);
            baseAdapter.addData(AdSize, dateBean);
            if (nativeAd != null) {
                nativeAd.AdShow(null);
            }
            beans.add(AdSize, dateBean);
            adMap.put(AdSize, dateBean);
            AdSize += 4;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAdFailedToLoad(String s) {
    }

    @OnClick(R.id.stick_commodity)
    public void onClick() {
        Util.MoveToPosition(scrollLinearLayoutManager,listRecycler,0);
    }
}
