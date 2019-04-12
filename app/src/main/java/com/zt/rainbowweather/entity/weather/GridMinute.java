package com.zt.rainbowweather.entity.weather;

import java.util.List;

public class GridMinute {

    private List<HeWeather6Bean> HeWeather6;

    public List<HeWeather6Bean> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather6Bean> HeWeather6) {
        this.HeWeather6 = HeWeather6;
    }

    public static class HeWeather6Bean {


        private BasicBean basic;
        private GridMinuteForecastBean grid_minute_forecast;
        private PcpnTypeBean pcpn_type;
        private String status;
        private UpdateBean update;
        private List<Pcpn5mBean> pcpn_5m;

        public BasicBean getBasic() {
            return basic;
        }

        public void setBasic(BasicBean basic) {
            this.basic = basic;
        }

        public GridMinuteForecastBean getGrid_minute_forecast() {
            return grid_minute_forecast;
        }

        public void setGrid_minute_forecast(GridMinuteForecastBean grid_minute_forecast) {
            this.grid_minute_forecast = grid_minute_forecast;
        }

        public PcpnTypeBean getPcpn_type() {
            return pcpn_type;
        }

        public void setPcpn_type(PcpnTypeBean pcpn_type) {
            this.pcpn_type = pcpn_type;
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

        public List<Pcpn5mBean> getPcpn_5m() {
            return pcpn_5m;
        }

        public void setPcpn_5m(List<Pcpn5mBean> pcpn_5m) {
            this.pcpn_5m = pcpn_5m;
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

        public static class GridMinuteForecastBean {
            /**
             * date : 2018-04-08 18:25
             */

            private String date;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }
        }

        public static class PcpnTypeBean {
            /**
             * pcpn_type : rain
             */

            private String pcpn_type;

            public String getPcpn_type() {
                return pcpn_type;
            }

            public void setPcpn_type(String pcpn_type) {
                this.pcpn_type = pcpn_type;
            }
        }

        public static class UpdateBean {
            /**
             * loc : 2018-04-08 18:25
             * utc : 2018-04-08 10:25
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

        public static class Pcpn5mBean {
            /**
             * pcpn : 0.0
             * time : 2018-04-08 18:25
             */

            private String pcpn;
            private String time;

            public String getPcpn() {
                return pcpn;
            }

            public void setPcpn(String pcpn) {
                this.pcpn = pcpn;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
