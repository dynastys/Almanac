package com.zt.rainbowweather.entity.weather;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class ConventionWeather extends LitePalSupport {

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
         * update : {"loc":"2019-05-22 16:54","utc":"2019-05-22 08:54"}
         * status : ok
         * now : {"cloud":"0","cond_code":"100","cond_txt":"晴","fl":"29","hum":"25","pcpn":"0.0","pres":"1006","tmp":"29","vis":"20","wind_deg":"50","wind_dir":"东北风","wind_sc":"1","wind_spd":"3"}
         * daily_forecast : [{"cond_code_d":"100","cond_code_n":"100","cond_txt_d":"晴","cond_txt_n":"晴","date":"2019-05-22","hum":"48","pcpn":"0.0","pop":"0","pres":"1010","sr":"04:53","ss":"18:48","tmp_max":"31","tmp_min":"20","uv_index":"10","vis":"25","wind_deg":"212","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"10"},{"cond_code_d":"100","cond_code_n":"101","cond_txt_d":"晴","cond_txt_n":"多云","date":"2019-05-23","hum":"40","pcpn":"0.0","pop":"0","pres":"1011","sr":"04:53","ss":"18:48","tmp_max":"33","tmp_min":"21","uv_index":"8","vis":"25","wind_deg":"223","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"5"},{"cond_code_d":"101","cond_code_n":"104","cond_txt_d":"多云","cond_txt_n":"阴","date":"2019-05-24","hum":"62","pcpn":"0.0","pop":"0","pres":"1009","sr":"04:53","ss":"18:49","tmp_max":"33","tmp_min":"21","uv_index":"4","vis":"25","wind_deg":"136","wind_dir":"东南风","wind_sc":"4-5","wind_spd":"26"},{"cond_code_d":"104","cond_code_n":"305","cond_txt_d":"阴","cond_txt_n":"小雨","date":"2019-05-25","hum":"78","pcpn":"0.0","pop":"0","pres":"1008","sr":"04:52","ss":"18:49","tmp_max":"27","tmp_min":"20","uv_index":"0","vis":"11","wind_deg":"178","wind_dir":"南风","wind_sc":"5-6","wind_spd":"35"},{"cond_code_d":"306","cond_code_n":"305","cond_txt_d":"中雨","cond_txt_n":"小雨","date":"2019-05-26","hum":"77","pcpn":"5.1","pop":"80","pres":"1007","sr":"04:52","ss":"18:50","tmp_max":"25","tmp_min":"21","uv_index":"3","vis":"4","wind_deg":"179","wind_dir":"南风","wind_sc":"4-5","wind_spd":"27"},{"cond_code_d":"101","cond_code_n":"100","cond_txt_d":"多云","cond_txt_n":"晴","date":"2019-05-27","hum":"38","pcpn":"1.0","pop":"55","pres":"1015","sr":"04:51","ss":"18:51","tmp_max":"26","tmp_min":"20","uv_index":"12","vis":"24","wind_deg":"9","wind_dir":"北风","wind_sc":"4-5","wind_spd":"27"},{"cond_code_d":"101","cond_code_n":"101","cond_txt_d":"多云","cond_txt_n":"多云","date":"2019-05-28","hum":"33","pcpn":"0.0","pop":"1","pres":"1014","sr":"04:51","ss":"18:51","tmp_max":"26","tmp_min":"19","uv_index":"12","vis":"25","wind_deg":"270","wind_dir":"西风","wind_sc":"1-2","wind_spd":"1"}]
         * hourly : [{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"13","hum":"36","pop":"0","pres":"1010","time":"2019-05-22 17:00","tmp":"28","wind_deg":"248","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"9"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"37","pop":"0","pres":"1010","time":"2019-05-22 18:00","tmp":"26","wind_deg":"170","wind_dir":"南风","wind_sc":"3-4","wind_spd":"23"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"17","hum":"37","pop":"0","pres":"1010","time":"2019-05-22 19:00","tmp":"25","wind_deg":"189","wind_dir":"南风","wind_sc":"1-2","wind_spd":"1"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"17","hum":"38","pop":"0","pres":"1011","time":"2019-05-22 20:00","tmp":"25","wind_deg":"236","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"5"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"17","hum":"43","pop":"0","pres":"1012","time":"2019-05-22 21:00","tmp":"24","wind_deg":"170","wind_dir":"南风","wind_sc":"1-2","wind_spd":"10"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"16","hum":"48","pop":"0","pres":"1012","time":"2019-05-22 22:00","tmp":"23","wind_deg":"171","wind_dir":"南风","wind_sc":"1-2","wind_spd":"7"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"52","pop":"0","pres":"1012","time":"2019-05-22 23:00","tmp":"22","wind_deg":"195","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"2"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"56","pop":"0","pres":"1013","time":"2019-05-23 00:00","tmp":"22","wind_deg":"171","wind_dir":"南风","wind_sc":"1-2","wind_spd":"8"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"60","pop":"0","pres":"1013","time":"2019-05-23 01:00","tmp":"22","wind_deg":"178","wind_dir":"南风","wind_sc":"1-2","wind_spd":"5"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"64","pop":"0","pres":"1014","time":"2019-05-23 02:00","tmp":"21","wind_deg":"259","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"9"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"66","pop":"0","pres":"1014","time":"2019-05-23 03:00","tmp":"21","wind_deg":"222","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"2"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"69","pop":"0","pres":"1014","time":"2019-05-23 04:00","tmp":"20","wind_deg":"197","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"1"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"14","hum":"71","pop":"0","pres":"1014","time":"2019-05-23 05:00","tmp":"20","wind_deg":"232","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"9"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"66","pop":"0","pres":"1013","time":"2019-05-23 06:00","tmp":"21","wind_deg":"235","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"9"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"61","pop":"0","pres":"1013","time":"2019-05-23 07:00","tmp":"23","wind_deg":"249","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"8"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"56","pop":"0","pres":"1014","time":"2019-05-23 08:00","tmp":"24","wind_deg":"200","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"2"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"14","hum":"48","pop":"0","pres":"1014","time":"2019-05-23 09:00","tmp":"26","wind_deg":"191","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"4"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"14","hum":"40","pop":"0","pres":"1014","time":"2019-05-23 10:00","tmp":"28","wind_deg":"197","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"7"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"32","pop":"0","pres":"1014","time":"2019-05-23 11:00","tmp":"30","wind_deg":"233","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"8"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"34","pop":"0","pres":"1016","time":"2019-05-23 12:00","tmp":"31","wind_deg":"243","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"3"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"36","pop":"0","pres":"1016","time":"2019-05-23 13:00","tmp":"31","wind_deg":"218","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"1"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"39","pop":"0","pres":"1013","time":"2019-05-23 14:00","tmp":"32","wind_deg":"210","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"8"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"15","hum":"36","pop":"0","pres":"1014","time":"2019-05-23 15:00","tmp":"30","wind_deg":"199","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"2"},{"cloud":"0","cond_code":"100","cond_txt":"晴","dew":"16","hum":"36","pop":"0","pres":"1014","time":"2019-05-23 16:00","tmp":"30","wind_deg":"199","wind_dir":"西南风","wind_sc":"1-2","wind_spd":"2"}]
         * lifestyle : [{"type":"comf","brf":"较不舒适","txt":"白天天气晴好，明媚的阳光在给您带来好心情的同时，也会使您感到有些热，不很舒适。"},{"type":"drsg","brf":"热","txt":"天气热，建议着短裙、短裤、短薄外套、T恤等夏季服装。"},{"type":"flu","brf":"少发","txt":"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。"},{"type":"sport","brf":"较适宜","txt":"天气较好，户外运动请注意防晒，推荐您在室内进行低强度运动。"},{"type":"trav","brf":"适宜","txt":"天气较好，但稍感觉有些热，不过还是个好天气哦。适宜旅游，可不要错过机会呦！"},{"type":"uv","brf":"很强","txt":"紫外线辐射极强，建议涂擦SPF20以上、PA++的防晒护肤品，尽量避免暴露于日光下。"},{"type":"cw","brf":"较适宜","txt":"较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"},{"type":"air","brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},{"type":"ac","brf":"部分时间开启","txt":"天气热，到中午的时候您将会感到有点热，因此建议在午后较热时开启制冷空调。"},{"type":"ag","brf":"易发","txt":"天气条件易诱发过敏，易过敏人群应减少外出，外出宜穿长衣长裤并佩戴好眼镜和口罩，外出归来时及时清洁手和口鼻。"},{"type":"gl","brf":"很必要","txt":"白天天空晴朗，太阳辐射很强，建议佩戴透射比2级且标注UV400的遮阳镜"},{"type":"mu","brf":"去油防晒","txt":"建议用蜜质SPF20面霜打底，水质无油粉底霜。"},{"type":"airc","brf":"极适宜","txt":"天气不错，极适宜晾晒。抓紧时机把久未见阳光的衣物搬出来晒晒太阳吧！"},{"type":"ptfc","brf":"良好","txt":"天气较好，路面干燥，交通气象条件良好，车辆可以正常行驶。"},{"type":"fsh","brf":"较适宜","txt":"较适合垂钓，但天气稍热，会对垂钓产生一定的影响。"},{"type":"spi","brf":"极强","txt":"紫外辐射极强，外出时应特别加强防护，建议涂擦SPF20以上，PA++的防晒护肤品，并随时补涂。"}]
         */

        private BasicBean basic;
        private UpdateBean update;
        private String status;
        private NowBean now;
        private List<DailyForecastBean> daily_forecast;
        private List<HourlyBean> hourly;
        private List<LifestyleBean> lifestyle;

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

        public NowBean getNow() {
            return now;
        }

        public void setNow(NowBean now) {
            this.now = now;
        }

        public List<DailyForecastBean> getDaily_forecast() {
            return daily_forecast;
        }

        public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
            this.daily_forecast = daily_forecast;
        }

        public List<HourlyBean> getHourly() {
            return hourly;
        }

        public void setHourly(List<HourlyBean> hourly) {
            this.hourly = hourly;
        }

        public List<LifestyleBean> getLifestyle() {
            return lifestyle;
        }

        public void setLifestyle(List<LifestyleBean> lifestyle) {
            this.lifestyle = lifestyle;
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
             * loc : 2019-05-22 16:54
             * utc : 2019-05-22 08:54
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

        public static class NowBean {
            /**
             * cloud : 0
             * cond_code : 100
             * cond_txt : 晴
             * fl : 29
             * hum : 25
             * pcpn : 0.0
             * pres : 1006
             * tmp : 29
             * vis : 20
             * wind_deg : 50
             * wind_dir : 东北风
             * wind_sc : 1
             * wind_spd : 3
             */

            private String cloud;
            private String cond_code;
            private String cond_txt;
            private String fl;
            private String hum;
            private String pcpn;
            private String pres;
            private String tmp;
            private String vis;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCloud() {
                return cloud;
            }

            public void setCloud(String cloud) {
                this.cloud = cloud;
            }

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

            public String getFl() {
                return fl;
            }

            public void setFl(String fl) {
                this.fl = fl;
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

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
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

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }
        }

        public static class DailyForecastBean implements Parcelable{
            /**
             * cond_code_d : 100
             * cond_code_n : 100
             * cond_txt_d : 晴
             * cond_txt_n : 晴
             * date : 2019-05-22
             * hum : 48
             * pcpn : 0.0
             * pop : 0
             * pres : 1010
             * sr : 04:53
             * ss : 18:48
             * tmp_max : 31
             * tmp_min : 20
             * uv_index : 10
             * vis : 25
             * wind_deg : 212
             * wind_dir : 西南风
             * wind_sc : 1-2
             * wind_spd : 10
             */

            private String cond_code_d;
            private String cond_code_n;
            private String cond_txt_d;
            private String cond_txt_n;
            private String date;
            private String hum;
            private String pcpn;
            private String pop;
            private String pres;
            private String sr;
            private String ss;
            private String tmp_max;
            private String tmp_min;
            private String uv_index;
            private String vis;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            protected DailyForecastBean(Parcel in) {
                cond_code_d = in.readString();
                cond_code_n = in.readString();
                cond_txt_d = in.readString();
                cond_txt_n = in.readString();
                date = in.readString();
                hum = in.readString();
                pcpn = in.readString();
                pop = in.readString();
                pres = in.readString();
                sr = in.readString();
                ss = in.readString();
                tmp_max = in.readString();
                tmp_min = in.readString();
                uv_index = in.readString();
                vis = in.readString();
                wind_deg = in.readString();
                wind_dir = in.readString();
                wind_sc = in.readString();
                wind_spd = in.readString();
            }

            public static final Creator<DailyForecastBean> CREATOR = new Creator<DailyForecastBean>() {
                @Override
                public DailyForecastBean createFromParcel(Parcel in) {
                    return new DailyForecastBean(in);
                }

                @Override
                public DailyForecastBean[] newArray(int size) {
                    return new DailyForecastBean[size];
                }
            };

            public String getCond_code_d() {
                return cond_code_d;
            }

            public void setCond_code_d(String cond_code_d) {
                this.cond_code_d = cond_code_d;
            }

            public String getCond_code_n() {
                return cond_code_n;
            }

            public void setCond_code_n(String cond_code_n) {
                this.cond_code_n = cond_code_n;
            }

            public String getCond_txt_d() {
                return cond_txt_d;
            }

            public void setCond_txt_d(String cond_txt_d) {
                this.cond_txt_d = cond_txt_d;
            }

            public String getCond_txt_n() {
                return cond_txt_n;
            }

            public void setCond_txt_n(String cond_txt_n) {
                this.cond_txt_n = cond_txt_n;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
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

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }

            public String getTmp_max() {
                return tmp_max;
            }

            public void setTmp_max(String tmp_max) {
                this.tmp_max = tmp_max;
            }

            public String getTmp_min() {
                return tmp_min;
            }

            public void setTmp_min(String tmp_min) {
                this.tmp_min = tmp_min;
            }

            public String getUv_index() {
                return uv_index;
            }

            public void setUv_index(String uv_index) {
                this.uv_index = uv_index;
            }

            public String getVis() {
                return vis;
            }

            public void setVis(String vis) {
                this.vis = vis;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
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

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(cond_code_d);
                dest.writeString(cond_code_n);
                dest.writeString(cond_txt_d);
                dest.writeString(cond_txt_n);
                dest.writeString(date);
                dest.writeString(hum);
                dest.writeString(pcpn);
                dest.writeString(pop);
                dest.writeString(pres);
                dest.writeString(sr);
                dest.writeString(ss);
                dest.writeString(tmp_max);
                dest.writeString(tmp_min);
                dest.writeString(uv_index);
                dest.writeString(vis);
                dest.writeString(wind_deg);
                dest.writeString(wind_dir);
                dest.writeString(wind_sc);
                dest.writeString(wind_spd);
            }
        }

        public static class HourlyBean {
            /**
             * cloud : 0
             * cond_code : 100
             * cond_txt : 晴
             * dew : 13
             * hum : 36
             * pop : 0
             * pres : 1010
             * time : 2019-05-22 17:00
             * tmp : 28
             * wind_deg : 248
             * wind_dir : 西南风
             * wind_sc : 1-2
             * wind_spd : 9
             */

            private String cloud;
            private String cond_code;
            private String cond_txt;
            private String dew;
            private String hum;
            private String pop;
            private String pres;
            private String time;
            private String tmp;
            private String wind_deg;
            private String wind_dir;
            private String wind_sc;
            private String wind_spd;

            public String getCloud() {
                return cloud;
            }

            public void setCloud(String cloud) {
                this.cloud = cloud;
            }

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

            public String getDew() {
                return dew;
            }

            public void setDew(String dew) {
                this.dew = dew;
            }

            public String getHum() {
                return hum;
            }

            public void setHum(String hum) {
                this.hum = hum;
            }

            public String getPop() {
                return pop;
            }

            public void setPop(String pop) {
                this.pop = pop;
            }

            public String getPres() {
                return pres;
            }

            public void setPres(String pres) {
                this.pres = pres;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTmp() {
                return tmp;
            }

            public void setTmp(String tmp) {
                this.tmp = tmp;
            }

            public String getWind_deg() {
                return wind_deg;
            }

            public void setWind_deg(String wind_deg) {
                this.wind_deg = wind_deg;
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

            public String getWind_spd() {
                return wind_spd;
            }

            public void setWind_spd(String wind_spd) {
                this.wind_spd = wind_spd;
            }
        }

        public static class LifestyleBean implements Serializable {
            /**
             * type : comf
             * brf : 较不舒适
             * txt : 白天天气晴好，明媚的阳光在给您带来好心情的同时，也会使您感到有些热，不很舒适。
             */

            private String type;
            private String brf;
            private String txt;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }
}
