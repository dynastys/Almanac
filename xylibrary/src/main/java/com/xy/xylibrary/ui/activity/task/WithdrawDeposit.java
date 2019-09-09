package com.xy.xylibrary.ui.activity.task;

public class WithdrawDeposit {

    /**
     * total : 1
     * data : true
     * isSuccess : true
     * message :
     * errorCode : 0
     */

    private int total;
    private boolean data;
    private boolean isSuccess;
    private String message;
    private int errorCode;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
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
