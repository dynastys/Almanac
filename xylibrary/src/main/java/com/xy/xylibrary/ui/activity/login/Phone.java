package com.xy.xylibrary.ui.activity.login;



public class Phone {

    /**
     * total : 1
     * errorCode : 0
     * data : {"id":"0279e1db-0cfc-44de-a67f-d7f472c44b11","mobile":13333333333,"name":"用户5201006","passWord":"","wxid":"11111","img":"","gold":1000,"vCode":0,"active":1000,"createTime":"2019-08-09T11:05:13.8193708","updateTime":"2019-08-09T11:05:13.8193733","isDelete":false}
     * isSuccess : true
     * message : 成功
     */

    private int total;
    private int errorCode;
    private DataBean data;
    private boolean isSuccess;
    private String message;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
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

    public static class DataBean {
        /**
         * id : 0279e1db-0cfc-44de-a67f-d7f472c44b11
         * mobile : 13333333333
         * name : 用户5201006
         * passWord :
         * wxid : 11111
         * img :
         * gold : 1000
         * vCode : 0
         * active : 1000
         * createTime : 2019-08-09T11:05:13.8193708
         * updateTime : 2019-08-09T11:05:13.8193733
         * isDelete : false
         */

        private String id;
        private long mobile;
        private String name;
        private String passWord;
        private String wxid;
        private String img;
        private int gold;
        private int vCode;
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

        public int getVCode() {
            return vCode;
        }

        public void setVCode(int vCode) {
            this.vCode = vCode;
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
