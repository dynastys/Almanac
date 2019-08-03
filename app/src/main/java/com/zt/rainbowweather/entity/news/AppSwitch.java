package com.zt.rainbowweather.entity.news;

import java.util.List;

public class AppSwitch {

    /**
     * code : 0
     * msg : null
     * data : [{"app_market_code":null,"module_name":"首页底部资讯","on_off":true},{"app_market_code":null,"module_name":"黄历icon","on_off":true},{"app_market_code":null,"module_name":"黄历资讯","on_off":true},{"app_market_code":null,"module_name":"看一看","on_off":true},{"app_market_code":null,"module_name":"个人中心icon1","on_off":true},{"app_market_code":null,"module_name":"个人中心icon2","on_off":true}]
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

    public static class DataBean {
        /**
         * app_market_code : null
         * module_name : 首页底部资讯
         * on_off : true
         */

        private Object app_market_code;
        private String module_name;
        private boolean on_off;

        public Object getApp_market_code() {
            return app_market_code;
        }

        public void setApp_market_code(Object app_market_code) {
            this.app_market_code = app_market_code;
        }

        public String getModule_name() {
            return module_name;
        }

        public void setModule_name(String module_name) {
            this.module_name = module_name;
        }

        public boolean isOn_off() {
            return on_off;
        }

        public void setOn_off(boolean on_off) {
            this.on_off = on_off;
        }
    }
}
