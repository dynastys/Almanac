package com.zt.rainbowweather.entity;

import java.util.List;

public class Icons {

    /**
     * code : -1
     * msg : null
     * data : [{"icon_id":1,"icon_type_id":0,"title":"紫微测算","cover":"http://ssp-res.felink.com/mgr/pic/2018051810185053496171.png","link":"http://url.ifjing.com/iaY7Zz","clickAction":1},{"icon_id":2,"icon_type_id":0,"title":"八字批命","cover":"http://ssp-res.felink.com/mgr/pic/2018071717362005947396.png","link":"http://url.ifjing.com/iaY7Zz","clickAction":1},{"icon_id":3,"icon_type_id":0,"title":"猪年大运","cover":"http://ssp-res.felink.com/mgr/pic/2019022011455648175446.png","link":"http://url.ifjing.com/Z7zy6f","clickAction":1},{"icon_id":4,"icon_type_id":0,"title":"八字合婚","cover":"http://ssp-res.felink.com/mgr/pic/2019011015564947461185.png","link":"http://url.ifjing.com/rYvmyu","clickAction":1}]
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
         * icon_id : 1
         * icon_type_id : 0
         * title : 紫微测算
         * cover : http://ssp-res.felink.com/mgr/pic/2018051810185053496171.png
         * link : http://url.ifjing.com/iaY7Zz
         * clickAction : 1
         */

        private int icon_id;
        private int icon_type_id;
        private String title;
        private String cover;
        private String link;
        private int clickAction;

        public int getIcon_id() {
            return icon_id;
        }

        public void setIcon_id(int icon_id) {
            this.icon_id = icon_id;
        }

        public int getIcon_type_id() {
            return icon_type_id;
        }

        public void setIcon_type_id(int icon_type_id) {
            this.icon_type_id = icon_type_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getClickAction() {
            return clickAction;
        }

        public void setClickAction(int clickAction) {
            this.clickAction = clickAction;
        }
    }
}
