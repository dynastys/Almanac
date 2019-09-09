package com.zt.rainbowweather.entity.news;

public class MessageEvent {
    private String message;
    private String cityName;
    public  MessageEvent(String message,String cityName){
        this.message = message;
        this.cityName = cityName;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
