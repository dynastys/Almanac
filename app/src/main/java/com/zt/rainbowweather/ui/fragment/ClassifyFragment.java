package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.zt.rainbowweather.presenter.atlas.ClassifyLogic;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 图集分类页面
 */
public class ClassifyFragment extends BaseFragment {

    @BindView(R.id.list_recycler)
    RecyclerView listRecycler;
    Unbinder unbinder;

    @Override
    protected int getLayoutRes() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initData(View view) {
        ClassifyLogic.getClassifyLogic().InitView(getActivity(),listRecycler,null);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
