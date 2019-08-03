package com.zt.rainbowweather.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.presenter.almanac.YiJiLogic;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 宜忌界面
 * */
@SuppressLint("ValidFragment")
public class YiJiFragment extends BaseFragment {

    @BindView(R.id.yi_ji_category)
    RecyclerView yiJiCategory;
    Unbinder unbinder;

     private List<HuangLi.DataBean.Bean> Beans;
    public YiJiFragment(List<HuangLi.DataBean.Bean> Beans){
        this.Beans = Beans;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_yi_ji;
    }

    @Override
    protected void initData(View view) {
        try {
            YiJiLogic yiJiLogic = new YiJiLogic();
            yiJiLogic.initData(getActivity(),yiJiCategory,Beans);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
