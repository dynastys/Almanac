package com.xy.xylibrary.ui.activity.login;

import org.litepal.crud.LitePalSupport;

public class WeChat extends LitePalSupport {

    /**
     * code : 0
     * msg : string
     * data : {"user_id":"00000000-0000-0000-0000-000000000000","user_name":"string","sex":0,"Phone":"string","head_imgurl":"string","gold":0,"money":0,"channel_code":"string","taobao_udid":"string","weixin_openid":"string","weixin_udid":"string","token":"string","create_time":"2018-12-17T08:26:27.482Z","lastlogin_time":"2018-12-17T08:26:27.482Z"}
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

    public static class DataBean extends LitePalSupport {
        /**
         * user_id : 00000000-0000-0000-0000-000000000000
         * user_name : string
         * sex : 0
         * Phone : string
         * head_imgurl : string
         * gold : 0
         * money : 0
         * channel_code : string
         * taobao_udid : string
         * weixin_openid : string
         * weixin_udid : string
         * token : string
         * create_time : 2018-12-17T08:26:27.482Z
         * lastlogin_time : 2018-12-17T08:26:27.482Z
         */
        private String user_id;
        private String user_name;
        private int sex;
        private String phone;
        private String head_imgurl;
        private int gold;
        private int money;
        private String channel_code;
        private String taobao_udid;
        private String weixin_openid;
        private String weixin_udid;
        private String token;
        private String create_time;
        private String lastlogin_time;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getHead_imgurl() {
            return head_imgurl;
        }

        public void setHead_imgurl(String head_imgurl) {
            this.head_imgurl = head_imgurl;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getChannel_code() {
            return channel_code;
        }

        public void setChannel_code(String channel_code) {
            this.channel_code = channel_code;
        }

        public String getTaobao_udid() {
            return taobao_udid;
        }

        public void setTaobao_udid(String taobao_udid) {
            this.taobao_udid = taobao_udid;
        }

        public String getWeixin_openid() {
            return weixin_openid;
        }

        public void setWeixin_openid(String weixin_openid) {
            this.weixin_openid = weixin_openid;
        }

        public String getWeixin_udid() {
            return weixin_udid;
        }

        public void setWeixin_udid(String weixin_udid) {
            this.weixin_udid = weixin_udid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getLastlogin_time() {
            return lastlogin_time;
        }

        public void setLastlogin_time(String lastlogin_time) {
            this.lastlogin_time = lastlogin_time;
        }
    }
}
