package com.xy.xylibrary.ui.activity.login;

import java.util.List;

public class WeChatLogin {


    /**
     * total : 1
     * data : {"openid":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","nickname":"@东方","sex":"1","language":"zh_CN","province":"","city":"","country":"BM","headimgurl":"http://thirdwx.qlogo.cn/mmopen/vi_32/CKmhP9JlNeM9PoMPSx4a7KGgrnEln0ECknOuTL65QuEmqkUbjtsUrVWaQxvvFc5N2Uibial2W7EXtLC5R62nTGMQ/132","privilege":[],"unionid":"oHpUa6M1wpb3bBbKs6fKyu6FQuSE","errcode":null,"errmsg":null,"score":0,"sxid":null,"money":0,"user_img":null,"userVm":{"id":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","mobile":18621731538,"name":"用户9577182","passWord":"","wxid":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","img":"","gold":123526,"active":195576,"createTime":"2019-08-15T10:04:46.0124848","updateTime":"2019-08-23T17:29:39.9128553","isDelete":false}}
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
         * userVm : {"id":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","mobile":18621731538,"name":"用户9577182","passWord":"","wxid":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","img":"","gold":123526,"active":195576,"createTime":"2019-08-15T10:04:46.0124848","updateTime":"2019-08-23T17:29:39.9128553","isDelete":false}
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
        private UserVmBean userVm;
        private List<Object> privilege;

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

        public UserVmBean getUserVm() {
            return userVm;
        }

        public void setUserVm(UserVmBean userVm) {
            this.userVm = userVm;
        }

        public List<Object> getPrivilege() {
            return privilege;
        }

        public void setPrivilege(List<Object> privilege) {
            this.privilege = privilege;
        }

        public static class UserVmBean {
            /**
             * id : e7a3695d-c7e7-4821-8ac3-e8a05eb9c698
             * mobile : 18621731538
             * name : 用户9577182
             * passWord :
             * wxid : oJE5Vv_YT_pNtFWJJ43rtBkv90Ow
             * img :
             * gold : 123526
             * active : 195576
             * createTime : 2019-08-15T10:04:46.0124848
             * updateTime : 2019-08-23T17:29:39.9128553
             * isDelete : false
             */

            private String id;
            private long mobile;
            private String name;
            private String passWord;
            private String wxid;
            private String img;
            private int gold;
            private int active;
            private String createTime;
            private String updateTime;
            private boolean isDelete;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getMobile() {
                return mobile;
            }

            public void setMobile(long mobile) {
                this.mobile = mobile;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPassWord() {
                return passWord;
            }

            public void setPassWord(String passWord) {
                this.passWord = passWord;
            }

            public String getWxid() {
                return wxid;
            }

            public void setWxid(String wxid) {
                this.wxid = wxid;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public int getActive() {
                return active;
            }

            public void setActive(int active) {
                this.active = active;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public boolean isIsDelete() {
                return isDelete;
            }

            public void setIsDelete(boolean isDelete) {
                this.isDelete = isDelete;
            }
        }
    }
}
