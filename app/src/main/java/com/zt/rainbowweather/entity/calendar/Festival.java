package com.zt.rainbowweather.entity.calendar;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class Festival {

    /**
     * code : 0
     * msg : null
     * data : [{"festival_id":1,"date":"2019-01-01T00:00:00","festival_type":"节日","work_type":"休","remark":"元旦节 - 12月30日至1月1日放假，共三天，与周末连休。"},{"festival_id":2,"date":"2019-02-02T00:00:00","festival_type":"","work_type":"班","remark":"春节 - 调班"},{"festival_id":3,"date":"2019-02-03T00:00:00","festival_type":"","work_type":"班","remark":"春节 - 调班"},{"festival_id":4,"date":"2019-02-04T00:00:00","festival_type":"节日","work_type":"休","remark":"春节 - 2月04日至2月10日放假调休，共7天。"},{"festival_id":5,"date":"2019-02-05T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":6,"date":"2019-02-06T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":7,"date":"2019-02-07T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":8,"date":"2019-02-08T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":9,"date":"2019-02-09T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":10,"date":"2019-02-10T00:00:00","festival_type":"节日","work_type":"休","remark":"春节"},{"festival_id":11,"date":"2019-04-05T00:00:00","festival_type":"节日","work_type":"休","remark":"清明节 - 4月5日至7日放假调休，共3天，与周末连休。"},{"festival_id":12,"date":"2019-04-06T00:00:00","festival_type":"节日","work_type":"休","remark":"清明节"},{"festival_id":13,"date":"2019-04-07T00:00:00","festival_type":"节日","work_type":"休","remark":"清明节"},{"festival_id":15,"date":"2019-04-28T00:00:00","festival_type":"","work_type":"班","remark":"劳动节 - 调班"},{"festival_id":16,"date":"2019-05-01T00:00:00","festival_type":"节日","work_type":"休","remark":"劳动节 - 5月1日至4日放假调休，共4天。"},{"festival_id":17,"date":"2019-05-02T00:00:00","festival_type":"节日","work_type":"休","remark":"劳动节"},{"festival_id":18,"date":"2019-05-03T00:00:00","festival_type":"节日","work_type":"休","remark":"劳动节"},{"festival_id":19,"date":"2019-05-04T00:00:00","festival_type":"节日","work_type":"休","remark":"劳动节"},{"festival_id":20,"date":"2019-05-05T00:00:00","festival_type":"","work_type":"班","remark":"劳动节 - 调班"},{"festival_id":21,"date":"2019-06-07T00:00:00","festival_type":"节日","work_type":"休","remark":"端午节 - 6月07日至09日放假，共3天，与周末连休。"},{"festival_id":22,"date":"2019-06-08T00:00:00","festival_type":"节日","work_type":"休","remark":"端午节"},{"festival_id":23,"date":"2019-06-09T00:00:00","festival_type":"节日","work_type":"休","remark":"端午节"},{"festival_id":24,"date":"2019-09-13T00:00:00","festival_type":"节日","work_type":"休","remark":"中秋节 - 9月13日至15日放假，共3天，与周末连休。"},{"festival_id":25,"date":"2019-09-14T00:00:00","festival_type":"节日","work_type":"休","remark":"中秋节"},{"festival_id":26,"date":"2019-09-15T00:00:00","festival_type":"节日","work_type":"休","remark":"中秋节"},{"festival_id":27,"date":"2019-09-29T00:00:00","festival_type":"","work_type":"班","remark":"国庆节 - 调班"},{"festival_id":28,"date":"2019-10-01T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节 - 10月1日至7日放假调休，共7天。9月29日（周日）、10月12日（周六）上班。"},{"festival_id":29,"date":"2019-10-02T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":30,"date":"2019-10-03T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":31,"date":"2019-10-04T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":32,"date":"2019-10-05T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":33,"date":"2019-10-06T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":34,"date":"2019-10-07T00:00:00","festival_type":"节日","work_type":"休","remark":"国庆节"},{"festival_id":35,"date":"2019-10-12T00:00:00","festival_type":"","work_type":"班","remark":"国庆节 - 调班"}]
     */

    private int code;
    private Object msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean extends LitePalSupport {
        /**
         * festival_id : 1
         * date : 2019-01-01T00:00:00
         * festival_type : 节日
         * work_type : 休
         * remark : 元旦节 - 12月30日至1月1日放假，共三天，与周末连休。
         */

        private int festival_id;
        private String date;
        private String festival_type;
        private String work_type;
        private String remark;

        public int getFestival_id() {
            return festival_id;
        }

        public void setFestival_id(int festival_id) {
            this.festival_id = festival_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getFestival_type() {
            return festival_type;
        }

        public void setFestival_type(String festival_type) {
            this.festival_type = festival_type;
        }

        public String getWork_type() {
            return work_type;
        }

        public void setWork_type(String work_type) {
            this.work_type = work_type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
