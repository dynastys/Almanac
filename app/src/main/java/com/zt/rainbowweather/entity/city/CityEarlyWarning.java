package com.zt.rainbowweather.entity.city;

public class CityEarlyWarning {

    /**
     * code : 200
     * msg : 获取最新城市预警
     * data : {"icon":"http://image.nmc.cn/static2/site/nmc/themes/basic/alarm/p0003002.png                                                                                                                                                                                                                                                                                                                                                                                                                                                ","weather_warningid":"4a4c53b6-5e37-42f0-929b-8721fad1cf8b","province":"湖南省","city":"长沙市","county":"","type":"每日","warning_type":"高温预警","level":"橙色","status":"解除","warning_url":"http://zgjm.xytq.qukanzixun.com/warning/4a4c53b6-5e37-42f0-929b-8721fad1cf8b"}
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
         * icon : http://image.nmc.cn/static2/site/nmc/themes/basic/alarm/p0003002.png
         * weather_warningid : 4a4c53b6-5e37-42f0-929b-8721fad1cf8b
         * province : 湖南省
         * city : 长沙市
         * county :
         * type : 每日
         * warning_type : 高温预警
         * level : 橙色
         * status : 解除
         * warning_url : http://zgjm.xytq.qukanzixun.com/warning/4a4c53b6-5e37-42f0-929b-8721fad1cf8b
         */

        private String icon;
        private String weather_warningid;
        private String province;
        private String city;
        private String county;
        private String type;
        private String warning_type;
        private String level;
        private String status;
        private String warning_url;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getWeather_warningid() {
            return weather_warningid;
        }

        public void setWeather_warningid(String weather_warningid) {
            this.weather_warningid = weather_warningid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWarning_type() {
            return warning_type;
        }

        public void setWarning_type(String warning_type) {
            this.warning_type = warning_type;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWarning_url() {
            return warning_url;
        }

        public void setWarning_url(String warning_url) {
            this.warning_url = warning_url;
        }
    }
}
