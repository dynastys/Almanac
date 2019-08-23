package com.xy.xylibrary.ui.activity.login;

import java.util.List;

public class UserActiveInfo {


    /**
     * total : 1
     * data : {"toDayGold":4000,"toDayActice":4000,"userActiveVms":[{"id":"7503075d-0ead-411a-9f0a-470cfdd1b2e2","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"签到奖励","gold":2000,"active":2000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","activeType":"签到","createTime":"2019-08-19T17:55:43.9840502","updateTime":"2019-08-19T17:55:43.9840531","isDelete":false},{"id":"b9f889f4-4512-4a6e-ab9d-4cdb058a50c0","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"签到奖励","gold":2000,"active":2000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","activeType":"签到","createTime":"2019-08-19T18:22:28.4098142","updateTime":"2019-08-19T18:22:28.4098159","isDelete":false}]}
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
         * toDayGold : 4000
         * toDayActice : 4000
         * userActiveVms : [{"id":"7503075d-0ead-411a-9f0a-470cfdd1b2e2","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"签到奖励","gold":2000,"active":2000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","activeType":"签到","createTime":"2019-08-19T17:55:43.9840502","updateTime":"2019-08-19T17:55:43.9840531","isDelete":false},{"id":"b9f889f4-4512-4a6e-ab9d-4cdb058a50c0","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"签到奖励","gold":2000,"active":2000,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","activeType":"签到","createTime":"2019-08-19T18:22:28.4098142","updateTime":"2019-08-19T18:22:28.4098159","isDelete":false}]
         */

        private int toDayGold;
        private int toDayActice;
        private List<UserActiveVmsBean> userActiveVms;

        public int getToDayGold() {
            return toDayGold;
        }

        public void setToDayGold(int toDayGold) {
            this.toDayGold = toDayGold;
        }

        public int getToDayActice() {
            return toDayActice;
        }

        public void setToDayActice(int toDayActice) {
            this.toDayActice = toDayActice;
        }

        public List<UserActiveVmsBean> getUserActiveVms() {
            return userActiveVms;
        }

        public void setUserActiveVms(List<UserActiveVmsBean> userActiveVms) {
            this.userActiveVms = userActiveVms;
        }

        public static class UserActiveVmsBean {
            /**
             * id : 7503075d-0ead-411a-9f0a-470cfdd1b2e2
             * userID : e7a3695d-c7e7-4821-8ac3-e8a05eb9c698
             * name : 签到奖励
             * gold : 2000
             * active : 2000
             * appID : 08c1948f-48fc-4fd6-899e-eda7672b2250
             * activeType : 签到
             * createTime : 2019-08-19T17:55:43.9840502
             * updateTime : 2019-08-19T17:55:43.9840531
             * isDelete : false
             */

            private String id;
            private String userID;
            private String name;
            private int gold;
            private int active;
            private String appID;
            private String activeType;
            private String createTime;
            private String updateTime;
            private boolean isDelete;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserID() {
                return userID;
            }

            public void setUserID(String userID) {
                this.userID = userID;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getActiveType() {
                return activeType;
            }

            public void setActiveType(String activeType) {
                this.activeType = activeType;
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
