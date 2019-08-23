package com.xy.xylibrary.ui.activity.login;

public class UserInfo {

    /**
     * total : 1
     * data : {"id":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","mobile":18621731538,"name":"用户9577182","passWord":"","wxid":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","img":"","gold":46160,"active":42500,"createTime":"2019-08-15T10:04:46.0124848","updateTime":"2019-08-15T16:49:20.9548074","isDelete":false}
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
         * id : e7a3695d-c7e7-4821-8ac3-e8a05eb9c698
         * mobile : 18621731538
         * name : 用户9577182
         * passWord :
         * wxid : oJE5Vv_YT_pNtFWJJ43rtBkv90Ow
         * img :
         * gold : 46160
         * active : 42500
         * createTime : 2019-08-15T10:04:46.0124848
         * updateTime : 2019-08-15T16:49:20.9548074
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
