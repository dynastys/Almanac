package com.zt.rainbowweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.ColumnHorizontalScrollView;
import com.zt.rainbowweather.presenter.news.NativeNewsLogic;
import com.zt.rainbowweather.ui.activity.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * @author zw
 * @time 2019-3-8
 * 原生资讯页面（暂时不用）
 * */
public class NativeNewsFragment extends BaseFragment {

    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroupContent;
    @BindView(R.id.column)
    ColumnHorizontalScrollView column;
    @BindView(R.id.column_select)
    ImageView columnSelect;
    @BindView(R.id.viewpager_column)
    ViewPager viewpagerColumn;
    Unbinder unbinder;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;

    private NativeNewsLogic nativeNewsLogic;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_native_news;
    }

    @Override
    protected void initData(View view) {
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(getActivity());
        listBar.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.GONE);
        fileHeadTitle.setText("资讯");
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mRadioGroupContent != null && nativeNewsLogic == null){
            nativeNewsLogic =  NativeNewsLogic.getNativeNewsLogic();
            nativeNewsLogic.RequestNewsData((MainActivity)getActivity(),NativeNewsFragment.this, mRadioGroupContent, column, viewpagerColumn);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
