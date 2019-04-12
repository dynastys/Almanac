package com.zt.rainbowweather.entity.weather;

import java.util.List;

public class Now {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {
        /**
         * basic : {"admin_area":"北京","cnty":"中国","lat":"39.90","lon":"116.40","parent_city":"北京","tz":"+8.0"}
         * grid_now : {"cond_code":"100","cond_txt":"晴","hum":"20","pcpn":"0.0","pcpn_10m":"0.0","pres":"1005","tmp":"18","wind_dir":"北风","wind_sc":"1"}
         * status : ok
         * update : {"loc":"2018-04-08 17:00","utc":"2018-04-08 09:00"}
         */

        private BasicBean basic;
        private GridNowBean grid_now;
        private String status;
        private UpdateBean update;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public GridNowBean getGrid_now() {
            return grid_now;
        }

        public void setGrid_now(GridNowBean grid_now) {
            this.grid_now = grid_now;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        public static class BasicBean {
            /**
             * admin_area : 北京
             * cnty : 中国
             * lat : 39.90
             * lon : 116.40
             * parent_city : 北京
             * tz : +8.0
             */

            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;
            private String parent_city;
            private String tz;

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

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getTz() {
                return tz;
            }

            public void setTz(String tz) {
                this.tz = tz;
            }
        }

        public static class GridNowBean {
            /**
             * cond_code : 100
             * cond_txt : 晴
             * hum : 20
             * pcpn : 0.0
             * pcpn_10m : 0.0
             * pres : 1005
             * tmp : 18
             * wind_dir : 北风
             * wind_sc : 1
             */

            private String cond_code;
            private String cond_txt;
            private String hum;
            private String pcpn;
            private String pcpn_10m;
            private String pres;
            private String tmp;
            private String wind_dir;
            private String wind_sc;

            public String getCond_code() {
                return cond_code;
            }

            public void setCond_code(String cond_code) {
                this.cond_code = cond_code;
            }

            public String getCond_txt() {
                return cond_txt;
            }

            public void setCond_txt(String cond_txt) {
                this.cond_txt = cond_txt;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getPcpn_10m() {
                return pcpn_10m;
            }

            public void setPcpn_10m(String pcpn_10m) {
                this.pcpn_10m = pcpn_10m;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getWind_dir() {
                return wind_dir;
            }

            public void setWind_dir(String wind_dir) {
                this.wind_dir = wind_dir;
            }

            public String getWind_sc() {
                return wind_sc;
            }

            public void setWind_sc(String wind_sc) {
                this.wind_sc = wind_sc;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2018-04-08 17:00
             * utc : 2018-04-08 09:00
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
    }
}
