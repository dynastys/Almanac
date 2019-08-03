package com.zt.rainbowweather.entity.background;

public class BackdropTheme {

    /**
     * code : 0
     * msg : null
     * data : {"theme_img_id":6,"backdrop_theme_id":1,"img_src":"https://hbimg.huabanimg.com/8bc707c9541e4115510bf4b26d3c8372182fe52482371-FRIfzs","img_md5":""}
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

    public static class DataBean {
        /**
         * theme_img_id : 6
         * backdrop_theme_id : 1
         * img_src : https://hbimg.huabanimg.com/8bc707c9541e4115510bf4b26d3c8372182fe52482371-FRIfzs
         * img_md5 :
         */

        private int theme_img_id;
        private int backdrop_theme_id;
        private String img_src;
        private String img_md5;

        public int getTheme_img_id() {
            return theme_img_id;
        }

        public void setTheme_img_id(int theme_img_id) {
            this.theme_img_id = theme_img_id;
        }

        public int getBackdrop_theme_id() {
            return backdrop_theme_id;
        }

        public void setBackdrop_theme_id(int backdrop_theme_id) {
            this.backdrop_theme_id = backdrop_theme_id;
        }

        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }

        public String getImg_md5() {
            return img_md5;
        }

        public void setImg_md5(String img_md5) {
            this.img_md5 = img_md5;
        }
    }
}
