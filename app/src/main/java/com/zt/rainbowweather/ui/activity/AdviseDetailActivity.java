package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chenguang.weather.R;
import com.google.gson.Gson;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.advise.AdviseDetailBean;
import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;
import com.zt.rainbowweather.ui.fragment.ColumnFragment;
import com.zt.rainbowweather.utils.BarUtils;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.view.MagicButton;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;
import com.zt.xuanyin.entity.model.Native;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AdviseDetailActivity extends BaseActivity implements RequestSyntony<Article>,AdProtogenesisListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_detail)
    RecyclerView rvDetail;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.magic_btn)
    MagicButton magicBtn;

    private AdviseTitleBean mAdvise;
    private AdviseDetailAdapter mAdapter;
    private HeaderViewHolder headerViewHolder;
    private LinearLayoutManager linearLayoutManager;
    private int[] ID = new int[]{26,28,29,30,31,32,27,33,34,35,36,37,38,40,39,32};
    private int[] icon = new int[]{R.mipmap.comf,R.mipmap.drsg,R.mipmap.flu,R.mipmap.sport,R.mipmap.trav,R.mipmap.uv,R.mipmap.cw,R.mipmap.air,R.mipmap.ac,R.mipmap.ag,R.mipmap.gl,R.mipmap.mu,R.mipmap.airc,R.mipmap.ptfc,R.mipmap.fsh,R.mipmap.spi};
    private Map<Integer,Integer> map;
    private NativeAd nativelogic;

    public static void startActivity(Context context, AdviseTitleBean adviseTitleBean) {
        Intent intent = new Intent(context, AdviseDetailActivity.class);
        intent.putExtra("advise", adviseTitleBean);
        context.startActivity(intent);
    }


    private void addHeader() {
        try {
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.tvHeaderTitle.setText(mAdvise.title);
            headerViewHolder.tvHeaderContent.setText(mAdvise.headerSummary);
            headerViewHolder.ivHeader.setImageResource(map.get(Integer.parseInt(mAdvise.indexId)));

            Glide.with(AdviseDetailActivity.this)
                    .load(mAdvise.headerImgUrl)
                    .into(headerViewHolder.ivBg);
            mAdapter.addHeaderView(headerViewHolder.rootView);
            View footer = getLayoutInflater().inflate(R.layout.advise_footer,
                    (ViewGroup) rvDetail.getParent(), false);
            mAdapter.addFooterView(footer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    private void getData() {
        NewsRequest.getNewsRequest().getNewsListData(AdviseDetailActivity.this,mAdvise.indexId,1,20,AdviseDetailActivity.this);

    }

    private void showAdviseDetail(Article article) {
        try {
            mAdapter.setNewData(article.getDate());
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                if(position == 1 && nativelogic != null){
                    nativelogic.OnClick(null);
                }else{
                    AdviseMoreDetailActivity.startActivity(AdviseDetailActivity.this, article.getDate().get(position).getTitle(), article.getDate().get(position).getUrl());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
    }

    @OnClick(R.id.magic_btn)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Article article) {
        showAdviseDetail(article);
        // 获得信息流广告对象
        nativelogic = Ad.getAd().NativeAD(this, "98f8e423-02e0-49f5-989f-af46f5c59203", "a6f5748d-f822-489b-811c-b08ba658c096", "67C53558D3E3485EA681EA21735A5003", this);

    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }

    @Override
    public void onADReady(Native aNative) {
        Article.DateBean dateBean = new Article.DateBean();
        dateBean.nativelogic = nativelogic;
        dateBean.setAuthor("广告");
        dateBean.setImg(aNative.infoIcon.get(0));
        dateBean.setTitle(aNative.title +"\n" + aNative.desc);
        mAdapter.addData(1,dateBean);
    }

    @Override
    public void onAdFailedToLoad(String s) {

    }

    class HeaderViewHolder {
        private final Unbinder bind;
        View rootView;
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.iv_header)
        ImageView ivHeader;
        @BindView(R.id.tv_header_title)
        TextView tvHeaderTitle;
        @BindView(R.id.tv_header_content)
        TextView tvHeaderContent;
        @BindView(R.id.ll_content)
        RelativeLayout llContent;

        public HeaderViewHolder() {
            rootView = getLayoutInflater().inflate(R.layout.advise_header_view,
                    (ViewGroup) rvDetail.getParent(), false);
            bind = ButterKnife.bind(this, rootView);
        }

        public void unbind() {
            if (bind != null) {
                bind.unbind();
            }
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @Override
    protected Activity getContext() {
        return AdviseDetailActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_advise_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headerViewHolder != null) {
            headerViewHolder.unbind();
        }
    }

    @Override
    protected void loadViewLayout() {
        mAdvise = (AdviseTitleBean) getIntent().getSerializableExtra("advise");
        BarUtils.setStatusBarAlpha(AdviseDetailActivity.this, 0);//设置为透明
        BarUtils.addMarginTopEqualStatusBarHeight(titleBar);
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        titleBar.setAlpha(0);
    }

    @SuppressLint("UseSparseArrays")
    @Override
    protected void bindViews() {
        map = new HashMap<>();
        for (int i = 0; i < ID.length; i++) {
            map.put(ID[i],icon[i]);
        }
        tvTitle.setText(mAdvise.channelId);
        rvDetail.setLayoutManager(linearLayoutManager = new LinearLayoutManager(AdviseDetailActivity.this));
        rvDetail.addItemDecoration(new DividerItemDecoration(AdviseDetailActivity.this, DividerItemDecoration
                .VERTICAL));
        rvDetail.setAdapter(mAdapter = new AdviseDetailAdapter(null));
        addHeader();
        getData();
        rvDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                float scrollY = getRvDy();
                int height = headerViewHolder.rootView.getHeight() - titleBar.getHeight() -
                        BarUtils.getStatusBarHeight();
                float f = scrollY / height;
                if (f >= 1) {
                    magicBtn.setVisibility(View.GONE);
                    titleBar.setAlpha(1);
                    BarUtils.setStatusBarColor(AdviseDetailActivity.this, getResources().getColor(R.color
                            .colorPrimary));
                } else {
                    magicBtn.setVisibility(View.VISIBLE);
                    titleBar.setAlpha(f);
                    BarUtils.setStatusBarColor(AdviseDetailActivity.this, getResources().getColor(R.color
                            .colorPrimary), Math.round(f * 255));
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    public int getRvDy() {
        //获取第一个可见的item的位置
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition
                (firstVisibleItemPosition);
        //计算出还未移出屏幕的高度
        int itemTop = firstVisiableChildView.getTop();
        //由于每个item的高度并不相同，所以就循环相加获取总高度
        int sumHeight = 0;
        for (int i = 0; i < firstVisibleItemPosition; i++) {
            sumHeight += linearLayoutManager.findViewByPosition(firstVisibleItemPosition)
                    .getHeight();
        }
        //减去该Item还未移出屏幕的部分可得出滑动的距离
        return sumHeight - itemTop;
    }
}
