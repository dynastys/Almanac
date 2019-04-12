package com.zt.rainbowweather.entity.city;


import com.zt.rainbowweather.entity.City;

public class Event {

    public City city;
    public boolean isDelete=false;

    public Event(City city,boolean isDelete) {
        this.city= city;
        this.isDelete = isDelete;
    }

    public City getData() {
        return city;
    }

    public void setData(City city) {
        this.city= city;
    }

}
