package com.xy.xylibrary.ui.activity.login;

public interface RequestSyntony<T> {
    public void onCompleted();
    public void onError(Throwable e);
    public void onNext(T t);
}
