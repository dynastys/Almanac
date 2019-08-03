package com.zt.rainbowweather.entity.background;


import java.util.List;

public class SkinTheme {

    /**
     * code : 0
     * msg : null
     * data : [{"backdrop_theme_id":1,"name":"城市","cover":"","Type":"","UseCount":100},{"backdrop_theme_id":2,"name":"插画","cover":"","Type":"","UseCount":100},{"backdrop_theme_id":3,"name":"风景","cover":"","Type":"","UseCount":100},{"backdrop_theme_id":4,"name":"动物","cover":"","Type":"","UseCount":13}]
     */

    private int code;
    private Object msg;
    private List<DataBean> data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }



}
