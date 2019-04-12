package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.ColumnHorizontalPackage;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.ColumnData;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.entity.news.NewColumn;
import com.zt.rainbowweather.presenter.ScrollLinearLayoutManager;
import com.chenguang.weather.R;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseDetailActivity;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class ColumnFragment extends BaseFragment implements RequestSyntony<Article>,AdProtogenesisListener {
     Unbinder unbinder;
    @BindView(R.id.list_recycler)
    RecyclerView listRecycler;

    private BaseAdapter baseAdapter;
    private List<Article.DateBean> list_item = new ArrayList<>();
    private String text;
    private CustomScrollViewPager viewPager;
    private View rootView;
    private NativeAd nativelogic,nativelogic2;
    private Map<Integer,Article.DateBean> adMap = new HashMap<>();
    private boolean ISfirst = false;
    private List<Article.DateBean> beans = new ArrayList<>();

    public ColumnFragment() {

    }
    public void setviewPager(CustomScrollViewPager viewPager){
        this.viewPager = viewPager;

    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_column;
    }

    @Override
    protected void initData(View view) {
        for (int i = 0; i < 20; i++) {
            Article.DateBean dateBean = new Article.DateBean();
            beans.add(dateBean);
        }

            // 获得信息流广告对象
            nativelogic = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "2b937c83-dd4f-4936-b7e5-3b66dbae0cca", "67C53558D3E3485EA681EA21735A5003", this);
            // 获得信息流广告对象
            nativelogic2 = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "80b46cdb-e90f-47fd-aeca-b0b7c00272ff", "67C53558D3E3485EA681EA21735A5003", this);

        listRecycler.setLayoutManager(new ScrollLinearLayoutManager(getActivity()));
        baseAdapter = new BaseAdapter<>(R.layout.item_advise_detail_one, beans, (viewHolder, item) -> {
            GlideUtil.getGlideUtil().setImages(getActivity(), item.getImg(), viewHolder.getView(R.id.iv_title), 0);
            viewHolder.setText(R.id.tv_title, item.getTitle())
                    .setText(R.id.tv_from, item.getAuthor());
            if (!TextUtils.isEmpty(item.getPublishTime())) {
                viewHolder.setText(R.id.tv_time, item.getPublishTime().substring(0, 10));
            } else {
                viewHolder.getView(R.id.tv_time).setVisibility(View.GONE);
            }
            Log.e("BaseAdapter", "initData: " );
            if (item.nativelogic != null) {
                item.nativelogic.AdShow(null);
            }
        });
        baseAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if(adMap.get(position) != null){
                adMap.get(position).nativelogic.OnClick(null);
            }
            else  if(adMap.size() == 2){
                if(position > 7){
                    position = position - 2;
                }else if(position > 1){
                    position = position - 1;
                }
                AdviseMoreDetailActivity.startActivity(getActivity(), beans.get(position).getTitle(), beans.get(position).getUrl());
            }else if (adMap.size() == 1){
                if (position>1){
                    position = position - 1;
                }
                AdviseMoreDetailActivity.startActivity(getActivity(), beans.get(position).getTitle(), beans.get(position).getUrl());
            }
            else {
                AdviseMoreDetailActivity.startActivity(getActivity(), beans.get(position).getTitle(), beans.get(position).getUrl());
            }
        });

        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        baseAdapter.setOnLoadMoreListener(() -> {
            // //加载完成
            baseAdapter.loadMoreEnd();
            //            baseAdapter.loadMoreFail();
            //            baseAdapter.loadMoreComplete();
        }, listRecycler);
        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        listRecycler.setItemAnimator(new DefaultItemAnimator());
        listRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        listRecycler.setAdapter(baseAdapter);
        listRecycler.setHasFixedSize(true);
        listRecycler.setNestedScrollingEnabled(false);
        if(SaveShare.getValue(getActivity(),"Channelid").equals(text)){
            NewsRequest.getNewsRequest().getNewsListData(getActivity(),text,1,20,ColumnFragment.this);
        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && listRecycler != null && !ISfirst && !SaveShare.getValue(getActivity(),"Channelid").equals(text)){
            ISfirst = true;
            showLoadingDialog("");
//            // 获得信息流广告对象
//            nativelogic = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "2b937c83-dd4f-4936-b7e5-3b66dbae0cca", "67C53558D3E3485EA681EA21735A5003", this);
//            // 获得信息流广告对象
//            nativelogic2 = Ad.getAd().NativeAD(getActivity(), "98f8e423-02e0-49f5-989f-af46f5c59203", "80b46cdb-e90f-47fd-aeca-b0b7c00272ff", "67C53558D3E3485EA681EA21735A5003", this);

            NewsRequest.getNewsRequest().getNewsListData(getActivity(),text,1,20,ColumnFragment.this);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        if(viewPager != null){
            viewPager.setObjectForPosition(rootView,0);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            beans.clear();
            beans.addAll(article.getDate());
            dismissLoadingDialog();
            baseAdapter.setNewData(article.getDate());
            if(adMap.size() == 1){
                baseAdapter.addData(1,adMap.get(1));
            }else if(adMap.size() == 2){
                baseAdapter.addData(1,adMap.get(1));
                baseAdapter.addData(8,adMap.get(8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onADReady(Native aNative) {
        Article.DateBean dateBean = new Article.DateBean();
        if(adMap.size() > 0){
            dateBean.nativelogic = nativelogic2;
            dateBean.setAuthor("广告");
            dateBean.setImg(aNative.infoIcon.get(0));
            dateBean.setTitle(aNative.title +"\n" + aNative.desc);
             if(baseAdapter.getData().size() < 22){
                 baseAdapter.addData(8,dateBean);
             }
            adMap.put(8,dateBean);
        }else{
            dateBean.nativelogic = nativelogic;
            dateBean.setAuthor("广告");
            dateBean.setImg(aNative.infoIcon.get(0));
            dateBean.setTitle(aNative.title +"\n" + aNative.desc);
            if(baseAdapter.getData().size() < 21){
                baseAdapter.addData(1,dateBean);
            }
            adMap.put(1,dateBean);
        }

    }

    @Override
    public void onAdFailedToLoad(String s) {

    }
}
