package com.xy.xylibrary.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.constellation.xylibrary.R;
import com.xy.xylibrary.Interface.OnInitViewListener;
import com.xy.xylibrary.config.DialogConfig;
import com.xy.xylibrary.utils.DensityUtil;
import com.xy.xylibrary.utils.ViewUtil;

import java.lang.reflect.Field;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 基本dialog
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-26 13:54
 */

public class BasicDialog extends DialogFragment {
    private final static String KEY = "com.wondersgroup.android.library.basic.component.BasicDialogFragment.KEY";

    private DialogConfig mConfig;

    private Handler mHandler;

    private OnInitViewListener mInitListener;
    private DialogInterface.OnCancelListener mOnCancelListener;

    private Activity mActivity;

    @SuppressLint("ValidFragment")
    private BasicDialog() {

    }

    public static BasicDialog newInstance(DialogConfig configuration) {

        Bundle args = new Bundle();
        args.putParcelable(KEY, configuration);

        BasicDialog fragment = new BasicDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        mHandler = new Handler();

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Translucent);

        mConfig = getArguments().getParcelable(KEY);

        if (mConfig != null) {
            setCancelable(mConfig.cancelable);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(mConfig.layout, container, false);

        if (mConfig.bgResource != 0) {
            ViewUtil.setBackgroundResource(root, mConfig.bgResource);
        } else {
            GradientDrawable background = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM, new int[]{mConfig.solid, mConfig.solid});
            background.setCornerRadius(DensityUtil.dp2px(mConfig.radius));
            background.setStroke(mConfig.strokeWidth, mConfig.stroke);
            background.setDither(true);
            background.setShape(GradientDrawable.RECTANGLE);
            ViewUtil.setBackground(root, background);
        }


        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mInitListener != null) {
            mInitListener.initView(this, view);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAttributes();

        if (mConfig.dismissAuto) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissAllowingStateLoss();
                }
            }, mConfig.dismissTime);
        }
    }

    public void setAttributes() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();

            WindowManager.LayoutParams attributes = window.getAttributes();

            attributes.width = getDialogWidth();

            attributes.height = getDialogHeight();

            attributes.gravity = mConfig.gravity;

            attributes.x = mConfig.x;

            attributes.y = mConfig.y;

            window.setAttributes(attributes);
        }
    }

    public void show() {
        show(mActivity.getFragmentManager(), mConfig.tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        if (mOnCancelListener != null) mOnCancelListener.onCancel(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (mOnCancelListener != null) mOnCancelListener.onCancel(dialog);
    }

    public int getDialogWidth() {
        return mConfig.width;
    }

    public int getDialogHeight() {
        return mConfig.height;
    }

    public void setOnInitViewListener(OnInitViewListener listener) {
        mInitListener = listener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }

    private static final Class clz = DialogFragment.class;

    @SuppressWarnings("TryWithIdenticalCatches")
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        //mDismissed = false;
        try {
            Field dismissed = clz.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //mShownByMe = true;
        try {
            Field shown = clz.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
