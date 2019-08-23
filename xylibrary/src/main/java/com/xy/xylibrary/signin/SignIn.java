package com.xy.xylibrary.signin;

public class SignIn {


    /**
     * total : 0
     * data : 0
     * isSuccess : false
     * message : 用户已签到
     * errorCode : 9
     */

    private int total;
    private int data;
    private boolean isSuccess;
    private String message;
    private int errorCode;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
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
}
