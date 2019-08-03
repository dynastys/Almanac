package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xy.xylibrary.base.BaseFragment;
import com.zt.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FortuneJiYiFragment extends BaseFragment {
    @BindView(R.id.sui_id)
    TextView suiId;
    @BindView(R.id.year_lin)
    LinearLayout yearLin;
    @BindView(R.id.NongLiMonthDay)
    TextView NongLiMonthDay;
    @BindView(R.id.shengxiao)
    TextView shengxiao;
    @BindView(R.id.suitable)
    ImageView suitable;
    @BindView(R.id.suitable_content)
    RecyclerView suitableContent;
    @BindView(R.id.suitable_lin)
    LinearLayout suitableLin;
    @BindView(R.id.avoid)
    ImageView avoid;
    @BindView(R.id.avoid_content)
    RecyclerView avoidContent;
    @BindView(R.id.avoid_lin)
    LinearLayout avoidLin;
    @BindView(R.id.yi_ji_lin)
    LinearLayout yiJiLin;
    Unbinder unbinder;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_fortune_ji_yi;
    }

    @Override
    protected void initData(View view) {

    }

    @Override
    protected void initListener() {

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
}
