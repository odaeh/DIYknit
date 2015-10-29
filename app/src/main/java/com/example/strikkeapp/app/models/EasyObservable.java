package com.example.strikkeapp.app.models;

/**
 * Created by oda on 03.06.15.
 */
public interface EasyObservable<T> {

    void addListener(OnChangeListener<T> listener);

    void removeListener(OnChangeListener<T> listener);
}

