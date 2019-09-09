package com.xy.xylibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public final class ToastUtils {

    private static Toast sToast;
    private static int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private static int xOffset = 0;
    private static int yOffset = (int) (64 * Utils.getContext().getResources().getDisplayMetrics
            ().density + 0.5);
    @SuppressLint("StaticFieldLeak")
    private static View customView;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 设置吐司view
     *
     * @param layoutId 视图
     */
    public static void setView(@LayoutRes int layoutId) {
        try {
            LayoutInflater inflate = (LayoutInflater) Utils.getContext().getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            ToastUtils.customView = inflate.inflate(layoutId, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置吐司view
     *
     * @param view 视图
     */
    public static void setView(View view) {
        try {
            ToastUtils.customView = view;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取吐司view
     *
     * @return view 自定义view
     */
    public static View getView() {
        try {
            if (customView != null) return customView;
            if (sToast != null) return sToast.getView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 安全地显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShortSafe(final @StringRes int resId, final Object... args) {
        try {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    show(resId, Toast.LENGTH_SHORT, args);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安全地显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShortSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(format, Toast.LENGTH_SHORT, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param text 文本
     */
    public static void showLongSafe(final CharSequence text) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(text, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLongSafe(final @StringRes int resId) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLongSafe(final @StringRes int resId, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(resId, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 安全地显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLongSafe(final String format, final Object... args) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                show(format, Toast.LENGTH_LONG, args);
            }
        });
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    public static void showShort(CharSequence text) {
        try {
            show(text, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     */
    public static void showShort(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 显示短时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showShort(@StringRes int resId, Object... args) {
        show(resId, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示短时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showShort(String format, Object... args) {
        show(format, Toast.LENGTH_SHORT, args);
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    public static void showLong(CharSequence text) {
        try {
            if(!TextUtils.isEmpty(text)){
                customView = null;
            }
            show(text, Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     */
    public static void showLong(@StringRes int resId) {
        show(resId, Toast.LENGTH_LONG);
    }

    /**
     * 显示长时吐司
     *
     * @param resId 资源Id
     * @param args  参数
     */
    public static void showLong(@StringRes int resId, Object... args) {
        show(resId, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示长时吐司
     *
     * @param format 格式
     * @param args   参数
     */
    public static void showLong(String format, Object... args) {
        show(format, Toast.LENGTH_LONG, args);
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     */
    private static void show(@StringRes int resId, int duration) {
        try {
            show(Utils.getContext().getResources().getText(resId).toString(), duration);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示吐司
     *
     * @param resId    资源Id
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(@StringRes int resId, int duration, Object... args) {
        try {
            show(String.format(Utils.getContext().getResources().getString(resId), args), duration);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示吐司
     *
     * @param format   格式
     * @param duration 显示时长
     * @param args     参数
     */
    private static void show(String format, int duration, Object... args) {
        try {
            show(String.format(format, args), duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    private static void show(CharSequence text, int duration) {
        try {
            cancel();
            if (customView != null) {
                sToast = new Toast(Utils.getContext());
                sToast.setView(customView);
                sToast.setDuration(duration);
            } else {
                sToast = Toast.makeText(Utils.getContext(), text, duration);
            }
            sToast.setGravity(gravity, xOffset, yOffset);
            sToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消吐司显示
     */
    public static void cancel() {
        try {
            if (sToast != null) {
                sToast.cancel();
                sToast = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}