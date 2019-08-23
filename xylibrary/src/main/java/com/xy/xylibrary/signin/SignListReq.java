package com.xy.xylibrary.signin;

import java.util.List;

public class SignListReq {

    /**
     * ret : 0
     * msg : success!
     * datas : {"userSingninList":[{"id":"278901","userId":"43431","day":"1","familyId":"0","createTime":"2019-05-27 10:02:14","type":"0","seriesDay":"1"}],"day":1,"signState":true,"signLog":{"id":"951652","integral":"8","content":"每日签到","type":"1","logType":"3","familyId":"0","userId":"43431","createTime":"2019-05-27 10:02:14","orderId":"0"},"myPoint":10869}
     */

    private int ret;
    private String msg;
    private DatasBean datas;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DatasBean getDatas() {
        return datas;
    }

    public void setDatas(DatasBean datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * userSingninList : [{"id":"278901","userId":"43431","day":"1","familyId":"0","createTime":"2019-05-27 10:02:14","type":"0","seriesDay":"1"}]
         * day : 1
         * signState : true
         * signLog : {"id":"951652","integral":"8","content":"每日签到","type":"1","logType":"3","familyId":"0","userId":"43431","createTime":"2019-05-27 10:02:14","orderId":"0"}
         * myPoint : 10869
         */

        private int day;
        private boolean signState;
        private SignLogBean signLog;
        private int myPoint;
        private List<UserSingninListBean> userSingninList;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public boolean isSignState() {
            return signState;
        }

        public void setSignState(boolean signState) {
            this.signState = signState;
        }

        public SignLogBean getSignLog() {
            return signLog;
        }

        public void setSignLog(SignLogBean signLog) {
            this.signLog = signLog;
        }

        public int getMyPoint() {
            return myPoint;
        }

        public void setMyPoint(int myPoint) {
            this.myPoint = myPoint;
        }

        public List<UserSingninListBean> getUserSingninList() {
            return userSingninList;
        }

        public void setUserSingninList(List<UserSingninListBean> userSingninList) {
            this.userSingninList = userSingninList;
        }

        public static class SignLogBean {
            /**
             * id : 951652
             * integral : 8
             * content : 每日签到
             * type : 1
             * logType : 3
             * familyId : 0
             * userId : 43431
             * createTime : 2019-05-27 10:02:14
             * orderId : 0
             */

            private String id;
            private String integral;
            private String content;
            private String type;
            private String logType;
            private String familyId;
            private String userId;
            private String createTime;
            private String orderId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getLogType() {
                return logType;
            }

            public void setLogType(String logType) {
                this.logType = logType;
            }

            public String getFamilyId() {
                return familyId;
            }

            public void setFamilyId(String familyId) {
                this.familyId = familyId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }
        }

        public static class UserSingninListBean {
            /**
             * id : 278901
             * userId : 43431
             * day : 1
             * familyId : 0
             * createTime : 2019-05-27 10:02:14
             * type : 0
             * seriesDay : 1
             */

            private String id;
            private String userId;
            private String day;
            private String familyId;
            private String createTime;
            private String type;
            private String seriesDay;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getFamilyId() {
                return familyId;
            }

            public void setFamilyId(String familyId) {
                this.familyId = familyId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSeriesDay() {
                return seriesDay;
            }

            public void setSeriesDay(String seriesDay) {
                this.seriesDay = seriesDay;
            }
        }
    }
}