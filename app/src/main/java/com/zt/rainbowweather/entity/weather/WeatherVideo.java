package com.zt.rainbowweather.entity.weather;

public class WeatherVideo {

    /**
     * code : 200
     * msg : 获取视频成功！！
     * data : {"title":"最新天气预报","type":"最新天气预报","videourl":"http://img5.2345.com/tianqiimg/video/20190812/video.mp4","cover":"http://qukanzixun.com/Content/aimg/6148b016067fbb1303206181cb5e104.jpg","source":"新闻联播"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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
         * title : 最新天气预报
         * type : 最新天气预报
         * videourl : http://img5.2345.com/tianqiimg/video/20190812/video.mp4
         * cover : http://qukanzixun.com/Content/aimg/6148b016067fbb1303206181cb5e104.jpg
         * source : 新闻联播
         */

        private String title;
        private String type;
        private String videourl;
        private String cover;
        private String source;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVideourl() {
            return videourl;
        }

        public void setVideourl(String videourl) {
            this.videourl = videourl;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
