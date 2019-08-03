package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.zt.rainbowweather.presenter.atlas.RecommendLogic;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 图集推荐页面
 */
public class RecommendFragment extends BaseFragment {
    @BindView(R.id.list_recycler)
    RecyclerView ListRecycler;
    Unbinder unbinder;

    @Override
    protected int getLayoutRes() {
        return R.layout.recommend_fragment;
    }

    @Override
    protected void initData(View view) {
        RecommendLogic recommendLogic = new RecommendLogic();
        recommendLogic.InitView(getActivity(),ListRecycler,null,true);

    }

    @Override
    protected void initListener() {

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && ListRecycler != null){
            RecommendLogic.getRecommendLogic().InitView(getActivity(),ListRecycler,null,true);
        }
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
