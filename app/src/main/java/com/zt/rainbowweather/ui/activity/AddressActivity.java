package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chenguang.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.MoveCityEvent;
import com.zt.rainbowweather.entity.city.CitySelect;
import com.zt.rainbowweather.ui.adapter.AddressAdapter;
import com.zt.rainbowweather.ui.fragment.HomeFragment;
import com.zt.rainbowweather.utils.ActivityUtils;
import com.zt.rainbowweather.utils.ConstUtils;
import com.zt.rainbowweather.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.rv_address)
    RecyclerView rvAddress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.fb_add)
    FloatingActionButton fbAdd;
    @BindView(R.id.list_bar)
    TextView listBar;
    private AddressAdapter mAdapter;
    private List<AddressBean> mAddresses;
    private ItemTouchHelper itemTouchHelper;
    private List<City> cities1 = new ArrayList<>();
    private List<City> cities2 = new ArrayList<>();

    public static void startActivity(Context context, List<AddressBean> addressBeans) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("address", (Serializable) addressBeans);
        context.startActivity(intent);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @OnClick(R.id.fb_add)
    public void add() {
        SearchCityActivity.startActivity(AddressActivity.this, mAddresses);
    }

    @Override
    protected Activity getContext() {
        return AddressActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_address;
    }

    private List<City> getCityFromSP() {
        String json = SPUtils.getInstance(ConstUtils.SP_FILE_NAME).getString("addresses");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, new TypeToken<List<City>>() {
            }.getType());
        }
        return null;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(AddressActivity.this);
        listBar.setLayoutParams(layoutParams);
        listBar.setBackgroundColor(getResources().getColor(R.color.blue_light));
        titleBar.setBackgroundColor(getResources().getColor(R.color.blue_light));
        tvTitle.setText("城市管理");
        mAddresses = (List<AddressBean>) getIntent().getSerializableExtra
                ("address");
        cities2 = getCityFromSP();
        fbAdd.startAnimation(AnimationUtils.loadAnimation(AddressActivity.this, R.anim
                .anim_float_button_action));
    }

    @Override
    protected void bindViews() {
        rvAddress.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
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
                if (viewHolder.getAdapterPosition() == 0) {
                    return;
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
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
                if (viewHolder.getAdapterPosition() == 0) {
                    return;
                }
                super.onSwiped(viewHolder, direction);
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
                canvas.drawColor(ContextCompat.getColor(AddressActivity.this, R.color.blue_light));

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
            finish();
            List<City> cities = LitePal.where("name=?", addressBean.city.name).find(City.class);
             ContentValues values=new ContentValues();
            values.put("isChecked","0");
            LitePal.updateAll(City.class, values, "isChecked = ?","1");
            if(cities != null || cities.size() > 0){
                 cities.get(0).isChecked = "1";
                cities.get(0).save();
            }
            List<City> citiesss = LitePal.where("isChecked = ?", "1").find(City.class);
            for (int i = 0; i < citiesss.size(); i++) {
                Log.e("onEvent", "onEvent: "+citiesss.get(i).name );
            }
            ActivityUtils.finishToActivity(MainActivity.class, false);
            EventBus.getDefault().post(new CityEvent(addressBean.city));
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if(!cities1.contains(cities2)){
//            EventBus.getDefault().post(new CityEvent(addressBean.city));
//        }
    }
}
