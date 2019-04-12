package com.xy.xylibrary.Interface;

import android.view.View;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-23 17:57
 */

public abstract class OnFastClickListener implements View.OnClickListener {

    private static long mLastTimeMillis;

    private long DELAY_TIME;

    public OnFastClickListener() {
        DELAY_TIME = 400L;
    }

    public OnFastClickListener(long delay) {
        DELAY_TIME = delay;
    }

    private boolean isFastClick() {
        long current = System.currentTimeMillis();
        long delay = current - mLastTimeMillis;

        if (delay > 0 && delay < DELAY_TIME) {
            return true;
        }

        mLastTimeMillis = current;

        return false;
    }

    @Override
    public void onClick(View v) {

        if (isFastClick()) {
            return;
        }

        onForbidFastClick(v);
    }

    public abstract void onForbidFastClick(View v);
}
