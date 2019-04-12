package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenguang.weather.R;
import com.umeng.message.PushAgent;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.entity.advise.AdviseTitleBean;
import com.zt.rainbowweather.entity.weather.ConventionWeather;
import com.zt.rainbowweather.ui.adapter.CustomAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexOfLivingActivity extends BaseActivity {

    @BindView(R.id.select)
    ImageView select;
    @BindView(R.id.head)
    RelativeLayout head;
    @BindView(R.id.functionset_grid)
    GridView functionsetGrid;
    @BindView(R.id.list_bar)
    TextView listBar;

    private List<ConventionWeather.HeWeather6Bean.LifestyleBean> mList = new ArrayList<>();
    private int[] ID = new int[]{26,28,29,30,31,32,27,33,34,35,36,37,38,40,39,32};
    private int[] icon = new int[]{R.mipmap.comf,R.mipmap.drsg,R.mipmap.flu,R.mipmap.sport,R.mipmap.trav,R.mipmap.uv,R.mipmap.cw,R.mipmap.air,R.mipmap.ac,R.mipmap.ag,R.mipmap.gl,R.mipmap.mu,R.mipmap.airc,R.mipmap.ptfc,R.mipmap.fsh,R.mipmap.spi};

    public static void startActivity(Context context, List<ConventionWeather.HeWeather6Bean.LifestyleBean> addressBeans) {
        Intent intent = new Intent(context, IndexOfLivingActivity.class);
        intent.putExtra("index", (Serializable) addressBeans);
        context.startActivity(intent);
    }


    @Override
    protected Activity getContext() {
        return IndexOfLivingActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.functionset_choose;
    }

    @Override
    protected void loadViewLayout() {
        try {
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(IndexOfLivingActivity.this);
            listBar.setLayoutParams(layoutParams);
            listBar.setBackgroundColor(getResources().getColor(R.color.blue_light));
            mList = (List<ConventionWeather.HeWeather6Bean.LifestyleBean>) getIntent().getSerializableExtra
                    ("index");
            for (int i = 0; i < mList.size(); i++) {
                switch (mList.get(i).getType()){
                    case "comf":
                        mList.get(i).setType("舒适度指数");
                        break;
                    case "cw":
                        mList.get(i).setType("洗车指数");
                        break;
                    case "drsg":
                        mList.get(i).setType("穿衣指数");
                        break;
                    case "flu":
                        mList.get(i).setType("感冒指数");
                        break;
                    case "sport":
                        mList.get(i).setType("运动指数");
                        break;
                    case "trav":
                        mList.get(i).setType("旅游指数");
                        break;
                    case "uv":
                        mList.get(i).setType("紫外线指数");
                        break;
                    case "air":
                        mList.get(i).setType("空气污染扩散条件指数");
                        break;
                    case "ac":
                        mList.get(i).setType("空调开启指数");
                        break;
                    case "ag":
                        mList.get(i).setType("过敏指数");
                        break;
                    case "gl":
                        mList.get(i).setType("太阳镜指数");
                        break;
                    case "mu":
                        mList.get(i).setType("化妆指数");
                        break;
                    case "airc":
                        mList.get(i).setType("晾晒指数");
                        break;
                    case "ptfc":
                        mList.get(i).setType("交通指数");
                        break;
                    case "fsh":
                        mList.get(i).setType("钓鱼指数");
                        break;
                    case "spi":
                        mList.get(i).setType("防晒指数");
                        break;
                }
            }

            CustomAdapter mAdapter = new CustomAdapter(IndexOfLivingActivity.this,
                    R.layout.function_item, mList, functionsetGrid,icon);
            functionsetGrid.setAdapter(mAdapter);
            functionsetGrid.setOnItemClickListener((parent, view, position, id) -> {
                AdviseTitleBean adviseTitleBean = new AdviseTitleBean();
                adviseTitleBean.contentUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
                adviseTitleBean.headerImgUrl = "https://sec-cdn.static.xiaomi.net/secStatic/imgs/b769c44e846b6f0fed3c9780d0bf431aae425f02.png";
                adviseTitleBean.imgUrl = "http://f4.market.xiaomi.com/download/MiSafe/001a2c4210f6944e83427fd96c3216666b4de8646/a.webp";
                adviseTitleBean.title = mList.get(position).getBrf();
                adviseTitleBean.headerSummary = mList.get(position).getTxt();
                adviseTitleBean.channelId = mList.get(position).getType();
                adviseTitleBean.indexId = "" + ID[position];
                AdviseDetailActivity.startActivity(IndexOfLivingActivity.this, adviseTitleBean);
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void bindViews() {

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

    @OnClick(R.id.select)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
