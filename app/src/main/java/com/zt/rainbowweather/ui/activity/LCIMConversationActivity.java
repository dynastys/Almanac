package com.zt.rainbowweather.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.umeng.analytics.MobclickAgent;
import com.zt.weather.R;
import com.xy.xylibrary.base.BaseActivity;
import com.xy.xylibrary.utils.Utils;
import com.zt.rainbowweather.feedback.LCIMConversationFragment;
import com.zt.rainbowweather.presenter.PreferencesHelper;
import com.zt.rainbowweather.utils.ToastUtils;
import java.util.Arrays;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.utils.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

/**
 * @author zw
 * @time 2019-3-8
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 */
public class LCIMConversationActivity extends BaseActivity {

    protected LCIMConversationFragment conversationFragment;
    @BindView(R.id.list_bar)
    TextView statusBarList;
    @BindView(R.id.finish_file_head)
    ImageView finishFileHead;
    @BindView(R.id.file_head_title)
    TextView fileHeadTitle;
    @BindView(R.id.file_co_option)
    TextView fileCoOption;

    private MaterialDialog materialDialog;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LCIMConversationActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this); //统计时长
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LCIMConversationActivity"); //手动统计页面("SplashScreen"为页面名称，可自定义)，必须保证 onPageEnd 在 onPause 之前调用，因为SDK会在 onPause 中保存onPageEnd统计到的页面数据。
        MobclickAgent.onPause(this);
    }
    public void showCollectionDialog(DialogInterface.OnDismissListener onDismissListener) {
        new MaterialDialog
                .Builder(this)
                .title("提示")
                .content(getResources().getString(R.string.app_name))
//                .positiveText("加入")
//                .onPositive(singleButtonCallback)
                .negativeText("确定")
                .dismissListener(onDismissListener)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        try {
//            //提示
//            showCollectionDialog(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    dialog.dismiss();
//                }
//            });
            conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
            initByIntent(getIntent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Activity getContext() {
        return LCIMConversationActivity.this;
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_lcimconversation;
    }

    @Override
    protected void loadViewLayout() {
        ViewGroup.LayoutParams layoutParams = statusBarList.getLayoutParams();
        layoutParams.height = Utils.getStatusBarHeight(LCIMConversationActivity.this);
        statusBarList.setLayoutParams(layoutParams);
        finishFileHead.setVisibility(View.VISIBLE);
        statusBarList.setVisibility(View.GONE);
        fileCoOption.setText("留下足迹");
        fileHeadTitle.setText("意见反馈");
        fileCoOption.setVisibility(View.VISIBLE);
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        if (null == LCChatKit.getInstance().getClient()) {
            showToast("please login first!");
            finish();
            return;
        }
        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(LCIMConstants.PEER_ID)) {
                getConversation(extras.getString(LCIMConstants.PEER_ID));
            } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
                String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
                updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
            } else {
                showToast("memberId or conversationId is needed");
                finish();
            }
        }
    }

    /**
     * 设置 actionBar title 以及 up 按钮事件
     *
     * @param title
     */
    protected void initActionBar(String title) {
        try {
            ActionBar actionBar = getSupportActionBar();
            if (null != actionBar) {
                if (null != title) {
                    actionBar.setTitle(title);
                }
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                finishActivity(RESULT_OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 主动刷新 UI
     *
     * @param conversation
     */
    protected void updateConversation(AVIMConversation conversation) {
        if (null != conversation) {
            try {
                conversationFragment.setConversation(conversation);
                LCIMConversationItemCache.getInstance().insertConversation(conversation.getConversationId());
                LCIMConversationUtils.getConversationName(conversation, new AVCallback<String>() {
                    @Override
                    protected void internalDone0(String s, AVException e) {
                        if (null != e) {
                            LCIMLogUtils.logException(e);
                        } else {
                            initActionBar(s);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取 conversation
     * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
     */
    protected void getConversation(final String memberId) {
        LCChatKit.getInstance().getClient().createConversation(
                Arrays.asList(memberId), "我", null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (null != e) {
                            showToast(e.getMessage());
                        } else {
                            updateConversation(avimConversation);
                        }
                    }
                });
    }

    /**
     * 弹出 toast
     *
     * @param content
     */
    protected void showToast(String content) {
        Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
    }

    private void showContactDialog(final boolean isBackPress) {
        if (materialDialog == null) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title("联系方式")
                    .content("您的反馈对我们实在太重要了，留下您的联系方式吧。")
                    .inputType(
                            InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                    | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .positiveText("确定")
                    .negativeText("取消")
                    .negativeColor(ContextCompat.getColor(this, R.color.textAssist))
                    .autoDismiss(false)
                    .onNegative((dialog, which) -> dialog.dismiss())
                    .input(
                            "手机号、QQ或者邮箱",
                            PreferencesHelper.getInstance().getFeedbackContact(),
                            true,
                            (dialog, input) -> {
                                if (TextUtils.isEmpty(input)) {
                                    dialog.dismiss();
                                } else {
                                    if (materialDialog != null) {
                                        materialDialog.dismiss();
                                        ToastUtils.showLong("设置联系方式成功");

                                    }
                                }
                            })
                    .dismissListener(dialog -> {
                        if (isBackPress) {
                            onBackPressed();
                        }
                    });
            if (isBackPress) {
                builder = builder
                        .neutralText("不再弹出")
                        .neutralColor(ContextCompat.getColor(this, R.color.textAssistSecondary))
                        .onNeutral((dialog, which) -> {
                            dialog.dismiss();
                            PreferencesHelper.getInstance().setCanShowFeedbackContactDialog(false);
                            ToastUtils.showLong("放心，我下次不会在弹了");

                        });
            }
            materialDialog = builder.build();
        }
        materialDialog.show();
    }


    @OnClick({R.id.finish_file_head, R.id.file_co_option})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_file_head:
                finish();
                break;
            case R.id.file_co_option:
                showContactDialog(false);
                break;
        }
    }

    @Override
    public void granted() {

    }

    @Override
    public void denied(List<String> deniedList) {

    }
}
