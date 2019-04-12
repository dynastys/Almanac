package com.zt.rainbowweather.entity;

public class MoveCityEvent {
    public MoveCityEvent(City sourceCity, City desCity) {
        this.sourceCity = sourceCity;
        this.desCity = desCity;
    }

    public City sourceCity;
    public City desCity;
}
