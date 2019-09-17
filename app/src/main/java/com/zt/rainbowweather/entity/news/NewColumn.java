package com.zt.rainbowweather.entity.news;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class NewColumn  extends LitePalSupport {



    /**
     * code : 200
     * msg : 查询成功
     * data : [{"channel_name":"推荐","channelid":20},{"channel_name":"娱乐","channelid":1},{"channel_name":"情感","channelid":23},{"channel_name":"搞笑","channelid":4},{"channel_name":"时尚","channelid":3},{"channel_name":"星座","channelid":2},{"channel_name":"养生","channelid":7},{"channel_name":"社会","channelid":13}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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
         * channel_name : 推荐
         * channelid : 20
         */

        private String channel_name;
        private int channelid;

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public int getChannelid() {
            return channelid;
        }

        public void setChannelid(int channelid) {
            this.channelid = channelid;
        }
    }
}
