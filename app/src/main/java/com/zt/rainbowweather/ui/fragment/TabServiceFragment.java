package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.entity.background.PersonalCenterIcon;
import com.zt.rainbowweather.presenter.GridItemDecoration;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.utils.utils;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TabServiceFragment extends BaseFragment {

    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    Unbinder unbinder;
    private BaseAdapter baseAdapter;
    private CustomScrollViewPager viewPager;
    private List<PersonalCenterIcon.DataBean.IconsBean> iconsBeanList;

    public void setviewPager(CustomScrollViewPager viewPager) {
        this.viewPager = viewPager;
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tab_service;
    }

    @Override
    protected void initData(View view) {
        try {
            Bundle args = getArguments();
            iconsBeanList = (List<PersonalCenterIcon.DataBean.IconsBean>) args.getSerializable("Icon");
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerService.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        GridItemDecoration divider = new GridItemDecoration.Builder(Objects.requireNonNull(getActivity()))
                .setHorizontalSpan(R.dimen.dp_1)
                .setVerticalSpan(R.dimen.dp_1)
                .setColorResource(R.color.nb_divider_narrow)
                .setShowLastLine(true)
                .build();
//        recyclerService.addItemDecoration(divider);
        if (iconsBeanList != null && iconsBeanList.size() > 0) {
            baseAdapter = new BaseAdapter<>(R.layout.popup_recycler_item, iconsBeanList, (viewHolder, item) -> {
                try {
                    viewHolder.setText(R.id.popup_recycler_tv, item.getTitle());
                    GlideUtil.getGlideUtil().setImages(getActivity(), item.getCover(), viewHolder.getView(R.id.popup_recycler_image),1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            baseAdapter.setOnItemClickListener((adapter, view1, position) -> {
                switch (iconsBeanList.get(position).getIcon_type_id()) {
                    case 0://跳转
                        AdviseMoreDetailActivity.startActivity(getActivity(), iconsBeanList.get(position).getTitle(), iconsBeanList.get(position).getLink(),"0");
                        break;
                    case 1://下载
                        utils.Download(getActivity(), iconsBeanList.get(position).getLink());
                        break;
                    default:
                }
            });
            recyclerService.setAdapter(baseAdapter);
        }
//        AlmanacRequest.getAlmanacRequest().getGainIconData(getActivity(), 2, "",TabServiceFragment.this);
//        NewsRequest.getNewsRequest().getServiceListData(getActivity(), TabServiceFragment.this);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        assert rootView != null;
        unbinder = ButterKnife.bind(this, rootView);
        if (viewPager != null) {
            viewPager.setObjectForPosition(rootView, 0);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
