package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.utils.SaveShare;
import com.zt.weather.R;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
import com.zt.rainbowweather.entity.news.Article;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;
import com.zt.rainbowweather.utils.BarUtils;
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

/**
 * @author zw
 * @time 2019-3-8
 * 生活指数详情
 * */
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
    private int[] icon = new int[]{R.mipmap.comf,R.mipmap.drsg,R.mipmap.flu,R.mipmap.sport,
            R.mipmap.trav,R.mipmap.uv,R.mipmap.cw,R.mipmap.air,
            R.mipmap.ac,R.mipmap.ag,R.mipmap.gl,R.mipmap.mu,
            R.mipmap.airc,R.mipmap.ptfc,R.mipmap.fsh,R.mipmap.spi};
    private Map<Integer,Integer> map;
    private NativeAd nativelogic;
    private int pageindex = 1;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AdviseDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AdviseDetailActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

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
            Log.e("mAdvise", "addHeader: "+ map.size());
            if(headerViewHolder.ivHeader != null && map != null){
                headerViewHolder.ivHeader.setImageResource(map.get(Integer.parseInt(mAdvise.indexId)));
            }
            Glide.with(AdviseDetailActivity.this).load(mAdvise.headerImgUrl).into(headerViewHolder.ivBg);
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
        NewsRequest.getNewsRequest().getNewsListData(AdviseDetailActivity.this,mAdvise.indexId,pageindex,20,AdviseDetailActivity.this);
    }

    private void showAdviseDetail(Article article) {
        try {
            if(article.getData() != null && article.getData().size() > 0){
                pageindex++;
                mAdapter.setNewData(article.getData());
                mAdapter.setOnItemClickListener((adapter, view, position) -> {
                    if(position == 1 && nativelogic != null){
                        nativelogic.OnClick(null);
                    }else{
                        AdviseMoreDetailActivity.startActivity(AdviseDetailActivity.this, article.getData().get(position).getTitle(), article.getData().get(position).getHtml_url(),"1");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        String ISAD = SaveShare.getValue(AdviseDetailActivity.this, "ISAD");
        if (!TextUtils.isEmpty(ISAD) && ISAD.equals("1")) {
            // 获得信息流广告对象
            nativelogic = Ad.getAd().NativeAD(this, "98f8e423-02e0-49f5-989f-af46f5c59203", "a6f5748d-f822-489b-811c-b08ba658c096", "67C53558D3E3485EA681EA21735A5003", this);
        }
       }

    @Override
    public void granted() {
    }

    @Override
    public void denied(List<String> deniedList) {
    }

    @Override
    public void onADReady(Native aNative,NativeAd nativelogic) {
        Article.DataBean dateBean = new Article.DataBean();
        dateBean.nativelogic = nativelogic;
        dateBean.setFrom_name("广告");
        dateBean.setList_show_type(1);
        dateBean.setArticle_imgs(aNative.infoIcon);
        dateBean.setTitle(aNative.title +"\n" + aNative.desc);
        mAdapter.addData(1,dateBean);
    }

    @Override
    public void onAdFailedToLoad(String s) {
        nativelogic = null;
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
        try {
            MobclickAgent.onEvent(AdviseDetailActivity.this,"shengH_index");
            mAdvise = (AdviseTitleBean) getIntent().getSerializableExtra("advise");
            BarUtils.setStatusBarAlpha(AdviseDetailActivity.this, 0);//设置为透明
            BarUtils.addMarginTopEqualStatusBarHeight(titleBar);
            titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            titleBar.setAlpha(0);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("UseSparseArrays")
    @Override
    protected void bindViews() {
        try {
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
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
