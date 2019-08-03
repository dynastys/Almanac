package com.zt.rainbowweather.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zt.rainbowweather.utils.ToastUtils;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Shares;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.api.RequestSyntony;
import com.zt.rainbowweather.entity.calendar.DanXiangLi;
import com.zt.rainbowweather.entity.calendar.HuangLi;
import com.zt.rainbowweather.presenter.request.AlmanacRequest;
import com.zt.rainbowweather.utils.Util;
import com.zt.rainbowweather.view.ChangeTextViewSpace;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zw
 * @time 2019-3-8
 * 单向历页面
 * */
public class DXiangLiActivity extends BaseActivity implements RequestSyntony<HuangLi> {

    @BindView(R.id.list_bar)
    TextView listBar;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;
    @BindView(R.id.dxiangli_sui_id)
    TextView dxiangliSuiId;
    @BindView(R.id.dxiangli_year_lin)
    LinearLayout dxiangliYearLin;
    @BindView(R.id.dxiangli_m)
    TextView dxiangliM;
    @BindView(R.id.dxiangli_tv)
    ChangeTextViewSpace dxiangliTv;
    @BindView(R.id.dxiangli_week)
    TextView dxiangliWeek;
    @BindView(R.id.dxiangli_NongLiMonthDay)
    TextView dxiangliNongLiMonthDay;
    @BindView(R.id.dxiangli_text)
    TextView dxiangliText;
    @BindView(R.id.danxiangli_tv)
    TextView danxiangliTv;
    @BindView(R.id.danxiangli_author)
    TextView danxiangliAuthor;
    @BindView(R.id.danxiangli_author_production)
    TextView danxiangliAuthorProduction;
    @BindView(R.id.dxiangli_lin)
    LinearLayout dxiangliLin;

    private DanXiangLi danXiangLi;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Uri bitmap = Shares.localshare(DXiangLiActivity.this, "aaa", dxiangliLin,true);
            if(bitmap != null){
                ShareActivity.startActivity(DXiangLiActivity.this, bitmap);
            } else{
                dismissLoadingDialog();
                ToastUtils.showLong("分享失败");
            }
        }
    };

    public static void startActivity(Activity context, DanXiangLi danXiangLi) {
        Intent intent = new Intent(context, DXiangLiActivity.class);
        intent.putExtra("danXiangLi", danXiangLi);
        context.overridePendingTransition(com.constellation.xylibrary.R.anim.in_from_right, com.constellation.xylibrary.R.anim.out_to_left);
        context.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            MobclickAgent.onPageStart("DXiangLiActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
            MobclickAgent.onResume(this); //统计时长
            dismissLoadingDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("DXiangLiActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }

    @Override
    protected Activity getContext() {
        return DXiangLiActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_dxiang_li;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void loadViewLayout() {
        try {
            MobclickAgent.onEvent(DXiangLiActivity.this,"danxiangli");
            danXiangLi = (DanXiangLi) getIntent().getSerializableExtra("danXiangLi");
            ViewGroup.LayoutParams layoutParams = listBar.getLayoutParams();
            layoutParams.height = Utils.getStatusBarHeight(DXiangLiActivity.this);
            listBar.setLayoutParams(layoutParams);
            finishFileHead.setVisibility(View.VISIBLE);
            fileCoOption.setText("分享");
            fileHeadTitle.setText("单向历");
            if (danXiangLi == null) {
                AlmanacRequest.getAlmanacRequest().getDanXiangLiData(this, Utils.getOldDate(0), new RequestSyntony<DanXiangLi>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onNext(DanXiangLi danXiangLi) {
                        dxiangliText.setText(danXiangLi.getData().getContent());
                        danxiangliTv.setText("\u3000\u3000" + danXiangLi.getData().getExtract());
                        danxiangliAuthor.setText(danXiangLi.getData().getAuthor_type() + " " + danXiangLi.getData().getAuthor_name());
                        danxiangliAuthorProduction.setText("《" + danXiangLi.getData().getProduction() + "》");
                    }
                });
                AlmanacRequest.getAlmanacRequest().getHuangLiData(this, Utils.getOldDate(0), DXiangLiActivity.this);
            } else {
                dxiangliSuiId.setText(danXiangLi.getData().sui_ci_shengxiao);
                dxiangliM.setText(danXiangLi.getData().month);
                dxiangliTv.setSpacing(10);
                dxiangliTv.setText(danXiangLi.getData().monthY);
                dxiangliWeek.setText(danXiangLi.getData().week);
                dxiangliNongLiMonthDay.setText(danXiangLi.getData().NongLiDay);
                dxiangliText.setText(danXiangLi.getData().getContent());
                danxiangliTv.setText("\u3000\u3000" + danXiangLi.getData().getExtract());
                danxiangliAuthor.setText(danXiangLi.getData().getAuthor_type() + " " + danXiangLi.getData().getAuthor_name());
                danxiangliAuthorProduction.setText("《" + danXiangLi.getData().getProduction() + "》");
            }
        } catch (Exception e) {
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
    public void granted() {
    }

    @Override
    public void denied(List<String> deniedList) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }


    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onNext(HuangLi huangLi) {
        dxiangliSuiId.setText(huangLi.getData().getSui_ci().get(1) + " 【" + huangLi.getData().getShengXiao() + "】 " + huangLi.getData().getSui_ci().get(2) + " " + huangLi.getData().getSui_ci().get(0));
        dxiangliM.setText(Utils.getMonth(2) + "月");
        dxiangliTv.setSpacing(10);
        dxiangliTv.setText(Util.MonthEnglish(Utils.getMonth(2)));
        dxiangliWeek.setText("周" + huangLi.getData().getWeek());
        dxiangliNongLiMonthDay.setText(Utils.getMonth(3));

    }

    @OnClick({R.id.finish_file_head, R.id.file_co_option})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish_file_head:
                finish();
                break;
            case R.id.file_co_option:
                if (dxiangliLin != null) {
                    showLoadingDialog("");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                break;
        }
    }
}
