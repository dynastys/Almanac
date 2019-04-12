package com.xy.xylibrary.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.xy.xylibrary.utils.LoadingDialog;

import butterknife.ButterKnife;

/**
 * zw
 */
public abstract class BaseFragment extends Fragment {

    private View rootView;
    /**
     * 初始化布局
     */
    protected abstract int getLayoutRes();

    protected abstract void initData(View view);

    protected abstract void initListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        rootView = view;
        // 初始化View注入
        ButterKnife.bind(this,view);
        initData(view);
        initListener();
        return view;
    }
    public void visible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void invisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }
    public View getRootView(){
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(getActivity());
    }

    private LoadingDialog loadingDialog;
    //加载进度条
    public void showLoadingDialog(String message) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
        }
        loadingDialog.show(message, true, false);
    }

    //取消进度条
    public void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

}
