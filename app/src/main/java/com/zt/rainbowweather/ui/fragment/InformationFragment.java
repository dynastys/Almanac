package com.zt.rainbowweather.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.ui.adapter.FragmentPagerAdapter;
import com.zt.rainbowweather.entity.background.IsUserLight;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 * @author zw
 * @time 2019-3-8
 * */
public class InformationFragment extends BaseFragment {

    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.tablayout_vp)
    TabLayout tablayoutVp;
    @BindView(R.id.viewpager_atlas)
    ViewPager viewpagerAtlas;
    @BindView(R.id.list_bar)
    TextView listBar;

    private List<BaseFragment> mFragment;
    private String[] title = new String[]{"视频","资讯"};

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_atlas;
    }

    @Override
    protected void initData(View view) {
        tablayoutVp.addTab(tablayoutVp.newTab().setText("视频"));
        tablayoutVp.addTab(tablayoutVp.newTab().setText("资讯"));
        mFragment = new ArrayList<>();
        mFragment.add(new RecommendFragment());
        mFragment.add(new ClassifyFragment());
        viewpagerAtlas.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public BaseFragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tablayoutVp.setupWithViewPager(viewpagerAtlas);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            EventBus.getDefault().post(new IsUserLight(false));
        }
    }

    @Override
    protected void initListener() {

    }
}
