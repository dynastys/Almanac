package com.xy.xylibrary.signin;


import java.util.List;

public class AppSignInList {

    /**
     * total : 7
     * data : {"toDayIsSign":false,"signCount":0,"signAtureID":"a5ae4ed9-214c-4082-902a-3a8e31996417","signAtureVms":[{"id":"a5ae4ed9-214c-4082-902a-3a8e31996417","signAtureDay":1,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:23:58.69","updateTime":"2019-08-06T20:23:58.69","isDelete":false,"isSignAture":false},{"id":"8bcff670-d599-48e2-8342-1be74ff766a7","signAtureDay":2,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:05.257","updateTime":"2019-08-06T20:24:05.257","isDelete":false,"isSignAture":false},{"id":"657bea14-d0cf-4844-b942-9be88e135bef","signAtureDay":3,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:10.507","updateTime":"2019-08-06T20:24:10.507","isDelete":false,"isSignAture":false},{"id":"aa60a1ea-8d07-43ef-be43-d403e9d8dfd3","signAtureDay":4,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:15.453","updateTime":"2019-08-06T20:24:15.453","isDelete":false,"isSignAture":false},{"id":"546505ae-e2c6-4468-a61d-d51ce1541b7f","signAtureDay":5,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:21.063","updateTime":"2019-08-06T20:24:21.063","isDelete":false,"isSignAture":false},{"id":"48b2e810-7a07-4e40-8b23-26f9df628cce","signAtureDay":6,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:26.367","updateTime":"2019-08-06T20:24:26.367","isDelete":false,"isSignAture":false},{"id":"e668c377-6a86-4f5a-a300-da6376263e39","signAtureDay":7,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:32.6","updateTime":"2019-08-06T20:24:32.6","isDelete":false,"isSignAture":false}]}
     * isSuccess : true
     * message :
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
         * toDayIsSign : false
         * signCount : 0
         * signAtureID : a5ae4ed9-214c-4082-902a-3a8e31996417
         * signAtureVms : [{"id":"a5ae4ed9-214c-4082-902a-3a8e31996417","signAtureDay":1,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:23:58.69","updateTime":"2019-08-06T20:23:58.69","isDelete":false,"isSignAture":false},{"id":"8bcff670-d599-48e2-8342-1be74ff766a7","signAtureDay":2,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:05.257","updateTime":"2019-08-06T20:24:05.257","isDelete":false,"isSignAture":false},{"id":"657bea14-d0cf-4844-b942-9be88e135bef","signAtureDay":3,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:10.507","updateTime":"2019-08-06T20:24:10.507","isDelete":false,"isSignAture":false},{"id":"aa60a1ea-8d07-43ef-be43-d403e9d8dfd3","signAtureDay":4,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:15.453","updateTime":"2019-08-06T20:24:15.453","isDelete":false,"isSignAture":false},{"id":"546505ae-e2c6-4468-a61d-d51ce1541b7f","signAtureDay":5,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:21.063","updateTime":"2019-08-06T20:24:21.063","isDelete":false,"isSignAture":false},{"id":"48b2e810-7a07-4e40-8b23-26f9df628cce","signAtureDay":6,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:26.367","updateTime":"2019-08-06T20:24:26.367","isDelete":false,"isSignAture":false},{"id":"e668c377-6a86-4f5a-a300-da6376263e39","signAtureDay":7,"gold":1000,"active":1000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","giveGold":1000,"giveActive":1000,"createTime":"2019-08-06T20:24:32.6","updateTime":"2019-08-06T20:24:32.6","isDelete":false,"isSignAture":false}]
         */

        private boolean toDayIsSign;
        private int signCount;
        private String signAtureID;
        private List<SignAtureVmsBean> signAtureVms;

        public boolean isToDayIsSign() {
            return toDayIsSign;
        }

        public void setToDayIsSign(boolean toDayIsSign) {
            this.toDayIsSign = toDayIsSign;
        }

        public int getSignCount() {
            return signCount;
        }

        public void setSignCount(int signCount) {
            this.signCount = signCount;
        }

        public String getSignAtureID() {
            return signAtureID;
        }

        public void setSignAtureID(String signAtureID) {
            this.signAtureID = signAtureID;
        }

        public List<SignAtureVmsBean> getSignAtureVms() {
            return signAtureVms;
        }

        public void setSignAtureVms(List<SignAtureVmsBean> signAtureVms) {
            this.signAtureVms = signAtureVms;
        }

        public static class SignAtureVmsBean {
            /**
             * id : a5ae4ed9-214c-4082-902a-3a8e31996417
             * signAtureDay : 1
             * gold : 1000
             * active : 1000
             * appID : 08c1948f-48fc-4fd6-899e-eda7672b2250
             * giveGold : 1000
             * giveActive : 1000
             * createTime : 2019-08-06T20:23:58.69
             * updateTime : 2019-08-06T20:23:58.69
             * isDelete : false
             * isSignAture : false
             */

            private String id;
            private int signAtureDay;
            private int gold;
            private int active;
            private String appID;
            private int giveGold;
            private int giveActive;
            private String createTime;
            private String updateTime;
            private boolean isDelete;
            private boolean isSignAture;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getSignAtureDay() {
                return signAtureDay;
            }

            public void setSignAtureDay(int signAtureDay) {
                this.signAtureDay = signAtureDay;
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

            public String getAppID() {
                return appID;
            }

            public void setAppID(String appID) {
                this.appID = appID;
            }

            public int getGiveGold() {
                return giveGold;
            }

            public void setGiveGold(int giveGold) {
                this.giveGold = giveGold;
            }

            public int getGiveActive() {
                return giveActive;
            }

            public void setGiveActive(int giveActive) {
                this.giveActive = giveActive;
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

            public boolean isIsSignAture() {
                return isSignAture;
            }

            public void setIsSignAture(boolean isSignAture) {
                this.isSignAture = isSignAture;
            }
        }
    }
}
