package com.xy.xylibrary.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.PermissionListener;
import com.xy.xylibrary.utils.ApplyForPermissions;
import com.xy.xylibrary.utils.LoadingDialog;
import com.xy.xylibrary.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * activity基类
 */
public abstract class BaseActivity extends AppCompatActivity implements PermissionListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected static Activity mContext;
    protected boolean IsModel = false;
    protected void process(Bundle savedInstanceState) {
        try {
            // 华为,OPPO机型在StatusBarUtil.setLightStatusBar后布局被顶到状态栏上去了
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
                if (content != null && !isUseFullScreenMode()) {
                    content.setFitsSystemWindows(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 在setContentView之前执行
    public void setStatusBar() {
    /*
     为统一标题栏与状态栏的颜色，我们需要更改状态栏的颜色，而状态栏文字颜色是在android 6.0之后才可以进行更改
     所以统一在6.0之后进行文字状态栏的更改
    */
        try {
            if (isUseFullScreenMode()) {
                StatusBarUtil.transparencyBar(this);
            } else {
                StatusBarUtil.setStatusBarColor(this, setStatusBarColor());
            }
            if (isUserLightMode()) {
                StatusBarUtil.setLightStatusBar(this, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 是否设置成透明状态栏，即就是全屏模式
    protected boolean isUseFullScreenMode() {
        return true;
    }

    protected int setStatusBarColor() {
        return Color.TRANSPARENT;
    }

    // 是否改变状态栏文字颜色为黑色，默认为黑色
    protected boolean isUserLightMode() {
        return IsModel;
    }

    public void setIsUserLightMode(boolean b){
        IsModel = b;
        setStatusBar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
//         mDelegate = new BasicDelegate(mContext, this);
        mContext.setContentView(getContentId());
        //butterknife初始化
        ButterKnife.bind(mContext);
        process(savedInstanceState);
        setStatusBar();
        initView(savedInstanceState);
    }

    @Override
    public void setContentView(View view) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        // 设置一个exit transition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode());
        }
        super.setContentView(view);
    }

    protected abstract Activity getContext();

    @LayoutRes
    protected abstract int getContentId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化界面
     */
    protected void initView(final Bundle savedInstanceState) {
        try {
            loadViewLayout();
            bindViews();
//            //权限申请通过
            processLogic(savedInstanceState);
            setListener();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //权限申请
                ApplyForPermissions.getInstence(mContext).ApplyFor(this);
            }

        } catch (Exception e) {
            Log.e("Exception", "initView: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取控件
     *
     * @param id  控件的id
     * @param <E>
     * @return
     */
    protected <E extends View> E get(int id) {
        return (E) findViewById(id);
    }

    /**
     * 加载布局
     */
    protected abstract void loadViewLayout();

    /**
     * find控件
     */
    protected abstract void bindViews();

    /**
     * 处理数据
     */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 界面跳转
     *
     * @param tarActivity
     */
    protected void intentActivity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(mContext, tarActivity);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        startActivity(intent);
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 申请后的处理
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        List<String> deniedList = new ArrayList<>();
                        // 遍历所有申请的权限，把被拒绝的权限放入集合
                        for (int i = 0; i < grantResults.length; i++) {
                            int grantResult = grantResults[i];
                            if (grantResult == PackageManager.PERMISSION_GRANTED) {

                            } else {
                                deniedList.add(permissions[i]);
                            }
                        }
                        if (!deniedList.isEmpty()) {
                            denied(deniedList);
                        }else{
                          granted();
                        }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private LoadingDialog loadingDialog;

    //加载进度条
    public void showLoadingDialog(String message) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show(message, true, false);
    }

    //取消进度条
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 软键盘展示
     * */
    protected void Show(View submitBt) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 接受软键盘输入的编辑文本或其它视图
        inputMethodManager.showSoftInput(submitBt, InputMethodManager.SHOW_FORCED);
    }

    //关闭输入法
    protected void IMEClose(final EditText txtSearchKey) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSoftInput(txtSearchKey);
            }
        }, 300);
    }

    public boolean hideSoftInput(EditText txtSearchKey) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(txtSearchKey.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
