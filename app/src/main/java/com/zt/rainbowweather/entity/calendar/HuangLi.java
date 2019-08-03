package com.zt.rainbowweather.entity.calendar;

import java.io.Serializable;
import java.util.List;

public class HuangLi implements Serializable{

    /**
     * code : 0
     * msg : null
     * data : {"HuangLiId":25351,"Date":"2019-05-29T00:00:00","NongLiYear":"2019","NongLiMonth":"四月","NongLiDay":"廿五","Star":"双子座","TaiShen":"厨灶炉外正南","WuXing":"炉中火","Chong":"冲（庚申）猴","Sha":"煞北","ShengXiao":"猪","JiRi":null,"ZhiRi":null,"XiongShen":"八座 刀砧 独火 鬼哭 横天 劫煞 破败 天兵 天罡 天牢 天瘟 土符 土禁 月害 朱雀中","JiShenYiQu":"天德合 敬安 鸣吠对 母仓 天恩 天贵 天良 天岳","CaiShen":"正西","XiShen":"西南","FuShen":"西北","week":"三","sui_ci":["丙寅日","己亥年","己巳月"],"yi":[{"type":null,"values":["入宅","搬家","祈福","盖屋","移徙","开工","置产","伐木","开张","开业","开市","安床","安葬","出行","上梁","破土","装修","动土","修造","纳畜","斋醮","安香","立券","修坟","经络","启钻","旅游","放水","筑堤"],"action":1}],"ji":[{"type":null,"values":["入宅","搬家","祈福","盖屋","移徙","开工","置产","伐木","开张","开业","开市","安床","安葬","出行","上梁","破土","装修","动土","修造","纳畜","斋醮","安香","立券","修坟","经络","启钻","旅游","放水","筑堤"],"action":2}]}
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

    public static class DataBean implements Serializable{
        /**
         * HuangLiId : 25351
         * Date : 2019-05-29T00:00:00
         * NongLiYear : 2019
         * NongLiMonth : 四月
         * NongLiDay : 廿五
         * Star : 双子座
         * TaiShen : 厨灶炉外正南
         * WuXing : 炉中火
         * Chong : 冲（庚申）猴
         * Sha : 煞北
         * ShengXiao : 猪
         * JiRi : null
         * ZhiRi : null
         * XiongShen : 八座 刀砧 独火 鬼哭 横天 劫煞 破败 天兵 天罡 天牢 天瘟 土符 土禁 月害 朱雀中
         * JiShenYiQu : 天德合 敬安 鸣吠对 母仓 天恩 天贵 天良 天岳
         * CaiShen : 正西
         * XiShen : 西南
         * FuShen : 西北
         * week : 三
         * sui_ci : ["丙寅日","己亥年","己巳月"]
         * yi : [{"type":null,"values":["入宅","搬家","祈福","盖屋","移徙","开工","置产","伐木","开张","开业","开市","安床","安葬","出行","上梁","破土","装修","动土","修造","纳畜","斋醮","安香","立券","修坟","经络","启钻","旅游","放水","筑堤"],"action":1}]
         * ji : [{"type":null,"values":["入宅","搬家","祈福","盖屋","移徙","开工","置产","伐木","开张","开业","开市","安床","安葬","出行","上梁","破土","装修","动土","修造","纳畜","斋醮","安香","立券","修坟","经络","启钻","旅游","放水","筑堤"],"action":2}]
         */

        private int HuangLiId;
        private String Date;
        private String NongLiYear;
        private String NongLiMonth;
        private String NongLiDay;
        private String Star;
        private String TaiShen;
        private String WuXing;
        private String Chong;
        private String Sha;
        private String ShengXiao;
        private Object JiRi;
        private Object ZhiRi;
        private String XiongShen;
        private String JiShenYiQu;
        private String CaiShen;
        private String XiShen;
        private String FuShen;
        private String week;
        private List<String> sui_ci;
        private List<Bean> yi;
        private List<Bean> ji;

        public int getHuangLiId() {
            return HuangLiId;
        }

        public void setHuangLiId(int HuangLiId) {
            this.HuangLiId = HuangLiId;
        }

        public String getDate() {
            return Date;
        }

        public void setDate(String Date) {
            this.Date = Date;
        }

        public String getNongLiYear() {
            return NongLiYear;
        }

        public void setNongLiYear(String NongLiYear) {
            this.NongLiYear = NongLiYear;
        }

        public String getNongLiMonth() {
            return NongLiMonth;
        }

        public void setNongLiMonth(String NongLiMonth) {
            this.NongLiMonth = NongLiMonth;
        }

        public String getNongLiDay() {
            return NongLiDay;
        }

        public void setNongLiDay(String NongLiDay) {
            this.NongLiDay = NongLiDay;
        }

        public String getStar() {
            return Star;
        }

        public void setStar(String Star) {
            this.Star = Star;
        }

        public String getTaiShen() {
            return TaiShen;
        }

        public void setTaiShen(String TaiShen) {
            this.TaiShen = TaiShen;
        }

        public String getWuXing() {
            return WuXing;
        }

        public void setWuXing(String WuXing) {
            this.WuXing = WuXing;
        }

        public String getChong() {
            return Chong;
        }

        public void setChong(String Chong) {
            this.Chong = Chong;
        }

        public String getSha() {
            return Sha;
        }

        public void setSha(String Sha) {
            this.Sha = Sha;
        }

        public String getShengXiao() {
            return ShengXiao;
        }

        public void setShengXiao(String ShengXiao) {
            this.ShengXiao = ShengXiao;
        }

        public Object getJiRi() {
            return JiRi;
        }

        public void setJiRi(Object JiRi) {
            this.JiRi = JiRi;
        }

        public Object getZhiRi() {
            return ZhiRi;
        }

        public void setZhiRi(Object ZhiRi) {
            this.ZhiRi = ZhiRi;
        }

        public String getXiongShen() {
            return XiongShen;
        }

        public void setXiongShen(String XiongShen) {
            this.XiongShen = XiongShen;
        }

        public String getJiShenYiQu() {
            return JiShenYiQu;
        }

        public void setJiShenYiQu(String JiShenYiQu) {
            this.JiShenYiQu = JiShenYiQu;
        }

        public String getCaiShen() {
            return CaiShen;
        }

        public void setCaiShen(String CaiShen) {
            this.CaiShen = CaiShen;
        }

        public String getXiShen() {
            return XiShen;
        }

        public void setXiShen(String XiShen) {
            this.XiShen = XiShen;
        }

        public String getFuShen() {
            return FuShen;
        }

        public void setFuShen(String FuShen) {
            this.FuShen = FuShen;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public List<String> getSui_ci() {
            return sui_ci;
        }

        public void setSui_ci(List<String> sui_ci) {
            this.sui_ci = sui_ci;
        }

        public List<Bean> getYi() {
            return yi;
        }

        public void setYi(List<Bean> yi) {
            this.yi = yi;
        }

        public List<Bean> getJi() {
            return ji;
        }

        public void setJi(List<Bean> ji) {
            this.ji = ji;
        }

        public static class Bean implements Serializable{
            /**
             * type : null
             * values : ["入宅","搬家","祈福","盖屋","移徙","开工","置产","伐木","开张","开业","开市","安床","安葬","出行","上梁","破土","装修","动土","修造","纳畜","斋醮","安香","立券","修坟","经络","启钻","旅游","放水","筑堤"]
             * action : 1
             */

            private Object type;
            private int action;
            private List<String> values;

            public Object getType() {
                return type;
            }

            public void setType(Object type) {
                this.type = type;
            }

            public int getAction() {
                return action;
            }

            public void setAction(int action) {
                this.action = action;
            }

            public List<String> getValues() {
                return values;
            }

            public void setValues(List<String> values) {
                this.values = values;
            }
        }


    }
}
