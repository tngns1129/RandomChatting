package com.familyset.randomchatting.ui;

public interface BaseView<T> {
    void setPresenter(T presenter);
    boolean isActive();
}
