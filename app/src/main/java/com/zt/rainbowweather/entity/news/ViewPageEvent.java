package com.zt.rainbowweather.entity.news;

public class ViewPageEvent {
    private String City;
    private int message;
    public  ViewPageEvent(int message,String City){
        this.message=message;
        this.City = City;
    }
    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
