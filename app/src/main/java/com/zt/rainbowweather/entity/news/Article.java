package com.zt.rainbowweather.entity.news;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;
import com.zt.xuanyin.controller.NativeAd;

import java.util.List;

public class Article {
    public static final int TYPE_ONE = 1000010;
    public static final int TYPE_THREE = 1000011;


    private int code;
    private Object msg;
    private List<DateBean> date;

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

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean implements MultiItemEntity {


        private String title;
        private String img;
        private String url;
        private String publishTime;
        private String author;
        public NativeAd nativelogic;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        @Override
        public int getItemType() {
          return AdviseDetailAdapter.TYPE_ONE;
        }
    }
}
