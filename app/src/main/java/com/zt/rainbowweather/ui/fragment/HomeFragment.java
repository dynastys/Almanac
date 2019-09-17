package com.zt.rainbowweather.ui.fragment;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.umeng.analytics.MobclickAgent;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Shares;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.MoveCityEvent;
import com.zt.rainbowweather.entity.background.IsUserLight;
import com.zt.rainbowweather.entity.city.CityX;
import com.zt.rainbowweather.entity.city.Event;
import com.zt.rainbowweather.entity.city.Refresh;
import com.zt.rainbowweather.entity.city.Share;
import com.zt.rainbowweather.presenter.home.CityWeatherQuantity;
import com.zt.rainbowweather.presenter.home.OnPageChangeListener;
import com.zt.rainbowweather.presenter.map.MapLocation;
import com.zt.rainbowweather.ui.activity.MainActivity;
import com.zt.rainbowweather.ui.activity.SearchCityActivity;
import com.zt.rainbowweather.ui.activity.ShareActivity;
import com.zt.rainbowweather.ui.adapter.AddressAdapter;
import com.zt.rainbowweather.utils.ActivityUtils;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.utils.UpdateDialog;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.view.NoScrollViewPager;
import com.zt.weather.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zw
 * @time 2019-3-8
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnPageChangeListener {

    Unbinder unbinder;
    @BindView(R.id.view_pager)
    NoScrollViewPager viewPager;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.rb_main)
    RadioGroup rbMain;
    @BindView(R.id.ll_main_indicator)
    LinearLayout llMainIndicator;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.actionBarSize)
    TextView actionBarSize;
    @BindView(R.id.name_city)
    TextView nameCity;
//    @BindView(R.id.drawerlayout)
   private static DrawerLayout drawerlayout;
    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.city_icon_add)
    ImageView cityIconAdd;
    @BindView(R.id.address_wether_bg_rel)
    LinearLayout addressWetherBgRel;
    @BindView(R.id.city_iv_search_flag)
    ImageView cityIvSearchFlag;
    @BindView(R.id.city_keyword)
    TextView cityKeyword;
    @BindView(R.id.city_view_search)
    RelativeLayout cityViewSearch;
    @BindView(R.id.add_city)
    TextView addCity;
    @BindView(R.id.city_address_f)
    FrameLayout cityAddressF;

    private AddressAdapter mAdapter;
    private List<AddressBean> mAddresses;
    private ItemTouchHelper itemTouchHelper;
    private CityWeatherQuantity cityWeatherQuantity;
    private List<City> cities = new ArrayList<>();
    private boolean ISOPEN = true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Uri bitmap = Shares.localshare(getActivity(), "aaa", null, true);
                if (bitmap != null) {
                    ShareActivity.startActivity(getActivity(), bitmap);

                }else{
                    dismissLoadingDialog();
                    ToastUtils.showLong("分享失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerlayout.isDrawerOpen(Gravity.START)) {
                drawerlayout.closeDrawer(Gravity.START);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData(View view) {
        try {
            cities = MapLocation.getCitys();
            if (cities == null) {
                cities = LitePal.findAll(City.class);
            }
            drawerlayout = view.findViewById(R.id.drawerlayout);
            ivBack.setVisibility(View.GONE);
            ViewGroup.LayoutParams layoutParams = actionBarSize.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(getActivity());
            actionBarSize.setLayoutParams(layoutParams);
            cityWeatherQuantity = new CityWeatherQuantity(getActivity(), HomeFragment.this, viewPager, rbMain);
            cityWeatherQuantity.PageSize(HomeFragment.this);
            nameCity.setText(cities.get(0).affiliation);
//            drawerlayout.openDrawer(Gravity.START);
            drawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            cityIconAdd.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams1 = listBar.getLayoutParams();
            layoutParams1.height = Utils.getStatusBarHeight(getActivity());
            listBar.setLayoutParams(layoutParams1);
            tvTitle.setText("城市管理");
            Drawable resource = null;
            if(cities != null && cities.size()>0){
                resource = SaveShare.getDrawable(getActivity(), cities.get(0).name);
            }
            if (resource != null) {
                addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(resource), 20)));
            } else {
                addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(addressWetherBgRel.getBackground()), 20)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void bindViews() {
        try {
            Drawable resource = null;
            if(cities != null && cities.size()>0){
                resource = SaveShare.getDrawable(getActivity(), cities.get(0).name);
            }
            if (resource != null) {
                addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(resource), 20)));
            } else {
                addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(getActivity(), Util.drawable2Bitmap(addressWetherBgRel.getBackground()), 20)));
            }
            mAddresses = cityWeatherQuantity.getAllAddresses();
            rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new AddressAdapter(mAddresses);
            ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter) {
                /*
                屏蔽第一项的拖拽
                 */
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                                      RecyclerView.ViewHolder target) {
                    if (source.getLayoutPosition() == 0 || target.getLayoutPosition() == 0) {
                        return false;
                    }
                    return super.onMove(recyclerView, source, target);
                }

                /*
                屏蔽第一项的侧滑
                 */
                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder
                        viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                    if (viewHolder.getAdapterPosition() == 0) {
                    return;
//                    }
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
//                            isCurrentlyActive);
                }

                @Override
                public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView
                        .ViewHolder viewHolder, float dX, float dY, int actionState, boolean
                                                    isCurrentlyActive) {
                    if (viewHolder.getAdapterPosition() == 0) {
                        return;
                    }
                    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState,
                            isCurrentlyActive);
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                    if (viewHolder.getAdapterPosition() == 0) {
                    return;
//                    }
//                    super.onSwiped(viewHolder, direction);
                }
            };
            itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
            itemTouchHelper.attachToRecyclerView(rvAddress);
            itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
            itemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN);
            mAdapter.enableSwipeItem();
            mAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                    try {
                        if (pos == 0) {
                            return;
                        }
//                     mAdapter.disableSwipeItem();
                        final UpdateDialog confirmDialog = new UpdateDialog(getActivity(), "提示", "删除", "放弃", "是否删除该城市？");
                        confirmDialog.show();
                        confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
                            @Override
                            public void doConfirm() {
                                //                            mAdapter.enableSwipeItem()
                                try {
                                    AddressBean addressBean = mAdapter.getData().get(pos);
                                    mAdapter.remove(pos);
                                    CityEvent event = new CityEvent(addressBean.city);
                                    event.isDelete = true;
                                    EventBus.getDefault().post(event);
                                    LitePal.deleteAll(City.class, "name =?", addressBean.city.name);
                                    confirmDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void doCancel() {
                                mAdapter.notifyDataSetChanged();
                                confirmDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

                }

                @Override
                public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                    AddressBean addressBean = mAdapter.getData().get(pos);
                    CityEvent event = new CityEvent(addressBean.city);
                    event.isDelete = true;
                    EventBus.getDefault().post(event);
                    LitePal.deleteAll(City.class, "name =?", addressBean.city.name);
                }

                @Override
                public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder,
                                              float dX, float dY, boolean isCurrentlyActive) {
                    canvas.drawColor(ContextCompat.getColor(getActivity(), R.color.text_black));
                }
            });
            mAdapter.enableDragItem(itemTouchHelper);
            mAdapter.setOnItemDragListener(new OnItemDragListener() {
                @Override
                public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                }

                @Override
                public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView
                        .ViewHolder target, int to) {
                    EventBus.getDefault().post(new MoveCityEvent(mAdapter.getData().get(from).city,
                            mAdapter.getData().get(to).city));
                }

                @Override
                public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                }
            });
            rvAddress.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                AddressBean addressBean = (AddressBean) adapter.getData().get(position);
                drawerlayout.closeDrawer(Gravity.START);
                ActivityUtils.finishToActivity(MainActivity.class, false);
                EventBus.getDefault().post(addressBean.city);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && ISOPEN) {
            ISOPEN = false;
            MobclickAgent.onEvent(getActivity(), "home");
            EventBus.getDefault().post(new IsUserLight(false));

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        cityWeatherQuantity.updateWeatherFragment(event);
        if (!event.isDelete) {
            nameCity.setText(event.city.name);
            cities = cityWeatherQuantity.getAllCities();
            cities.add(event.city);
        } else {
            cities = cityWeatherQuantity.setFragments();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCityX(CityX cityX) {
        cityWeatherQuantity.WeatherXz(cityX);
        cities = cityWeatherQuantity.getAllCities();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Refresh event) {
        if (event.overscrollTop == 0) {
            llBottom.setVisibility(View.VISIBLE);
        } else {
            llBottom.setVisibility(View.GONE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void moveCityPosition(MoveCityEvent event) {
        cityWeatherQuantity.setMoveFragmentPosition(event);
        cities = cityWeatherQuantity.setFragments();
    }

    @Override
    protected void initListener() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private long exitTime = 0;


    @OnClick({R.id.iv_add, R.id.iv_more, R.id.city_icon_add, R.id.add_city, R.id.city_view_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                try {
                    if (drawerlayout.isDrawerOpen(Gravity.START)) {
                        drawerlayout.closeDrawer(Gravity.START);
                    } else {
                        if(mAddresses ==null || mAddresses.size() == 0){
                            mAddresses = cityWeatherQuantity.getAllAddresses();
                        }
                        bindViews();
                        drawerlayout.openDrawer(Gravity.START);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_more:
                showLoadingDialog("");
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    try {
                        if (viewPager != null) {
                            EventBus.getDefault().post(new Share(true));
                            MobclickAgent.onEvent(getActivity(), "share");
                            new Thread(() -> {
                                try {
                                    handler.sendEmptyMessage(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();

                            exitTime = System.currentTimeMillis();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.city_view_search:
            case R.id.city_icon_add:
            case R.id.add_city:
                drawerlayout.closeDrawer(Gravity.START);
                SearchCityActivity.startActivity(getActivity(), mAddresses);
                break;

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (position == 0) {
                nameCity.setText(cities.get(position).affiliation);
            } else {
                nameCity.setText(cities.get(position).name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {
        llBottom.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCanSlipping(boolean enable) {
        try {
            viewPager.setNoScroll(enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(cityWeatherQuantity != null){
            cityWeatherQuantity.saveCityToSp();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment"); //统计页面("MainScreen"为页面名称，可自定义)
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");
        try {
            dismissLoadingDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
