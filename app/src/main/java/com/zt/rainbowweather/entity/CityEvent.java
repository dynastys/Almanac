package com.zt.rainbowweather.entity;

/**
 * Created by hcg on 2018/5/19.
 */

public class CityEvent {
    public City city;
    public boolean isDelete=false;

    public CityEvent(City city) {
        this.city = city;
    }
}
