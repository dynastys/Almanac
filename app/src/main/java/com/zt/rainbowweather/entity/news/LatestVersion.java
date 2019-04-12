package com.zt.rainbowweather.entity.news;

public class LatestVersion {


    /**
     * code : 0
     * date : {"version_code":1,"version_name":"1.0.0","release_notes":"\r\n1、修复若干BUG。\r\n2、彩虹天气,感谢您的支持,我们将会持续更新,更专注产品服务。","source_file_url":"","publish_time":"2019-04-03T15:01:51.5294899+08:00"}
     * msg : null
     */

    private int code;
    private DateBean date;
    private Object msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DateBean getDate() {
        return date;
    }

    public void setDate(DateBean date) {
        this.date = date;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public static class DateBean {
        /**
         * version_code : 1
         * version_name : 1.0.0
         * release_notes :
         1、修复若干BUG。
         2、彩虹天气,感谢您的支持,我们将会持续更新,更专注产品服务。
         * source_file_url :
         * publish_time : 2019-04-03T15:01:51.5294899+08:00
         */

        private int version_code;
        private String version_name;
        private String release_notes;
        private String source_file_url;
        private String publish_time;

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public String getVersion_name() {
            return version_name;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public String getRelease_notes() {
            return release_notes;
        }

        public void setRelease_notes(String release_notes) {
            this.release_notes = release_notes;
        }

        public String getSource_file_url() {
            return source_file_url;
        }

        public void setSource_file_url(String source_file_url) {
            this.source_file_url = source_file_url;
        }

        public String getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(String publish_time) {
            this.publish_time = publish_time;
        }
    }
}
