package com.xy.xylibrary.ui.activity.task;

import java.util.List;

public class WithdrawalRecord {

    /**
     * total : 1
     * data : {"sumMoney":10,"withdrawalsInfoVms":[{"id":"ce975a8a-bb68-43e0-9adc-5547fef39a6c","gold":10000,"money":10,"status":0,"desc":"string","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"用户9577182","appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","wxID":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","createTime":"2019-08-23T16:10:21.6993683","updateTime":"2019-08-23T16:10:21.6993742","isDelete":false}]}
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
         * sumMoney : 10
         * withdrawalsInfoVms : [{"id":"ce975a8a-bb68-43e0-9adc-5547fef39a6c","gold":10000,"money":10,"status":0,"desc":"string","userID":"e7a3695d-c7e7-4821-8ac3-e8a05eb9c698","name":"用户9577182","appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","wxID":"oJE5Vv_YT_pNtFWJJ43rtBkv90Ow","createTime":"2019-08-23T16:10:21.6993683","updateTime":"2019-08-23T16:10:21.6993742","isDelete":false}]
         */

        private int sumMoney;
        private List<WithdrawalsInfoVmsBean> withdrawalsInfoVms;

        public int getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(int sumMoney) {
            this.sumMoney = sumMoney;
        }

        public List<WithdrawalsInfoVmsBean> getWithdrawalsInfoVms() {
            return withdrawalsInfoVms;
        }

        public void setWithdrawalsInfoVms(List<WithdrawalsInfoVmsBean> withdrawalsInfoVms) {
            this.withdrawalsInfoVms = withdrawalsInfoVms;
        }

        public static class WithdrawalsInfoVmsBean {
            /**
             * id : ce975a8a-bb68-43e0-9adc-5547fef39a6c
             * gold : 10000
             * money : 10
             * status : 0
             * desc : string
             * userID : e7a3695d-c7e7-4821-8ac3-e8a05eb9c698
             * name : 用户9577182
             * appID : 08c1948f-48fc-4fd6-899e-eda7672b2250
             * wxID : oJE5Vv_YT_pNtFWJJ43rtBkv90Ow
             * createTime : 2019-08-23T16:10:21.6993683
             * updateTime : 2019-08-23T16:10:21.6993742
             * isDelete : false
             */

            private String id;
            private int gold;
            private int money;
            private int status;
            private String desc;
            private String userID;
            private String name;
            private String appID;
            private String wxID;
            private String createTime;
            private String updateTime;
            private boolean isDelete;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
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

            public String getAppID() {
                return appID;
            }

            public void setAppID(String appID) {
                this.appID = appID;
            }

            public String getWxID() {
                return wxID;
            }

            public void setWxID(String wxID) {
                this.wxID = wxID;
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
