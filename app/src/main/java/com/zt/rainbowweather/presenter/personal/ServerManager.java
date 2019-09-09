package com.zt.rainbowweather.presenter.personal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zt.rainbowweather.view.tab.SlidingTabLayout;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.base.BaseFragment;
import com.xy.xylibrary.ui.adapter.FragmentPagerAdapter;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.view.CustomScrollViewPager;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.Icons;
import com.zt.rainbowweather.entity.background.PersonalCenterIcon;
import com.zt.rainbowweather.presenter.DataCleanManager;
import com.zt.rainbowweather.presenter.request.BackgroundRequest;
import com.zt.rainbowweather.ui.activity.AdviseMoreDetailActivity;
import com.zt.rainbowweather.ui.fragment.TabServiceFragment;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.rainbowweather.utils.utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerManager {

    private static ServerManager serverManager;
    private BaseAdapter baseAdapter, baseAdapter2;
    private List<String> list2 = new ArrayList<>();
    private List<BaseFragment> mFragment;
     public static ServerManager getServerManager() {
        if (serverManager == null) {
            synchronized (ServerManager.class){
                if (serverManager == null) {
                    serverManager = new ServerManager();
                }
            }
        }
        return serverManager;
    }

    public void FragmentsData(AppCompatActivity context, SlidingTabLayout tablayoutServiceVp, CustomScrollViewPager viewpagerService){
        try {
            BackgroundRequest.getBackgroundRequest().getPersonalCenterIconData(context, "huawei", new RequestSyntony<PersonalCenterIcon>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(PersonalCenterIcon personalCenterIcon) {
                    try {
                        mFragment = new ArrayList<>();
                        String[] mTitles = new String[personalCenterIcon.getData().size()];
                        for (int i = 0; i < personalCenterIcon.getData().size(); i++) {
                            mTitles[i] = personalCenterIcon.getData().get(i).getName();
    //                        tablayoutServiceVp.addTab(tablayoutServiceVp.newTab().setText(personalCenterIcon.getData().get(i).getName()));
                            TabServiceFragment tabServiceFragment = new TabServiceFragment();
                            //传递数据到fragment
                            Bundle data = new Bundle();
                            data.putSerializable("Icon", (Serializable) personalCenterIcon.getData().get(i).getIcons());
                            tabServiceFragment.setArguments(data);
                            tabServiceFragment.setviewPager(viewpagerService);
                            mFragment.add(tabServiceFragment);
                        }

                        viewpagerService.setAdapter(new FragmentPagerAdapter(context.getSupportFragmentManager()) {
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
                                return  personalCenterIcon.getData().get(position).getName();
                            }
                        });
                        viewpagerService.setOffscreenPageLimit(4);
                        tablayoutServiceVp.setViewPager(viewpagerService, mTitles);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setProgramaData(Activity context, TextView serveTxt, Icons serviceList, RecyclerView recyclerService){

        try {
            if (serviceList.getData() == null || serviceList.getData().size() == 0) {
                serveTxt.setVisibility(View.GONE);
                return;
            }
            baseAdapter2 = new BaseAdapter<>(R.layout.popup_recycler_item, serviceList.getData(), (viewHolder, item) -> {
                try {
                    viewHolder.setText(R.id.popup_recycler_tv, item.getTitle());
                    GlideUtil.getGlideUtil().setImages(context, item.getCover(), viewHolder.getView(R.id.popup_recycler_image),20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            baseAdapter2.setOnItemClickListener((adapter, view, position) -> {
                switch (serviceList.getData().get(position).getClickAction()) {
                    case 1://跳转
                        AdviseMoreDetailActivity.startActivity(context, serviceList.getData().get(position).getTitle(), serviceList.getData().get(position).getLink(),"0");
                        break;
                    case 2://下载
                        utils.Download(context, serviceList.getData().get(position).getLink());
                        break;
                    default:
                }
            });
            recyclerService.setAdapter(baseAdapter2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showIOSActionSheetDialog(Context context,TextView cacheTv) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("清除缓存")//设置对话框的标题
                .setMessage("确定要清除缓存吗？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", (dialog1, which) -> dialog1.dismiss())
                .setPositiveButton("确定", (dialog12, which) -> {
                    // String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                    DataCleanManager.clearAllCache(context);
                    try {
                        String cacheSize2 = DataCleanManager.getTotalCacheSize(context);
                        cacheTv.setText("0.0M");
                        ToastUtils.showLong("缓存清除成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog12.dismiss();
                }).create();
        dialog.show();

    }

}
