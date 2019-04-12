package com.zt.rainbowweather.presenter.home;

public interface OnPageChangeListener {
     void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
    void onPageSelected(int position);
    void onPageScrollStateChanged(int state);
}
