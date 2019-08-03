package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.AddressBean;
import com.zt.rainbowweather.entity.City;
import com.zt.rainbowweather.entity.CityEvent;
import com.zt.rainbowweather.entity.MoveCityEvent;
import com.zt.rainbowweather.ui.adapter.AddressAdapter;
import com.zt.rainbowweather.utils.ActivityUtils;
import com.zt.rainbowweather.utils.UpdateDialog;
import com.zt.rainbowweather.utils.Util;
import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import java.io.Serializable;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 城市列表页面
 * */
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
    @BindView(R.id.city_icon_add)
    ImageView cityIconAdd;
    @BindView(R.id.address_wether_bg)
    TextView addressWetherBg;
    @BindView(R.id.address_wether_bg_rel)
    RelativeLayout addressWetherBgRel;
    private AddressAdapter mAdapter;
    private List<AddressBean> mAddresses;
    private ItemTouchHelper itemTouchHelper;


    public static void startActivity(Context context, List<AddressBean> addressBeans) {
        Intent intent = new Intent(context, AddressActivity.class);
        intent.putExtra("address", (Serializable) addressBeans);
        context.startActivity(intent);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }

    @OnClick({R.id.fb_add, R.id.city_icon_add})
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



    @Override
    protected void loadViewLayout() {
        cityIconAdd.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(AddressActivity.this);
        listBar.setLayoutParams(layoutParams);
        tvTitle.setText("城市管理");
        mAddresses = (List<AddressBean>) getIntent().getSerializableExtra
                ("address");
        fbAdd.startAnimation(AnimationUtils.loadAnimation(AddressActivity.this, R.anim
                .anim_float_button_action));
        Drawable resource = SaveShare.getDrawable(AddressActivity.this, "icon");
        if(resource != null){
            addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(AddressActivity.this, Util.drawable2Bitmap(resource), 20)));
        }else{
            addressWetherBgRel.setBackground(new BitmapDrawable(Util.rsBlur(AddressActivity.this, Util.drawable2Bitmap(addressWetherBgRel.getBackground()), 20)));
        }
    }

    @Override
    protected void bindViews() {
        try {
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
                        final UpdateDialog confirmDialog = new UpdateDialog(mContext, "提示", "删除","放弃","是否删除该城市？");
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
                    canvas.drawColor(ContextCompat.getColor(AddressActivity.this, R.color.text_black));
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
                ActivityUtils.finishToActivity(MainActivity.class, false);
                EventBus.getDefault().post(addressBean.city);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AddressActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AddressActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
}
