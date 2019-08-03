package com.zt.rainbowweather.entity.calendar;

import java.util.List;

public class CrazyDreamRecommend {

    /**
     * code : 0
     * msg : null
     * data : [{"id":1,"name":"考试","url":"http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=考试"},{"id":2,"name":"牙齿","url":"http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=牙齿"},{"id":3,"name":"死人","url":"http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=死人"},{"id":4,"name":"血","url":"http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=血"},{"id":4,"name":"鱼","url":"http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=鱼"}]
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
         * id : 1
         * name : 考试
         * url : http://m.zgjiemeng.com/api.php?g=search&t=app&keyword=考试
         */

        private int id;
        private String name;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
