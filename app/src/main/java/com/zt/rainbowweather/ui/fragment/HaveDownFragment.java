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
 * 图集已下载页面
 */
public class HaveDownFragment extends BaseFragment {
    @BindView(R.id.have_list_recycler)
    RecyclerView haveListRecycler;
    Unbinder unbinder;

    @Override
    protected int getLayoutRes() {
        return R.layout.have_down_fragment;
    }

    @Override
    protected void initData(View view) {
      RecommendLogic.getRecommendLogic().InitView(getActivity(),haveListRecycler,null,false);
    }

    @Override
    protected void initListener() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if(isVisibleToUser && haveListRecycler != null){
                RecommendLogic.getRecommendLogic().InitView(getActivity(),haveListRecycler,null,false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
