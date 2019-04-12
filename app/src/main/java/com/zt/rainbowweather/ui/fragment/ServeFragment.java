package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.check.ox.sdk.LionListener;
import com.check.ox.sdk.LionStreamerView;
import com.chenguang.weather.R;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.Interface.SwipeRefreshListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.config.SwipeRefreshOnRefresh;
import com.xy.xylibrary.refresh.SuperEasyRefreshLayout;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.ServiceList;
import com.zt.rainbowweather.presenter.GridItemDecoration;
import com.zt.rainbowweather.presenter.request.NewsRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.utils.utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 外接服务
 */
public class ServeFragment extends BaseFragment implements SwipeRefreshListener, RequestSyntony<ServiceList>, LionListener {

    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.list_recycler)
    RecyclerView ListRecycler;
    @BindView(R.id.shop_list_swipe_refresh)
    SuperEasyRefreshLayout shopListSwipeRefresh;
    Unbinder unbinder;
    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    @BindView(R.id.shop_list_bar)
    TextView shopListBar;
    @BindView(R.id.TMBrView)
    LionStreamerView TMBrView;

    private BaseAdapter baseAdapter, baseAdapter2;
    private List<String> list2 = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_service;
    }

    @Override
    protected void initData(View view) {
        ViewGroup.LayoutParams layoutParams = shopListBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(getActivity());
        shopListBar.setLayoutParams(layoutParams);
        fileHeadTitle.setText("服务");
        recyclerService.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        ListRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                .setHorizontalSpan(R.dimen.dp_1)
                .setVerticalSpan(R.dimen.dp_1)
                .setColorResource(R.color.nb_divider_narrow)
                .setShowLastLine(true)
                .build();
        ListRecycler.addItemDecoration(divider);
        recyclerService.addItemDecoration(divider);

        SwipeRefreshOnRefresh swipeRefreshOnRefresh = new SwipeRefreshOnRefresh();
        swipeRefreshOnRefresh.SwipeRefresh(getActivity(), shopListSwipeRefresh, ServeFragment.this);
    }

    @Override
    protected void initListener() {
        NewsRequest.getNewsRequest().getServiceListData(getActivity(), ServeFragment.this);
        TMBrView.setAdListener(ServeFragment.this);
        TMBrView.loadAd(273299);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        if (shopListSwipeRefresh != null) {
            shopListSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onDropHeight(float overscrollTop) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ServiceFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ServiceFragment");
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(ServiceList serviceList) {
        if (serviceList.getDate().size() > 0) {
            baseAdapter2 = new BaseAdapter<>(R.layout.popup_recycler_item, serviceList.getDate().get(0).getLinks(), (viewHolder, item) -> {
                try {
                    viewHolder.setText(R.id.popup_recycler_tv, item.getName());
                    GlideUtil.getGlideUtil().setImages(getActivity(), item.getIcon_url(), (ImageView) viewHolder.getView(R.id.popup_recycler_image));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            baseAdapter2.setOnItemClickListener((adapter, view, position) -> {
                switch (serviceList.getDate().get(0).getLinks().get(position).getLink_action()) {
                    case 1://跳转
                        AdviseMoreDetailActivity.startActivity(getActivity(), serviceList.getDate().get(0).getLinks().get(position).getName(), serviceList.getDate().get(0).getLinks().get(position).getLink());
                        break;
                    case 2://下载
                        utils.Download(getActivity(), serviceList.getDate().get(0).getLinks().get(position).getLink());
                        break;
                    default:

                }

            });
            recyclerService.setAdapter(baseAdapter2);
            if (serviceList.getDate().size() > 1) {
                baseAdapter = new BaseAdapter<>(R.layout.popup_recycler_item, serviceList.getDate().get(1).getLinks(), (viewHolder, item) -> {
                    try {
                        viewHolder.setText(R.id.popup_recycler_tv, item.getName());
                        GlideUtil.getGlideUtil().setImages(getActivity(), item.getIcon_url(), viewHolder.getView(R.id.popup_recycler_image));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                baseAdapter.setOnItemClickListener((adapter, view, position) -> {
                    switch (serviceList.getDate().get(1).getLinks().get(position).getLink_action()) {
                        case 1://跳转
                            AdviseMoreDetailActivity.startActivity(getActivity(), serviceList.getDate().get(1).getLinks().get(position).getName(), serviceList.getDate().get(1).getLinks().get(position).getLink());
                            break;
                        case 2://下载
                            utils.Download(getActivity(), serviceList.getDate().get(1).getLinks().get(position).getLink());
                            break;
                        default:

                    }

                });
                ListRecycler.setAdapter(baseAdapter);
            }

        }
    }


    @Override
    public void onReceiveAd() {

    }

    @Override
    public void onFailedToReceiveAd() {

    }

    @Override
    public void onLoadFailed() {

    }

    @Override
    public void onCloseClick() {

    }

    @Override
    public void onAdClick() {

    }

    @Override
    public void onAdExposure() {

    }
}
