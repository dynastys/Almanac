package com.zt.rainbowweather.entity.background;

public class AppSpread {


    /**
     * code : 0
     * msg : null
     * data : {"id":7,"img_src":"http://121.199.42.243:8003/AppCoopen/7024e145d34943929e5b44b023a59e94_e58ffaa1d0c7276f1d496bd954c8cea714bcab5548e23-jX5CIn_fw658.jpg","play_duration":4,"click_url":" "}
     */

    private int code;
    private Object msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7
         * img_src : http://121.199.42.243:8003/AppCoopen/7024e145d34943929e5b44b023a59e94_e58ffaa1d0c7276f1d496bd954c8cea714bcab5548e23-jX5CIn_fw658.jpg
         * play_duration : 4
         * click_url :
         */

        private int id;
        private String img_src;
        private int play_duration;
        private String click_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImg_src() {
            return img_src;
        }

        public void setImg_src(String img_src) {
            this.img_src = img_src;
        }

        public int getPlay_duration() {
            return play_duration;
        }

        public void setPlay_duration(int play_duration) {
            this.play_duration = play_duration;
        }

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }
    }
}
