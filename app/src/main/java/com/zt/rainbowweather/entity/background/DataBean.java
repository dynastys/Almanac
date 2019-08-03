package com.zt.rainbowweather.entity.background;

import android.os.Parcel;

import org.litepal.crud.LitePalSupport;

public class DataBean extends LitePalSupport {
        /**
         * backdrop_theme_id : 1
         * name : 城市
         * cover :
         * Type :
         * UseCount : 100
         */

        private int backdrop_theme_id;
        private String name;
        private String cover;
        private String Type;
        private int UseCount;


        public int getBackdrop_theme_id() {
            return backdrop_theme_id;
        }

        public void setBackdrop_theme_id(int backdrop_theme_id) {
            this.backdrop_theme_id = backdrop_theme_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }

        public int getUseCount() {
            return UseCount;
        }

        public void setUseCount(int UseCount) {
            this.UseCount = UseCount;
        }


}
