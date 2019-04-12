package com.xy.xylibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;


import com.readystatesoftware.viewbadger.BadgeView;
import com.xy.xylibrary.Interface.OnBottomSheetInitListener;
import com.xy.xylibrary.Interface.OnFastClickListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * View相关工具类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-07 17:05
 */
@SuppressWarnings("unused")
public class ViewUtil {

    private static final float PADDING = 0.3f;
    private static final int CONTENT_STATE_LOADING = 0;
    private static final int CONTENT_STATE_EMPTY = 1;
    private static final int CONTENT_STATE_ERROR = 2;
    private static final int CONTENT_STATE_CONTENT = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    private @interface Visibility {
    }

    public static BadgeView setBadge(Context context, View badge, int count) {
        BadgeView badgeView = new BadgeView(context, badge);
        badgeView.setText(String.valueOf(count));
        badgeView.setTextSize(12.0f);
        badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeView.show();
        return badgeView;
    }


    /**
     * View延时点击策略
     * {@link View#setOnClickListener(View.OnClickListener)}
     */
    public static void setOnClick(View view, final View.OnClickListener onClickListener) {
        if (checkNotNull(view) && checkNotNull(onClickListener)) {
            view.setOnClickListener(new OnFastClickListener() {
                @Override
                public void onForbidFastClick(View v) {
                    onClickListener.onClick(v);
                }
            });
        }
    }
    /**
     * @param context activity
     * @param id      View初始化代理监听
     */
    public static BottomSheetDialog showBottomSheet(Activity context, @LayoutRes int id, OnBottomSheetInitListener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        final View content = inflater(context, id, null);
        dialog.setContentView(content);
        if (listener != null) listener.onBottomSheetInit(dialog, content);
        View parent = (View) content.getParent();
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        content.measure(0, 0);
        behavior.setSkipCollapsed(false);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(content.getMeasuredHeight());
        dialog.show();
        return dialog;
    }


    /**
     * 显示PopupWindow
     */
    public static PopupWindow showPopupWindow(Context context, @LayoutRes int contentView, View anchor) {
        return showPopupWindow(context, contentView, anchor, false);
    }

    /**
     * 显示PopupWindow
     */
    public static PopupWindow showPopupWindow(Context context, @LayoutRes int contentView, View anchor, boolean outsideTouchable) {

        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setContentView(ViewUtil.inflater(context, contentView, null));
        popupWindow.setOutsideTouchable(outsideTouchable);
        popupWindow.setWidth(anchor.getMeasuredWidth());
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        popupWindow.showAsDropDown(anchor);

        return popupWindow;
    }


    /**
     * 改变ViewGroup的所有子View的可见状态
     */
    public static void changeChildrenVisibility(ViewGroup content, @Visibility int visibility) {
        if (checkNotNull(content)) {
            for (int index = 0; index < content.getChildCount(); index++) {
                View child = content.getChildAt(index);
                if (child instanceof AppBarLayout) continue;
                setVisibility(child, visibility);
            }
        }
    }

    private static ViewGroup.LayoutParams matchParentParams() {
        return new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 子View消耗触摸事件
     */
    private static void preventParentEvent(View child) {
        setOnTouch(child, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }



//    /**
//     * {@link #inflater(int, ViewGroup, boolean)} attachToRoot=false
//     */
//    public static View inflater(Context context,@LayoutRes int id, ViewGroup parent) {
//        return inflater(context,id, parent, false);
//    }

    /**
     * {@link #inflater(int, ViewGroup, boolean)} attachToRoot=false
     */
    public static View inflater(Context context, @LayoutRes int id, ViewGroup parent) {
        return parent != null ?
                getInflaterService(context).inflate(id, parent, false)
                : getInflaterService(context).inflate(id, null);
    }

    /**
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     */
    public static View inflater(Context context,@LayoutRes int id, ViewGroup parent, boolean attach) {
        return getInflaterService(context).inflate(id, parent, attach);
    }

    /**
     * 获取LayoutInflater
     */
    private static LayoutInflater getInflaterService(Context context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * {@link Activity#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(Activity activity, int id) {
        try {
            return activity == null ? null : (E) activity.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link Fragment#getView()} {@link View#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(Fragment fragment, int id) {
        try {
            return (fragment == null || fragment.getView() == null) ?
                    null : (E) fragment.getView().findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link Fragment#getView()} {@link View#findViewWithTag(Object)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E findWithTag(Fragment fragment, Object tag) {
        try {
            return (fragment == null || fragment.getView() == null) ?
                    null : (E) fragment.getView().findViewWithTag(tag);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link View#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(View root, int id) {
        try {
            return root == null ? null : (E) root.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link View#findViewWithTag(Object)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E findWithTag(View root, Object tag) {
        try {
            return root == null ? null : (E) root.findViewWithTag(tag);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link View#setId(int)}
     */
    public static void setId(View view, @IdRes int id) {
        if (checkNotNull(view)) view.setId(id);
    }

    /**
     * {@link View#setTag(Object)}
     */
    public static void setTag(View view, Object tag) {
        if (checkNotNull(view)) view.setTag(tag);
    }

    /**
     * {@link TextView#setText(CharSequence)}
     */
    public static void setText(final TextView view, final CharSequence text) {
        if (checkNotNull(view)) {
            view.setText(text);
            if (view instanceof EditText) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ((EditText) view).setSelection(text.length());
                    }
                });
            }
        }
    }

    /**
     * {@link TextView#setText(int)}
     */
    public static void setText(TextView view, @StringRes int id) {
        if (checkNotNull(view)) view.setText(id);
    }

    /**
     * {@link TextView#append(CharSequence)}
     */
    public static void appendText(TextView view, CharSequence append) {
        if (checkNotNull(view)) view.append(append);
    }

    /**
     * {@link TextView#getText()}
     */
    public static String getText(TextView view) {
        return checkNotNull(view) ? view.getText().toString() : "";
    }

    /**
     * {@link TextView#setTextColor(int)}
     */
    public static void setTextColor(TextView view, int color) {
        if (checkNotNull(view)) view.setTextColor(color);
    }

    /**
     * {@link TextView#setTextSize(float)}
     */
    public static void setTextSize(TextView view, float dp) {
        if (checkNotNull(view)) view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    /**
     * {@link EditText#addTextChangedListener(TextWatcher)}
     */
    public static void addTextChangedListener(EditText view, TextWatcher watcher) {
        if (checkNotNull(view)) view.addTextChangedListener(watcher);
    }



    /**
     * {@link View#setOnTouchListener(View.OnTouchListener)}
     */
    public static void setOnTouch(View view, View.OnTouchListener onTouchListener) {
        if (checkNotNull(view)) view.setOnTouchListener(onTouchListener);
    }

    /**
     * {@link View#setVisibility(int)}
     */
    public static void setVisibility(View view, boolean visible) {
        setVisibility(view, visible ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link View#setVisibility(int)}
     */
    public static void setVisibility(View view, @Visibility int visibility) {
        if (checkNotNull(view) && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    /**
     * {@link ImageView#setImageResource(int)}
     */
    public static void setImageResource(ImageView view, @DrawableRes int id) {
        if (checkNotNull(view)) view.setImageResource(id);
    }

    /**
     * {@link ImageView#setImageBitmap(Bitmap)}
     */
    public static void setImageBitmap(ImageView view, Bitmap bitmap) {
        if (checkNotNull(view)) view.setImageBitmap(bitmap);
    }

    /**
     * {@link View#setBackgroundColor(int)}
     */
    public static void setBackgroundColor(View view, int color) {
        if (checkNotNull(view)) view.setBackgroundColor(color);
    }

    /**
     * {@link View#setBackgroundResource(int)}
     */
    public static void setBackgroundResource(View view, @DrawableRes int color) {
        if (checkNotNull(view)) view.setBackgroundResource(color);
    }

    /**
     * {@link View#setBackground(Drawable)}
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (checkNotNull(view)) view.setBackground(drawable);
    }

    /**
     * {@link RadioButton#setChecked(boolean)}
     */
    public static void setChecked(RadioButton view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link RadioButton#setChecked(boolean)}
     */
    public static void setChecked(SwitchCompat view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link CheckBox#setChecked(boolean)}
     */
    public static void setChecked(CheckBox view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link CheckedTextView#setChecked(boolean)}
     */
    public static void setChecked(CheckedTextView view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link CheckedTextView#setChecked(boolean)}
     */
    public static void setEnabled(View view, boolean enable) {
        if (checkNotNull(view)) view.setEnabled(enable);
    }

    /**
     * {@link ViewGroup#addView(View)}
     */
    public static void addView(ViewGroup parent, View child) {
        if (checkNotNull(parent) && checkNotNull(child)) {
            parent.addView(child);
        }
    }

    /**
     * {@link ViewGroup#addView(View, ViewGroup.LayoutParams)}
     */
    public static void addView(ViewGroup parent, View child, ViewGroup.LayoutParams params) {
        if (checkNotNull(parent) && checkNotNull(child) && checkNotNull(params)) {
            parent.addView(child, params);
        }
    }

    /**
     * {@link ViewGroup#removeView(View)}
     */
    public static void removeView(ViewGroup parent, View child) {
        if (checkNotNull(parent) && checkNotNull(child)) {
            parent.removeView(child);
        }
    }

    public static boolean isEmpty(TextView view) {
        return !checkNotNull(view) || TextUtils.isEmpty(view.getText().toString());
    }

    private static boolean checkNotNull(Object object) {
        return object != null;
    }
}
