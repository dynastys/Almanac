package com.zt.rainbowweather.entity.news;

import java.util.List;

public class Nnotice {

    /**
     * code : 0
     * msg : null
     * data : [{"article_id":"de8639b5-cb7e-4404-b3d1-015567fee8af","article_no":23033,"title":"捡破烂女人也不放过 一年半23名妇女受害","description":null,"html_url":"/20//23033?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"小玉聊社会","article_imgs":["http://p2.qhimg.com/t01a8237b17c49d04a5.jpg"]},{"article_id":"356bebaa-474e-4ade-a5bf-2663d821c66a","article_no":54987,"title":"男朋友是用什么套路搞定你的？今晚就让你在床上.....","description":null,"html_url":"/20//54987?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"趣闻社","article_imgs":["http://p2.qhimgs4.com/t01734e156af1a2d594.jpg"]},{"article_id":"0123baeb-a9bb-4d5c-99df-3bf33a997ff9","article_no":23206,"title":"65岁老大爷网恋女大学生，致女孩多次怀孕，大爷：一次就得15万","description":null,"html_url":"/20//23206?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"社会皮皮虾","article_imgs":["http://p1.qhimg.com/t010ad773ddda940fd9.jpg"]}]
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
         * article_id : de8639b5-cb7e-4404-b3d1-015567fee8af
         * article_no : 23033
         * title : 捡破烂女人也不放过 一年半23名妇女受害
         * description : null
         * html_url : /20//23033?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e
         * list_show_type : 0
         * from_name : 小玉聊社会
         * article_imgs : ["http://p2.qhimg.com/t01a8237b17c49d04a5.jpg"]
         */

        private String article_id;
        private int article_no;
        private String title;
        private Object description;
        private String html_url;
        private int list_show_type;
        private String from_name;
        private List<String> article_imgs;

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
    }
}
