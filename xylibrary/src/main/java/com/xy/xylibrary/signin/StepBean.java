package com.xy.xylibrary.signin;

/**
 * description: 签到bean.
 */
public class StepBean {
    /**
     * 未完成
     */
    public static final int STEP_UNDO = -1;
    /**
     * 正在进行
     */
    public static final int STEP_CURRENT = 0;
    /**
     * 已完成
     */
    public static final int STEP_COMPLETED = 1;

    private int state;
    private int number;//0为不显示积分，非0为显示积分
    private String day;

    public StepBean(int state, int number, String day) {
        this.state = state;
        this.number = number;
        this.day = day;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}