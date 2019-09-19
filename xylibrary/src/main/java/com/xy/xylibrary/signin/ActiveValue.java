package com.xy.xylibrary.signin;

import java.util.List;

public class ActiveValue {


    /**
     * total : 3
     * data : {"activeRewardsVms":[{"id":"d90719e9-3086-42a0-94fd-41ac6dbab4dd","createTime":"2019-09-18T10:35:02.22","updateTime":"2019-09-18T11:09:20.649681","isDelete":false,"active":200,"gold":2000,"name":"测试任务2","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false},{"id":"ad1cd5f5-600a-46ad-9681-84f4c0c8bee2","createTime":"2019-09-18T10:35:11.4","updateTime":"2019-09-18T11:09:12.3106426","isDelete":false,"active":300,"gold":3000,"name":"测试任务3","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false},{"id":"26239598-f972-4b54-810f-bdcad0d92497","createTime":"2019-09-18T11:08:41.5097936","updateTime":"2019-09-18T11:09:26.9339752","isDelete":false,"active":100,"gold":1000,"name":"测试任务1","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false}],"userActive":0}
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
         * activeRewardsVms : [{"id":"d90719e9-3086-42a0-94fd-41ac6dbab4dd","createTime":"2019-09-18T10:35:02.22","updateTime":"2019-09-18T11:09:20.649681","isDelete":false,"active":200,"gold":2000,"name":"测试任务2","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false},{"id":"ad1cd5f5-600a-46ad-9681-84f4c0c8bee2","createTime":"2019-09-18T10:35:11.4","updateTime":"2019-09-18T11:09:12.3106426","isDelete":false,"active":300,"gold":3000,"name":"测试任务3","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false},{"id":"26239598-f972-4b54-810f-bdcad0d92497","createTime":"2019-09-18T11:08:41.5097936","updateTime":"2019-09-18T11:09:26.9339752","isDelete":false,"active":100,"gold":1000,"name":"测试任务1","desc":null,"imgurl":null,"link":null,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","u_IsComplete":false}]
         * userActive : 0
         */

        private int userActive;
        private List<ActiveRewardsVmsBean> activeRewardsVms;

        public int getUserActive() {
            return userActive;
        }

        public void setUserActive(int userActive) {
            this.userActive = userActive;
        }

        public List<ActiveRewardsVmsBean> getActiveRewardsVms() {
            return activeRewardsVms;
        }

        public void setActiveRewardsVms(List<ActiveRewardsVmsBean> activeRewardsVms) {
            this.activeRewardsVms = activeRewardsVms;
        }

        public static class ActiveRewardsVmsBean {
            /**
             * id : d90719e9-3086-42a0-94fd-41ac6dbab4dd
             * createTime : 2019-09-18T10:35:02.22
             * updateTime : 2019-09-18T11:09:20.649681
             * isDelete : false
             * active : 200
             * gold : 2000
             * name : 测试任务2
             * desc : null
             * imgurl : null
             * link : null
             * appID : 08c1948f-48fc-4fd6-899e-eda7672b2250
             * u_IsComplete : false
             */

            private String id;
            private String createTime;
            private String updateTime;
            private boolean isDelete;
            private int active;
            private int gold;
            private String name;
            private Object desc;
            private Object imgurl;
            private Object link;
            private String appID;
            private boolean u_IsComplete;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public int getActive() {
                return active;
            }

            public void setActive(int active) {
                this.active = active;
            }

            public int getGold() {
                return gold;
            }

            public void setGold(int gold) {
                this.gold = gold;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getDesc() {
                return desc;
            }

            public void setDesc(Object desc) {
                this.desc = desc;
            }

            public Object getImgurl() {
                return imgurl;
            }

            public void setImgurl(Object imgurl) {
                this.imgurl = imgurl;
            }

            public Object getLink() {
                return link;
            }

            public void setLink(Object link) {
                this.link = link;
            }

            public String getAppID() {
                return appID;
            }

            public void setAppID(String appID) {
                this.appID = appID;
            }

            public boolean isU_IsComplete() {
                return u_IsComplete;
            }

            public void setU_IsComplete(boolean u_IsComplete) {
                this.u_IsComplete = u_IsComplete;
            }
        }
    }
}
