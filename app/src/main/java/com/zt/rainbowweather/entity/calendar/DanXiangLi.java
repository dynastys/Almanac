package com.zt.rainbowweather.entity.calendar;

import java.io.Serializable;

public class DanXiangLi implements Serializable {

    /**
     * code : -1
     * msg : null
     * data : {"date":"2019-05-29T15:17:13.5049007+08:00","type":1,"content":"开怀","author_name":"余秀华","author_type":"诗人","production":"月光落在左手上","extract":"我的身体里的火车从来不会错轨，所以允许大雪，风暴，泥石流，和荒谬。"}
     */

    private int code;
    private Object msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * date : 2019-05-29T15:17:13.5049007+08:00
         * type : 1
         * content : 开怀
         * author_name : 余秀华
         * author_type : 诗人
         * production : 月光落在左手上
         * extract : 我的身体里的火车从来不会错轨，所以允许大雪，风暴，泥石流，和荒谬。
         */

        private String date;
        private int type;
        private String content;
        private String author_name;
        private String author_type;
        private String production;
        private String extract;
        public String sui_ci_shengxiao;
        public String month;
        public String monthY;
        public String week;
        public String NongLiDay;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthor_name() {
            return author_name;
        }

        public void setAuthor_name(String author_name) {
            this.author_name = author_name;
        }

        public String getAuthor_type() {
            return author_type;
        }

        public void setAuthor_type(String author_type) {
            this.author_type = author_type;
        }

        public String getProduction() {
            return production;
        }

        public void setProduction(String production) {
            this.production = production;
        }

        public String getExtract() {
            return extract;
        }

        public void setExtract(String extract) {
            this.extract = extract;
        }
    }
}
