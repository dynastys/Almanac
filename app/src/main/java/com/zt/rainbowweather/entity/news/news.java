package com.zt.rainbowweather.entity.news;

import java.util.List;

public class news {

    /**
     * code : 0
     * msg : null
     * data : [{"article_id":"b84e368b-1aad-46ea-8ee2-3b5a9b87392e","article_no":23067,"title":"闹的满城风雨的假疫苗事件主人，其家人极其奢华的生活被网友曝光","description":null,"html_url":"/1//23067?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"用户1915095153","article_imgs":["http://p0.qhimgs4.com/t01fdc24cc6e7b4fec4.jpg"]},{"article_id":"217698be-7bdc-46de-9d09-8b612000bdb0","article_no":56955,"title":"张丹峰要凉了？知情人爆料出轨风波害惨剧方：以后不敢再用他演戏","description":null,"html_url":"/1//56955?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"趣闻社","article_imgs":["http://i1.go2yd.com/image.php?url=0LiSDDh6Qw"]},{"article_id":"5526236f-be14-494e-b36f-4036001ce3d8","article_no":20023,"title":"爱情分两种：\u201c套路深的\u201d和\u201c用情真的\u201d","description":null,"html_url":"/1//20023?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":1,"from_name":"趣闻社","article_imgs":["https://n.sinaimg.cn/translate/279/w426h653/20180608/mS2y-hcscwxa4560538.jpg"]},{"article_id":"5250df1f-0c08-47b9-b900-075e655dde99","article_no":20163,"title":"谭维维：如果能在舞台上死去，就太圆满了","description":null,"html_url":"/1//20163?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":1,"from_name":"趣闻社","article_imgs":["http://img.xy1732.cn/upload/cover/e6ce1acfc9b8468f90ba1b44d6de1ec0.jpeg"]},{"article_id":"26df369e-28a3-46c9-b2d3-8ddf23bf9b88","article_no":7,"title":"INFINITE金圣圭宣布14日入伍 以现役身份服役","description":null,"html_url":"/1//7?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":1,"from_name":"趣闻社","article_imgs":["https://n.sinaimg.cn/ent/transform/20170329/o4hz-fycstww1823557.jpg"]},{"article_id":"2cf65977-82dc-4113-b9b7-7f88ee5c43b7","article_no":1908,"title":"《九层妖塔》官司缠身 版权时代别心存侥幸","description":null,"html_url":"/1//1908?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":1,"from_name":"趣闻社","article_imgs":["https://n.sinaimg.cn/eladies/transform/500/w300h200/20180427/dYH9-fztkpip1975999.jpg"]},{"article_id":"6ecfb2e3-4d92-4de4-8f5e-a91e6af25a99","article_no":57027,"title":"刘强东案件女主照片曝光，网友：章泽天美多了，回顾东哥3段情史","description":null,"html_url":"/1//57027?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"趣闻社","article_imgs":["http://i1.go2yd.com/image.php?url=0LrYWi20ri"]},{"article_id":"b244af73-a851-4bd3-a46f-5432e72e9124","article_no":55265,"title":"王菲前夫窦唯东山再起, 身家达数十亿, 如今这样评价王菲","description":null,"html_url":"/1//55265?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"有一时尚","article_imgs":["http://p3.qhimg.com/t012f3e0a3668dea99c.jpg"]},{"article_id":"7108b3f2-45b6-45f9-9449-67ae350fd1e4","article_no":55222,"title":"曾靠一首歌挣了三个亿，流行程度超过凤凰传奇，后做一事遭封杀","description":null,"html_url":"/1//55222?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":0,"from_name":"鱼乐圈八卦","article_imgs":["http://p1.qhimgs4.com/t012d0554e66956c713.jpg"]},{"article_id":"88b5a9fa-bcb4-4e66-be7a-317c41f32145","article_no":1879,"title":"植发手术受季节的影响吗？","description":null,"html_url":"/1//1879?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e","list_show_type":1,"from_name":"趣闻社","article_imgs":["https://n.sinaimg.cn/eladies/transform/500/w300h200/20180412/Bib7-fyzeyqc0163873.jpg"]}]
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

    public static class DataBean {
        /**
         * article_id : b84e368b-1aad-46ea-8ee2-3b5a9b87392e
         * article_no : 23067
         * title : 闹的满城风雨的假疫苗事件主人，其家人极其奢华的生活被网友曝光
         * description : null
         * html_url : /1//23067?s=724b037f-dbaf-4ab4-9bfe-861a03765e4e
         * list_show_type : 0
         * from_name : 用户1915095153
         * article_imgs : ["http://p0.qhimgs4.com/t01fdc24cc6e7b4fec4.jpg"]
         */

        private String article_id;
        private int article_no;
        private String title;
        private Object description;
        private String html_url;
        private int list_show_type;
        private String from_name;
        private List<String> article_imgs;

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public int getArticle_no() {
            return article_no;
        }

        public void setArticle_no(int article_no) {
            this.article_no = article_no;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }

        public int getList_show_type() {
            return list_show_type;
        }

        public void setList_show_type(int list_show_type) {
            this.list_show_type = list_show_type;
        }

        public String getFrom_name() {
            return from_name;
        }

        public void setFrom_name(String from_name) {
            this.from_name = from_name;
        }

        public List<String> getArticle_imgs() {
            return article_imgs;
        }

        public void setArticle_imgs(List<String> article_imgs) {
            this.article_imgs = article_imgs;
        }
    }
}
