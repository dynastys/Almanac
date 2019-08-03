package com.zt.rainbowweather.entity.news;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.zt.rainbowweather.ui.adapter.AdviseDetailAdapter;
import com.zt.xuanyin.controller.NativeAd;

import java.util.List;

public class Article {
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

    public static class DataBean implements MultiItemEntity {
 
        public NativeAd nativelogic;
        private String article_id;
        private int article_no;
        private String title;
        private Object description;
        private String html_url;
        private int list_show_type;
        private String from_name;
        private List<String> article_imgs;
        public TTFeedAd ttFeedAd;
        public Object nativeExpressADView;

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public int getArticle_no() {
            return article_no;
        }

        public void setArticle_no(int article_no) {
            this.article_no = article_no;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }

        public int getList_show_type() {
            return list_show_type;
        }

        public void setList_show_type(int list_show_type) {
            this.list_show_type = list_show_type;
        }

        public String getFrom_name() {
            return from_name;
        }

        public void setFrom_name(String from_name) {
            this.from_name = from_name;
        }

        public List<String> getArticle_imgs() {
            return article_imgs;
        }

        public void setArticle_imgs(List<String> article_imgs) {
            this.article_imgs = article_imgs;
        }

        @Override
        public int getItemType() {
          return AdviseDetailAdapter.TYPE_ONE;
        }
    }
}
