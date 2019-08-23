package com.xy.xylibrary.signin;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class AppTaskList {

    /**
     * total : 6
     * data : [{"id":"28cc8264-b564-4986-8f77-d08615b73533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":1,"u_IsComplete":false,"u_NextCompleteTime":"2020-01-15T18:20:36.5902123"},{"id":"28cc8264-b564-4986-8f77-d08625b73533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":0,"u_IsComplete":false,"u_NextCompleteTime":null},{"id":"28cc8264-b564-4986-8f77-d08645373533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":1,"u_IsComplete":false,"u_NextCompleteTime":"2020-01-15T18:38:12.4736165"},{"id":"28cc8264-b564-4986-8f77-d08645b63533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":1,"u_IsComplete":false,"u_NextCompleteTime":"2020-01-16T11:29:53.4867682"},{"id":"28cc8264-b564-4986-8f77-d08645b73533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":1,"u_IsComplete":false,"u_NextCompleteTime":"2020-01-16T11:51:11.9137482"},{"id":"28cc8264-b564-4986-8f77-d08645b75533","name":"测试任务2","startTime":"2019-08-06T20:20:39.42","endTime":"2019-08-30T20:20:39.42","completeNumber":5,"completeIntervalType":1,"completeIntervalNumber":5,"appID":"08c1948f-48fc-4fd6-899e-eda7672b2250","appName":null,"maxGold":1000,"minGold":100,"active":500,"multitaskingType":1,"desc":"","isDouble":false,"link":"","completeMinTime":1,"showMaxGold":10000,"showMinGold":1000,"createTime":"2019-08-06T20:20:39.42","updateTime":"2019-08-06T20:20:39.42","isDelete":false,"u_CompleteNumber":1,"u_IsComplete":false,"u_NextCompleteTime":"2020-01-16T12:05:10.4791904"}]
     * isSuccess : true
     * message :
     * errorCode : 0
     */

    private int total;
    private boolean isSuccess;
    private String message;
    private int errorCode;
    private List<DataBean> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 28cc8264-b564-4986-8f77-d08615b73533
         * name : 测试任务2
         * startTime : 2019-08-06T20:20:39.42
         * endTime : 2019-08-30T20:20:39.42
         * completeNumber : 5
         * completeIntervalType : 1
         * completeIntervalNumber : 5
         * appID : 08c1948f-48fc-4fd6-899e-eda7672b2250
         * appName : null
         * maxGold : 1000
         * minGold : 100
         * active : 500
         * multitaskingType : 1
         * desc :
         * isDouble : false
         * link :
         * completeMinTime : 1
         * showMaxGold : 10000
         * showMinGold : 1000
         * createTime : 2019-08-06T20:20:39.42
         * updateTime : 2019-08-06T20:20:39.42
         * isDelete : false
         * u_CompleteNumber : 1
         * u_IsComplete : false
         * u_NextCompleteTime : 2020-01-15T18:20:36.5902123
         */

        private String id;
        private String name;
        private String startTime;
        private String endTime;
        private int completeNumber;
        private int completeIntervalType;
        private int completeIntervalNumber;
        private String appID;
        private Object appName;
        private int maxGold;
        private int minGold;
        private int active;
        private int multitaskingType;
        private String desc;
        private boolean isDouble;
        private String link;
        private int completeMinTime;
        private int showMaxGold;
        private int showMinGold;
        private String createTime;
        private String updateTime;
        private boolean isDelete;
        private int u_CompleteNumber;
        private boolean u_IsComplete;
        private String u_NextCompleteTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getCompleteNumber() {
            return completeNumber;
        }

        public void setCompleteNumber(int completeNumber) {
            this.completeNumber = completeNumber;
        }

        public int getCompleteIntervalType() {
            return completeIntervalType;
        }

        public void setCompleteIntervalType(int completeIntervalType) {
            this.completeIntervalType = completeIntervalType;
        }

        public int getCompleteIntervalNumber() {
            return completeIntervalNumber;
        }

        public void setCompleteIntervalNumber(int completeIntervalNumber) {
            this.completeIntervalNumber = completeIntervalNumber;
        }

        public String getAppID() {
            return appID;
        }

        public void setAppID(String appID) {
            this.appID = appID;
        }

        public Object getAppName() {
            return appName;
        }

        public void setAppName(Object appName) {
            this.appName = appName;
        }

        public int getMaxGold() {
            return maxGold;
        }

        public void setMaxGold(int maxGold) {
            this.maxGold = maxGold;
        }

        public int getMinGold() {
            return minGold;
        }

        public void setMinGold(int minGold) {
            this.minGold = minGold;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getMultitaskingType() {
            return multitaskingType;
        }

        public void setMultitaskingType(int multitaskingType) {
            this.multitaskingType = multitaskingType;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public boolean isIsDouble() {
            return isDouble;
        }

        public void setIsDouble(boolean isDouble) {
            this.isDouble = isDouble;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public int getCompleteMinTime() {
            return completeMinTime;
        }

        public void setCompleteMinTime(int completeMinTime) {
            this.completeMinTime = completeMinTime;
        }

        public int getShowMaxGold() {
            return showMaxGold;
        }

        public void setShowMaxGold(int showMaxGold) {
            this.showMaxGold = showMaxGold;
        }

        public int getShowMinGold() {
            return showMinGold;
        }

        public void setShowMinGold(int showMinGold) {
            this.showMinGold = showMinGold;
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

        public int getU_CompleteNumber() {
            return u_CompleteNumber;
        }

        public void setU_CompleteNumber(int u_CompleteNumber) {
            this.u_CompleteNumber = u_CompleteNumber;
        }

        public boolean isU_IsComplete() {
            return u_IsComplete;
        }

        public void setU_IsComplete(boolean u_IsComplete) {
            this.u_IsComplete = u_IsComplete;
        }

        public String getU_NextCompleteTime() {
            return u_NextCompleteTime;
        }

        public void setU_NextCompleteTime(String u_NextCompleteTime) {
            this.u_NextCompleteTime = u_NextCompleteTime;
        }
    }
}
