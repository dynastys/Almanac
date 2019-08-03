package com.zt.rainbowweather.entity.news;

public class ViewPageEvent {
    private int message;
    public  ViewPageEvent(int message){
        this.message=message;
    }
    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
