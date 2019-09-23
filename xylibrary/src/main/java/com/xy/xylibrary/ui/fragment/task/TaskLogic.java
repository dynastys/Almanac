package com.xy.xylibrary.ui.fragment.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;
import com.xy.xylibrary.Interface.ActiveListener;
import com.xy.xylibrary.base.AppContext;
import com.xy.xylibrary.presenter.PangolinBannerAd;
import com.xy.xylibrary.signin.ActiveValue;
import com.xy.xylibrary.signin.ActiveView;
import com.xy.xylibrary.signin.AdTask;
import com.xy.xylibrary.ui.activity.login.LoginTypeActivity;
import com.xy.xylibrary.utils.GlideUtil;
import com.xy.xylibrary.utils.SaveShare;
import com.xy.xylibrary.utils.UpdateDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.BaseAdapterListener;
import com.xy.xylibrary.base.BaseAdapter;
import com.xy.xylibrary.config.ScrollLinearLayoutManager;
import com.xy.xylibrary.signin.AppSignInList;
import com.xy.xylibrary.signin.AppTaskList;
import com.xy.xylibrary.signin.FinishTask;
import com.xy.xylibrary.signin.SignIn;
import com.xy.xylibrary.signin.StepBean;
import com.xy.xylibrary.signin.StepsView;
import com.xy.xylibrary.ui.activity.login.LoginRequest;
import com.xy.xylibrary.ui.activity.login.RequestSyntony;
import com.xy.xylibrary.ui.activity.login.UserMessage;
import com.xy.xylibrary.utils.ToastUtils;
import com.xy.xylibrary.utils.Utils;
import com.xy.xylibrary.view.AcodeEmojiView;
import com.xy.xylibrary.view.CustomHorizontalProgresNoNum;
import com.zt.xuanyin.Interface.AdProtogenesisListener;
import com.zt.xuanyin.controller.Ad;
import com.zt.xuanyin.controller.NativeAd;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 活动页面逻辑
 */
public class TaskLogic {

    @SuppressLint("StaticFieldLeak")
    private static TaskLogic taskLogic;
    private FragmentActivity context;
    private ArrayList<StepBean> mStepBeans = new ArrayList<>();
    private ArrayList<StepBean> mActiveStepBeans = new ArrayList<>();
    private UserMessage phoneDta;
    private AppSignInList appSignInListData;
    private List<TaskType> dataBeans = new ArrayList<>();
    private TTRewardVideoAd mttRewardVideoAd;
    private String taskID;//翻倍任务id
    private RecyclerView listRecycler;
    private TaskType taskTypeVideo;//激励视频
    private boolean ISVideo = true;
    private BaseAdapter baseAdapter;
    private AppContext.UserGold userGold;//签到接口
    private TaskFragment taskFragment;

    public static TaskLogic getTaskLogic() {
        if (taskLogic == null) {
            synchronized (TaskLogic.class) {
                if (taskLogic == null) {
                    taskLogic = new TaskLogic();
                }
            }
        }
        return taskLogic;
    }

    /**
     * 登录弹出框
     */
    public void LoadDialog(final FragmentActivity context, TaskFragment taskFragment) {
        try {
            new TDialog.Builder(context.getSupportFragmentManager())
                    .setLayoutRes(R.layout.login_dialog)    //设置弹窗展示的xml布局
                    .setScreenWidthAspect(context, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setScreenHeightAspect(context, 0.9f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setCancelableOutside(false)
                    .setOnBindViewListener(new OnBindViewListener() {
                        @Override
                        public void bindView(BindViewHolder viewHolder) {
                            TextView tvTitle = viewHolder.getView(R.id.login_tv);
                            Button hint_content = viewHolder.getView(R.id.login_see_btn);
//                            ImageView index_details_img = viewHolder.getView(R.id.login_interaction_ad);
//                            GlideUtil.getGlideUtil().setGifImages(context, R.drawable.interaction_ad, index_details_img);
                            SpannableString spannableString = new SpannableString("看天气就能免费赚钱，登录即送10000金币。");
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")), 14, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 19, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvTitle.setText(spannableString);
                            ImageView ad_image_banner = viewHolder.getView(R.id.ad_image_banner);
                            FrameLayout banner_container = viewHolder.getView(R.id.banner_container);
                            RelativeLayout GDT_ad = viewHolder.getView(R.id.GDT_ad);
                            RelativeLayout sign_interaction_ad = viewHolder.getView(R.id.sign_interaction_ad);
                            BannerAd("952a6e0d-edc1-484e-bc1a-ebb31fe5e784", ad_image_banner, banner_container, GDT_ad, sign_interaction_ad);
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    })
                    .addOnClickListener(R.id.login_see_btn, R.id.login_img, R.id.sign_interaction_ad)
                    .setOnViewClickListener(taskFragment)
                    .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                    .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                    .create()   //创建TDialog
                    .show();    //展示
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 领取活跃值弹出框
     */
    public void ActiveDialog(final FragmentActivity context, TaskFragment taskFragment, final int gold, final int active) {
        try {
            new TDialog.Builder(context.getSupportFragmentManager())
                    .setLayoutRes(R.layout.login_dialog)    //设置弹窗展示的xml布局
                    .setScreenWidthAspect(context, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setScreenHeightAspect(context, 0.9f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setCancelableOutside(false)
                    .setOnBindViewListener(new OnBindViewListener() {
                        @Override
                        public void bindView(BindViewHolder viewHolder) {
                            TextView tvTitle = viewHolder.getView(R.id.login_tv);
                            Button hint_content = viewHolder.getView(R.id.login_see_btn);
//                            ImageView index_details_img = viewHolder.getView(R.id.login_interaction_ad);
//                            GlideUtil.getGlideUtil().setGifImages(context, R.drawable.interaction_ad, index_details_img);
                            hint_content.setText("继续赚钱");
                            hint_content.setVisibility(View.GONE);
                            if (gold == 0) {
                                if (active > 0) {
                                    tvTitle.setText("已领取，继续完成任务获取金币哦！");
                                } else {
                                    tvTitle.setText("继续完成任务获取金币哦！");
                                }
                            } else {
                                SpannableString spannableString = new SpannableString("获得奖励" + gold + "金币，请继续加油哦！");
                                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")), 4, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tvTitle.setText(spannableString);
                            }
                            ImageView ad_image_banner = viewHolder.getView(R.id.ad_image_banner);
                            FrameLayout banner_container = viewHolder.getView(R.id.banner_container);
                            RelativeLayout GDT_ad = viewHolder.getView(R.id.GDT_ad);
                            RelativeLayout sign_interaction_ad = viewHolder.getView(R.id.sign_interaction_ad);
                            BannerAd("952a6e0d-edc1-484e-bc1a-ebb31fe5e784", ad_image_banner, banner_container, GDT_ad, sign_interaction_ad);
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    })
                    .addOnClickListener(R.id.login_see_btn, R.id.login_img, R.id.sign_interaction_ad)
                    .setOnViewClickListener(taskFragment)
                    .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                    .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                    .create()   //创建TDialog
                    .show();    //展示
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private DialogInterface.OnDismissListener onDismissListener;
    private int goldSize;

    /**
     * 签到弹出框
     */
    public void SignInDialog(final FragmentActivity context, final int goldSize, TaskFragment taskFragment, DialogInterface.OnDismissListener onDismissListener) {
        try {
            this.goldSize = goldSize;
            this.onDismissListener = onDismissListener;
            this.taskFragment = taskFragment;
            new TDialog.Builder(context.getSupportFragmentManager())
                    .setLayoutRes(R.layout.sign_in_dialog)    //设置弹窗展示的xml布局
                    .setScreenWidthAspect(context, 0.8f)   //设置弹窗宽度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setScreenHeightAspect(context, 0.9f)  //设置弹窗高度(参数aspect为屏幕宽度比例 0 - 1f)
                    .setCancelableOutside(false)
                    .setOnBindViewListener(new OnBindViewListener() {
                        @Override
                        public void bindView(BindViewHolder viewHolder) {
                            TextView tvTitle = viewHolder.getView(R.id.sign_in_tv);
                            Button hint_content = viewHolder.getView(R.id.sign_in_see_btn);
                            if (TaskFragment.Multiple == 2) {
                                hint_content.setText("领取3倍金币");
                                String gold = goldSize + "";
                                SpannableString spannableString = new SpannableString("签到红包 +" + gold + " 金币,继续签到可翻倍红包。");
                                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#656565")), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f48421")), 5, 6 + gold.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tvTitle.setText(spannableString);
                            } else {
                                hint_content.setText("领取2倍金币");
                                tvTitle.setText("每天签到，赚钱超简单，赚钱还能翻倍！");
                            }
                            ImageView ad_image_banner = viewHolder.getView(R.id.ad_image_banner);
                            FrameLayout banner_container = viewHolder.getView(R.id.banner_container);
                            RelativeLayout GDT_ad = viewHolder.getView(R.id.GDT_ad);
                            RelativeLayout sign_interaction_ad = viewHolder.getView(R.id.sign_interaction_ad);
                            BannerAd("d6006271-1123-4331-a735-fb14136ef109", ad_image_banner, banner_container, GDT_ad, sign_interaction_ad);
                        }
                    })
                    .setOnDismissListener(onDismissListener)
                    .addOnClickListener(R.id.sign_in_see_btn, R.id.sign_img, R.id.sign_interaction_ad)
                    .setOnViewClickListener(taskFragment)
                    .setDimAmount(0.6f)     //设置弹窗背景透明度(0-1f)
                    .setDialogAnimationRes(R.style.animate_dialog)  //设置弹窗动画
                    .create()   //创建TDialog
                    .show();    //展示
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 任务列表
     */
    public void getAppTaskList(final FragmentActivity context, final RecyclerView listRecycler) {
        try {
            this.context = context;
            this.listRecycler = listRecycler;
            phoneDta = LitePal.findLast(UserMessage.class);
            LoginRequest.getWeatherRequest().getAppTaskListData(context, "", phoneDta == null ? "" : phoneDta.uesrid, "0", new RequestSyntony<AppTaskList>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(AppTaskList appTaskList) {
                    try {
                        if (appTaskList != null && appTaskList.getData() != null && appTaskList.getData().size() > 0) {
                            //                         LitePal.deleteAll(TaskType.class);
                            setTaskList(context, listRecycler, appTaskList.getData());
                            //                       if(TextUtils.isEmpty(SaveShare.getValue(context, "userId"))){
                            //                            return;
                            //                        }
                            dataBeans = LitePal.findAll(TaskType.class);
                            if (dataBeans.size() > 0 && dataBeans.size() == appTaskList.getData().size()) {
                                for (int i = 0; i < dataBeans.size(); i++) {
                                    dataBeans.get(i).taskId = appTaskList.getData().get(i).getId();
                                    dataBeans.get(i).tasksize = appTaskList.getData().get(i).getCompleteNumber();
                                    dataBeans.get(i).taskfinishsize = appTaskList.getData().get(i).getU_CompleteNumber();
                                    dataBeans.get(i).ISfinish = appTaskList.getData().get(i).isU_IsComplete();
                                    dataBeans.get(i).link = appTaskList.getData().get(i).getLink();
                                    dataBeans.get(i).tasktype = appTaskList.getData().get(i).getMultitaskingType();
                                    dataBeans.get(i).IsDouble = appTaskList.getData().get(i).isIsDouble();
                                    dataBeans.get(i).gold = appTaskList.getData().get(i).getShowMinGold();
                                    dataBeans.get(i).CompleteMinTime = appTaskList.getData().get(i).getCompleteMinTime();
                                    if (TextUtils.isEmpty(dataBeans.get(i).time) || !dataBeans.get(i).time.equals(Utils.getOldDate(0))) {
                                        dataBeans.get(i).ISStartTask = false;
                                        dataBeans.get(i).time = Utils.getOldDate(0);
                                    }
                                    //                                dataBeans.add(dataBeans.get(i));
                                    //                            SaveShare.saveValue(context,"TaskId",dataBeans.get(i).taskId);
                                }
                            } else {
                                LitePal.deleteAll(TaskType.class);
                                dataBeans.clear();
                                for (int i = 0; i < appTaskList.getData().size(); i++) {
                                    TaskType taskType = new TaskType();
                                    taskType.taskId = appTaskList.getData().get(i).getId();
                                    taskType.tasksize = appTaskList.getData().get(i).getCompleteNumber();
                                    taskType.taskfinishsize = appTaskList.getData().get(i).getU_CompleteNumber();
                                    taskType.ISfinish = appTaskList.getData().get(i).isU_IsComplete();
                                    //                        taskType.schedule = appTaskList.getData().get(i).getU_CompleteNumber();
                                    taskType.tasktype = appTaskList.getData().get(i).getMultitaskingType();
                                    taskType.IsDouble = appTaskList.getData().get(i).isIsDouble();
                                    Log.e("taskType3", "taskType3.IsDouble: " + taskType.IsDouble);
                                    taskType.gold = appTaskList.getData().get(i).getShowMinGold();
                                    taskType.ISStartTask = false;
                                    taskType.time = Utils.getOldDate(0);
                                    if (dataBeans == null) {
                                        dataBeans = new ArrayList<>();
                                    }
                                    dataBeans.add(taskType);
                                    SaveShare.saveValue(context, "TaskId", taskType.taskId);
                                }
                            }
                            LitePal.saveAll(dataBeans);
                        }
                    } catch (Exception e) {
                        Log.e("Exception", "taskType3.IsDouble: " + e.getMessage().toString());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ISindicate = true;

    public void setTaskList(final FragmentActivity context, RecyclerView listRecycler, final List<AppTaskList.DataBean> beans) {
        try {
            ISindicate = true;
//        getAppTaskList(context,listRecycler);
            ScrollLinearLayoutManager setScrollEnable = new ScrollLinearLayoutManager(context);
            setScrollEnable.setScrollEnable(false);
            listRecycler.setLayoutManager(setScrollEnable);
            listRecycler.addItemDecoration(new DividerItemDecoration(context, 1));
            baseAdapter = new BaseAdapter<>(R.layout.recycler_item_task, beans, new BaseAdapterListener<AppTaskList.DataBean>() {
                @Override
                public void convertView(final BaseViewHolder viewHolder, AppTaskList.DataBean item) {
                    try {
                        if (item.getCompleteNumber() > 1 && item.getU_CompleteNumber() != item.getCompleteNumber()) {
                            CustomHorizontalProgresNoNum horizontalProgress1 = viewHolder.getView(R.id.horizontalProgress1);
                            horizontalProgress1.setVisibility(View.VISIBLE);
                            viewHolder.getView(R.id.receive_btn).setVisibility(View.GONE);
                            horizontalProgress1.setProgress(0);
                            horizontalProgress1.setSchedule(item.getU_CompleteNumber() + "/" + item.getCompleteNumber());
                            horizontalProgress1.setMax(100);
                            horizontalProgress1.setProgress((int) (((double) item.getU_CompleteNumber() / item.getCompleteNumber()) * 100));
                        } else {
//                            ImageView indicate = viewHolder.getView(R.id.indicate_btn);
//                            GlideUtil.getGlideUtil().setGifImages(context, R.drawable.pinkie, indicate);
                            viewHolder.getView(R.id.indicate_btn).setVisibility(View.GONE);
                            final TextView receive_btn = viewHolder.getView(R.id.receive_btn);
                            viewHolder.getView(R.id.receive_btn).setVisibility(View.VISIBLE);
                            viewHolder.getView(R.id.horizontalProgress1).setVisibility(View.GONE);
                            if (item.isU_IsComplete()) {
                                receive_btn.setText("领取完成");
                                receive_btn.setBackground(context.getResources().getDrawable(R.drawable.finish_search_5));
                            } else {
                                if (ISindicate && !TextUtils.isEmpty(SaveShare.getValue(context, "userId"))) {
                                    ISindicate = false;
                                    viewHolder.getView(R.id.indicate_btn).setVisibility(View.VISIBLE);
                                } else {
                                    viewHolder.getView(R.id.indicate_btn).setVisibility(View.GONE);
                                }
                                TaskType taskType = LitePal.where("tasktype = ?", item.getMultitaskingType() + "").findFirst(TaskType.class);
                                if (taskType != null && taskType.ISStartTask) {
                                    receive_btn.setText("立即领取");
                                    receive_btn.setBackground(context.getResources().getDrawable(R.drawable.withdraw_search_5));
                                } else {
                                    receive_btn.setText("立即完成");
                                    receive_btn.setBackground(context.getResources().getDrawable(R.drawable.search_5));
                                }
                            }
                        }
                        viewHolder.setText(R.id.task_title_tv, item.getName());
                        viewHolder.setText(R.id.task_money, "最高+" + item.getShowMaxGold());
                        viewHolder.setText(R.id.task_active, "" + item.getActive());
                        viewHolder.setText(R.id.task_details, item.getDesc());
                        final TextView receive_btn = viewHolder.getView(R.id.receive_btn);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            baseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        final TextView receive_btn = view.findViewById(R.id.receive_btn);
                        AcodeEmojiView acodeEmojiView = view.findViewById(R.id.view_point);
                        final TextView btn3 = view.findViewById(R.id.btn3);
                        acodeEmojiView.addEmoji(btn3);
                        if (TextUtils.isEmpty(SaveShare.getValue(context, "userId"))) {
                            Intent intent1 = new Intent(context, LoginTypeActivity.class);
                            context.startActivity(intent1);
                            return;
                        }
                        if (beans.get(position).isU_IsComplete() && beans.get(position).getU_CompleteNumber() == beans.get(position).getCompleteNumber()) {
                            return;
                        }
                        if (receive_btn.getText().equals("立即领取")) {
                            QuitDialog(context, "", beans.get(position).getId(), receive_btn);
                        } else {
                            TaskType taskType = dataBeans.get(position);
                            taskType.taskId = beans.get(position).getId();
                            switch (beans.get(position).getMultitaskingType()) {
                                case 1://阅读
                                    taskType.tasktype = 1;
                                    break;
                                case 8://下载任务
                                    taskType.tasktype = 8;
                                    taskType.link = beans.get(position).getLink();
                                    break;
                                case 9://跳转任务
                                    taskType.tasktype = 9;
                                    taskType.link = beans.get(position).getLink();
//                                    FinishTask(context, "", beans.get(position).getId(), false);
                                    break;
                                case 11://阅读广告
                                    taskType.tasktype = 11;
                                    break;
                                case 10://deeplink任务
                                    taskType.tasktype = 10;
                                    taskType.link = beans.get(position).getLink();
                                    break;
                                case 3://看天气
                                    taskType.tasktype = 3;
                                    break;
                                case 4://看小视频
                                    taskType.tasktype = 4;
                                    break;
                                case 5://查看7天预报
                                    taskType.tasktype = 5;
                                    taskType.ISStartTask = true;
                                    SaveShare.saveValue(context, "7天预报", "5");
                                    break;
                                case 6://看运势
                                    taskType.tasktype = 6;
                                    break;
                                case 7://看奖励视频
//                                    taskType.tasktype = 7;
                                    taskTypeVideo = taskType;
                                    ISVideo = false;
                                    taskID = taskTypeVideo.taskId;
                                    if (mttRewardVideoAd != null) {
                                        //step6:在获取到广告后展示
                                        mttRewardVideoAd.showRewardVideoAd(context);
                                        mttRewardVideoAd = null;
                                    } else {
                                        ToastUtils.showLong("暂时不能加倍哦");
                                    }
                                    break;
//                                default:
//                                    //跳转更新dialog
//                                    taskType.tasktype = 0;
//                                    break;
                            }
                            EventBus.getDefault().post(taskType);
//                            receive_btn.setText("立即领取");
//                            receive_btn.setBackground(context.getResources().getDrawable(R.drawable.withdraw_search_5));
                        }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });
            listRecycler.setAdapter(baseAdapter);
        } catch (Exception e) {
            Log.e("Exception", "taskType3.IsDouble: " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void QuitDialog(final FragmentActivity context, final String appid, final String id, final TextView receive_btn) {
        try {
            if (mttRewardVideoAd != null) {
                final UpdateDialog confirmDialog = new UpdateDialog(context, "提示", "完成任务", "点击任务翻倍", "点击任务翻倍");
                confirmDialog.show();
                confirmDialog.setClicklistener(new UpdateDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm() {
                        receive_btn.setText("领取完成");
                        receive_btn.setBackground(context.getResources().getDrawable(R.drawable.finish_search_5));
                        confirmDialog.dismiss();
                        FinishTask(context, "", id, false);
                    }

                    @Override
                    public void doCancel() {
                        //                LitePal.deleteAll(WeChat.DataBean.class);
                        receive_btn.setText("领取完成");
                        receive_btn.setBackground(context.getResources().getDrawable(R.drawable.finish_search_5));
                        confirmDialog.dismiss();
                        if (mttRewardVideoAd != null) {
                            ISVideo = true;
                            taskID = id;
                            //step6:在获取到广告后展示
                            mttRewardVideoAd.showRewardVideoAd(context);
                            mttRewardVideoAd = null;
                        } else {
                            ToastUtils.showLong("暂时不能加倍哦");
                        }
                    }
                });
            } else {
                receive_btn.setText("领取完成");
                receive_btn.setBackground(context.getResources().getDrawable(R.drawable.finish_search_5));
                FinishTask(context, "", id, false);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    /*签到翻倍*/
    public void DoubleGold() {
        if (mttRewardVideoAd != null) {
            ISVideo = true;
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(context);
            mttRewardVideoAd = null;
        } else {
            ToastUtils.showLong("暂时不能加倍哦");
        }
    }

    /**
     * 任务完成
     *
     * @param appid
     * @param id       任务id
     * @param isDouble 是否翻倍
     */
    public void FinishTask(final FragmentActivity context, String appid, String id, boolean isDouble) {
        LoginRequest.getWeatherRequest().getFinishTaskData(context, appid, "", id, isDouble, new RequestSyntony<FinishTask>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FinishTask finishTask) {
                try {
                    if (finishTask != null && finishTask.getData() > 0) {
                        ToastUtils.setView(R.layout.toast_show);
                        View view = ToastUtils.getView();
                        ((TextView) view.findViewById(R.id.add_money)).setText("+" + finishTask.getData());
//                        ((ImageView) view.findViewById(R.id.add_money_image)).setText("+" + finishTask.getData());
//                        GlideUtil.getGlideUtil().setGifImages(context,R.drawable.pinkie,(ImageView) view.findViewById(R.id.add_money_image));
                        ToastUtils.showLong("");
//                        ToastUtils.setView(null);
                        EventBus.getDefault().post(new AdTask());
//                        if(listRecycler != null){
//                            getAppTaskList(context, listRecycler);
//                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 任务完成
     *
     * @param appid
     * @param id       任务id
     * @param isDouble 是否翻倍
     */
    public void FinishTask2(final Context context, String appid, String id, boolean isDouble) {
        LoginRequest.getWeatherRequest().getFinishTaskData(context, appid, "", id, isDouble, new RequestSyntony<FinishTask>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FinishTask finishTask) {
                try {
                    if (finishTask != null && finishTask.getData() > 0) {
                        ToastUtils.setView(R.layout.toast_show);
                        View view = ToastUtils.getView();
                        ((TextView) view.findViewById(R.id.add_money)).setText("+" + finishTask.getData());
                        ToastUtils.showLong("");
//                        ToastUtils.setView(null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取APP签到列表
     */
    private void AppSignInData(final Context context, final StepsView mStepView, final SignInRort signInRort) {
        try {
            if (appSignInListData == null) {
                for (int i = 0; i < 7; i++) {
                    mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, "100"));
                }
            } else {
                setSignData(appSignInListData.getData().getSignAtureVms(), mStepView);
            }
            LoginRequest.getWeatherRequest().getAppSignInListData(context, "", phoneDta == null ? "" : phoneDta.uesrid, new RequestSyntony<AppSignInList>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(AppSignInList appSignInList) {
                    if (null != signInRort) {
                        signInRort.SignIn(appSignInList);
                    }
                    if (appSignInList != null && appSignInList.getData() != null) {
                        if (!TextUtils.isEmpty(SaveShare.getValue(context, "userId"))) {
                            appSignInListData = appSignInList;
                            setSignData(appSignInList.getData().getSignAtureVms(), mStepView);
                            SaveShare.saveValue(context, "SignInId", appSignInList.getData().getSignAtureID());
                        } else {
                            setSignData(appSignInList.getData().getSignAtureVms(), mStepView);
                            appSignInListData = null;
                            if (null != signInRort) {
                                signInRort.SignInDefeated();
                            }
                        }

                    } else {
                        appSignInListData = null;
                        if (null != signInRort) {
                            signInRort.SignInDefeated();
                        }
                        setSignData(null, mStepView);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取活跃值
     */
    public void ActiveValueStepViewData(final Context context, final ActiveView ActiveValueStepView, final ActiveListener activeListener) {
        try {
            if (appSignInListData == null) {
                for (int i = 0; i < 2; i++) {
                    mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, ""));
                }
            }
            LoginRequest.getWeatherRequest().getAllActiveRewardsListData(context, new RequestSyntony<ActiveValue>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(ActiveValue activeValue) {
                    if (activeValue != null && activeValue.getData() != null) {
                        setActiveData(activeValue, activeValue.getData().getActiveRewardsVms(), ActiveValueStepView);
                        activeListener.Active(activeValue);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 签到
     */
    public void requestSuccessData(final StepsView mStepView, int Multiple, final AppContext.UserGold userGold) {
        try {
            mStepBeans.clear();//清空初始化数据
            if (TextUtils.isEmpty(SaveShare.getValue(context, "userId"))) {
                Intent intent1 = new Intent(context, LoginTypeActivity.class);
                context.startActivity(intent1);
                return;
            }
            LoginRequest.getWeatherRequest().getSignInData(context, "", Multiple, SaveShare.getValue(context, "userId"), SaveShare.getValue(context, "SignInId"), new RequestSyntony<SignIn>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(SignIn signIn) {
                    try {
                        ToastUtils.setView(R.layout.toast_show);
                        View view = ToastUtils.getView();
                        ((TextView) view.findViewById(R.id.add_money)).setText(signIn.getData() + "");
                        ToastUtils.showLong("");
                        ToastUtils.setView(null);
                        userGold.gold(null);
                        if (mStepView != null) {
                            AppSignInData(context, mStepView, signInRort);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SignInRort signInRort;

    public void initData(StepsView mStepView, SignInRort signInRort) {
        this.signInRort = signInRort;
        AppSignInData(context, mStepView, signInRort);
    }

    /**
     * 活跃值数据处理
     *
     * @param datas
     */
    private void setActiveData(ActiveValue activeValue, List<ActiveValue.DataBean.ActiveRewardsVmsBean> datas, ActiveView ActiveStepView) {
        try {
            //处理已签到的数据
            //先添加已签到的日期到集合中
            mActiveStepBeans.clear();//清空初始化数据
            if (datas != null && datas.size() != 0) {
                for (int i = 0; i < datas.size(); i++) {
                    //判断是否需要显示积分图标，number表示-- 0为不显示积分，非0为显示积分
                    if (datas.get(i).isU_IsComplete()) {
                        mActiveStepBeans.add(new StepBean(StepBean.STEP_COMPLETED, 1, datas.get(i).getActive() + ""));
                    } else {
                        if (activeValue.getData().getUserActive() >= datas.get(i).getActive()) {
                            mActiveStepBeans.add(new StepBean(StepBean.STEP_CURRENT, 0, datas.get(i).getActive() + ""));
                            ActiveStepView.setProgress(mActiveStepBeans);
                        } else {
                            mActiveStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, datas.get(i).getActive() + ""));
                        }
                    }
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    mActiveStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, ""));
                }
            }
            ActiveStepView.setSize(2);
            ActiveStepView.setStepNum(mActiveStepBeans);

//        mStepView.startSignAnimation(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据签到处理
     *
     * @param datas
     */
    private void setSignData(List<AppSignInList.DataBean.SignAtureVmsBean> datas, StepsView mStepView) {

        try {
            //处理已签到的数据
            //先添加已签到的日期到集合中
            mStepBeans.clear();//清空初始化数据
            if (datas != null && datas.size() != 0) {
                for (int i = 0; i < datas.size(); i++) {
                    //判断是否需要显示积分图标，number表示-- 0为不显示积分，非0为显示积分
                    if (datas.get(i).isIsSignAture()) {
                        mStepBeans.add(new StepBean(StepBean.STEP_COMPLETED, 1, datas.get(i).getGold() + ""));
                    } else {
                        mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, datas.get(i).getGold() + ""));
                    }
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    mStepBeans.add(new StepBean(StepBean.STEP_UNDO, 0, ""));
                }
            }
            mStepView.setStepNum(mStepBeans);
//        mStepView.startSignAnimation(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最大天数
     *
     * @return
     */
    public int getDayOfMonth() {
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        return day;
    }

    /**
     * 日月份处理
     *
     * @param day
     * @return
     */
    public String setData(int day) {
        String time = "";
        if (day < 10) {
            time = "0" + day;
        } else {
            time = "" + day;
        }
        return time;
    }

    public void setUserGold(AppContext.UserGold userGold) {
        this.userGold = userGold;
    }

    /**
     * 激励视频广告
     */
    public void loadVideoAd(String codeId, int orientation) {
        try {
            //step1:初始化sdk
            TTAdManager ttAdManager = TTAdSdk.getAdManager();
            //step2:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
            TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
            //step3:创建TTAdNative对象,用于调用广告请求接口
            TTAdNative mTTAdNative = ttAdManager.createAdNative(context);
            //step4:创建广告请求参数AdSlot,具体参数含义参考文档
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(codeId)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .setRewardName("看视频翻倍") //奖励的名称
                    .setRewardAmount(100)  //奖励的数量
                    .setUserID(SaveShare.getValue(context, "Phone"))//用户id,必传参数
                    .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                    .build();
            //step5:请求广告
            mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
                @Override
                public void onError(int code, String message) {
                    Log.e("VideoAd", "onError: ");
                }

                //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
                @Override
                public void onRewardVideoCached() {
                    Log.e("VideoAd", "onRewardVideoCached: ");
                }

                //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
                @Override
                public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                    Log.e("VideoAd", "onRewardVideoAdLoad: ");
                    mttRewardVideoAd = ad;
                    //                mttRewardVideoAd.setShowDownLoadBar(false);
                    mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                        @Override
                        public void onAdShow() {
                            Log.e("VideoAd", "onAdShow: ");
                        }

                        @Override
                        public void onAdVideoBarClick() {
                            Log.e("VideoAd", "onAdVideoBarClick: ");
                        }

                        public void onSkippedVideo() {
                        }

                        @Override
                        public void onAdClose() {
                            try {
                                if (!TextUtils.isEmpty(taskID)) {
                                    FinishTask(context, "", taskID, ISVideo);
                                } else {
                                    if (TaskFragment.Multiple < 3 && onDismissListener != null) {
                                        SignInDialog(context, goldSize, taskFragment, onDismissListener);
                                    } else {
//                                        if(onDismissListener != null){
//                                            onDismissListener.onDismiss(null);
//                                        }
                                        TaskFragment.Multiple = 1;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            loadVideoAd("923044756", TTAdConstant.VERTICAL);
                        }

                        //视频播放完成回调
                        @Override
                        public void onVideoComplete() {
                        }

                        @Override
                        public void onVideoError() {
                            Log.e("VideoAd", "onVideoError: ");
                        }

                        //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                        @Override
                        public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {

                        }
                    });
                    mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NativeAd nativelogic;

    public void BannerAd(String id, final ImageView adImageBanner, final FrameLayout bannerContainer, final RelativeLayout GDTAd, final RelativeLayout adLin) {//
        // 获得原生banner广告对象
        nativelogic = Ad.getAd().NativeAD(context, "98f8e423-02e0-49f5-989f-af46f5c59203", id, "67C53558D3E3485EA681EA21735A5003", new AdProtogenesisListener() {
            @Override
            public void onADReady(com.zt.xuanyin.entity.model.Native url, NativeAd nativelogic) {
                try {
                    if (TextUtils.isEmpty(nativelogic.nativeObject.sdk_code)) {
                        Glide.with(context).load(url.src).into(adImageBanner);
                        adLin.setVisibility(View.VISIBLE);
                        bannerContainer.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("GDT_SDK")) {
                        GDTBanner(GDTAd);
                        bannerContainer.setVisibility(View.GONE);
                        adLin.setVisibility(View.GONE);
                    } else if (nativelogic.nativeObject.sdk_code.equals("TOUTIAO_SDK")) {
                        adLin.setVisibility(View.GONE);
                        GDTAd.setVisibility(View.GONE);
                        PangolinBannerAd.getPangolinBannerAd().BannerAD(context, bannerContainer, nativelogic);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAdFailedToLoad(String error) {
                adLin.setVisibility(View.GONE);
            }
        });
        adImageBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击上报
                nativelogic.OnClick(null);
            }
        });

    }

    private UnifiedBannerView GDTBanner(final RelativeLayout relatAd) {
        UnifiedBannerView banner = null;
        try {
            // 创建 Banner 广告 AdView 对象
            // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
            // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey
            banner = new UnifiedBannerView(context, nativelogic.nativeObject.appid, nativelogic.nativeObject.posid, new UnifiedBannerADListener() {
                @Override
                public void onNoAD(AdError adError) {
                    Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
                    try {
                        relatAd.setVisibility(View.GONE);
                        if (nativelogic != null) {
                            nativelogic.OnRequest(adError.getErrorCode() + "", adError.getErrorMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onADReceive() {
                    try {
                        relatAd.setVisibility(View.VISIBLE);
                        if (nativelogic != null) {
                            nativelogic.OnRequest("0", "msg");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("AD_DEMO", "onADReceive");
                }

                @Override
                public void onADExposure() {
                    Log.i("AD_DEMO", "onADExposure");
                    try {
                        if (nativelogic != null) {
                            nativelogic.AdShow(relatAd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onADClosed() {
                    Log.i("AD_DEMO", "onADClosed");

                }

                @Override
                public void onADClicked() {
                    Log.i("AD_DEMO", "onADClicked");
                    try {
                        if (nativelogic != null) {
                            nativelogic.OnClick(relatAd);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onADLeftApplication() {
                    Log.i("AD_DEMO", "onADLeftApplication");
                }

                @Override
                public void onADOpenOverlay() {

                }

                @Override
                public void onADCloseOverlay() {

                }
            });
            //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
            banner.setRefresh(30);
            relatAd.addView(banner);
            /* 发起广告请求，收到广告数据后会展示数据     */
            banner.loadAD();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return banner;
    }

}
