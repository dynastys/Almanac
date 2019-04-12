package com.zt.rainbowweather.entity;

import java.util.List;

public class ServiceList {


    /**
     * code : 200
     * date : [{"type":1,"name":"服务","links":[{"name":"2019猪年运程","link":"https://www.6669667.com/zhunianyc2019/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/57012caad7ab421696ff1170f0b08ed6.png"},{"name":"八字财运","link":"https://www.6669667.com/bazicy/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/42fd3d1fb3614c6498d1059c7a2384d2.png"},{"name":"宝宝起名","link":"https://www.6669667.com/baobaoqm/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/7a918b6b7c7840b18aafaa02c61b6a84.jpg"},{"name":"财运测算","link":"https://www.6669667.com/xmcaiyun/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/d6f9f58a9c5040bb8141e48adacfe647.png"},{"name":"旺夫女人","link":"https://www.6669667.com/wangfunvren/?spread=xyhd12","link_action":1,"icon_url":"http://media.xuanad.com/img/d6f9f58a9c5040bb8141e48adacfe647.png"}]},{"type":2,"name":"工具","links":[{"name":"天天闹钟","link":"http://baidu.com","link_action":1,"icon_url":"http://media.xuanad.com/img/57012caad7ab421696ff1170f0b08ed6.png"},{"name":"记加班","link":"http://sogou.com","link_action":1,"icon_url":"http://media.xuanad.com/img/42fd3d1fb3614c6498d1059c7a2384d2.png"},{"name":"购优惠","link":"http://qq.com","link_action":1,"icon_url":"http://media.xuanad.com/img/7a918b6b7c7840b18aafaa02c61b6a84.jpg"},{"name":"趣闻社","link":"http://qws.gqtp.com","link_action":1,"icon_url":"http://media.xuanad.com/img/d6f9f58a9c5040bb8141e48adacfe647.png"}]}]
     * msg : 查询成功
     */

    private int code;
    private String msg;
    private List<DateBean> date;

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

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean {
        /**
         * type : 1
         * name : 服务
         * links : [{"name":"2019猪年运程","link":"https://www.6669667.com/zhunianyc2019/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/57012caad7ab421696ff1170f0b08ed6.png"},{"name":"八字财运","link":"https://www.6669667.com/bazicy/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/42fd3d1fb3614c6498d1059c7a2384d2.png"},{"name":"宝宝起名","link":"https://www.6669667.com/baobaoqm/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/7a918b6b7c7840b18aafaa02c61b6a84.jpg"},{"name":"财运测算","link":"https://www.6669667.com/xmcaiyun/?spread=xyhd123","link_action":1,"icon_url":"http://media.xuanad.com/img/d6f9f58a9c5040bb8141e48adacfe647.png"},{"name":"旺夫女人","link":"https://www.6669667.com/wangfunvren/?spread=xyhd12","link_action":1,"icon_url":"http://media.xuanad.com/img/d6f9f58a9c5040bb8141e48adacfe647.png"}]
         */

        private int type;
        private String name;
        private List<LinksBean> links;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<LinksBean> getLinks() {
            return links;
        }

        public void setLinks(List<LinksBean> links) {
            this.links = links;
        }

        public static class LinksBean {
            /**
             * name : 2019猪年运程
             * link : https://www.6669667.com/zhunianyc2019/?spread=xyhd123
             * link_action : 1
             * icon_url : http://media.xuanad.com/img/57012caad7ab421696ff1170f0b08ed6.png
             */

            private String name;
            private String link;
            private int link_action;
            private String icon_url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public int getLink_action() {
                return link_action;
            }

            public void setLink_action(int link_action) {
                this.link_action = link_action;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public void setIcon_url(String icon_url) {
                this.icon_url = icon_url;
            }
        }
    }
}
