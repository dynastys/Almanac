package com.xy.xylibrary.ui.activity.login;

import java.util.List;

public class WeChatLogin {


    /**
     * total : 1
     * data : {"openid":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","nickname":"@东方","sex":"1","language":"zh_CN","province":"","city":"","country":"BM","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/CKmhP9JlNeM9PoMPSx4a7KGgrnEln0ECknOuTL65QuEmqkUbjtsUrVWaQxvvFc5N2Uibial2W7EXtLC5R62nTGMQ/132","privilege":[],"unionid":"oHpUa6M1wpb3bBbKs6fKyu6FQuSE","errcode":null,"errmsg":null,"score":0,"sxid":null,"money":0,"user_img":null}
     * isSuccess : true
     * message : 成功
     * errorCode : 0
     */

    private int total;
    private DataBean data;
    private boolean isSuccess;
    private String message;
    private int errorCode;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public static class DataBean {
        /**
         * openid : oJE5Vv_YT_pNtFWJJ43rtBkv90Ow
         * nickname : @东方
         * sex : 1
         * language : zh_CN
         * province :
         * city :
         * country : BM
         * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/CKmhP9JlNeM9PoMPSx4a7KGgrnEln0ECknOuTL65QuEmqkUbjtsUrVWaQxvvFc5N2Uibial2W7EXtLC5R62nTGMQ/132
         * privilege : []
         * unionid : oHpUa6M1wpb3bBbKs6fKyu6FQuSE
         * errcode : null
         * errmsg : null
         * score : 0
         * sxid : null
         * money : 0.0
         * user_img : null
         */

        private String openid;
        private String nickname;
        private String sex;
        private String language;
        private String province;
        private String city;
        private String country;
        private String headimgurl;
        private String unionid;
        private Object errcode;
        private Object errmsg;
        private int score;
        private Object sxid;
        private double money;
        private Object user_img;
        private List<?> privilege;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public Object getErrcode() {
            return errcode;
        }

        public void setErrcode(Object errcode) {
            this.errcode = errcode;
        }

        public Object getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(Object errmsg) {
            this.errmsg = errmsg;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public Object getSxid() {
            return sxid;
        }

        public void setSxid(Object sxid) {
            this.sxid = sxid;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public Object getUser_img() {
            return user_img;
        }

        public void setUser_img(Object user_img) {
            this.user_img = user_img;
        }

        public List<?> getPrivilege() {
            return privilege;
        }

        public void setPrivilege(List<?> privilege) {
            this.privilege = privilege;
        }
    }
}
