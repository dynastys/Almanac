package com.zt.rainbowweather.entity.weather;

import java.util.List;

public class AirThDay {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"cid":"CN101020100","location":"上海","parent_city":"上海","admin_area":"上海","cnty":"中国","lat":"31.23170662","lon":"121.47264099","tz":"+8.00"}
         * update : {"loc":"2019-04-19 13:55","utc":"2019-04-19 05:55"}
         * status : ok
         * air_forecast : [{"aqi":"69","date":"2019-04-19","main":"PM2.5","qlty":"良"},{"aqi":"63","date":"2019-04-20","main":"-","qlty":"良"},{"aqi":"92","date":"2019-04-21","main":"PM2.5","qlty":"良"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private List<AirForecastBean> air_forecast;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<AirForecastBean> getAir_forecast() {
            return air_forecast;
        }

        public void setAir_forecast(List<AirForecastBean> air_forecast) {
            this.air_forecast = air_forecast;
        }

        public static class BasicBean {
            /**
             * cid : CN101020100
             * location : 上海
             * parent_city : 上海
             * admin_area : 上海
             * cnty : 中国
             * lat : 31.23170662
             * lon : 121.47264099
             * tz : +8.00
             */

            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;
            private String tz;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2019-04-19 13:55
             * utc : 2019-04-19 05:55
             */

            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }

        public static class AirForecastBean {
            /**
             * aqi : 69
             * date : 2019-04-19
             * main : PM2.5
             * qlty : 良
             */

            private String aqi;
            private String date;
            private String main;
            private String qlty;

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }
        }
    }
}
