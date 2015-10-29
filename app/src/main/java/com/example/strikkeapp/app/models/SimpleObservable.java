package com.example.strikkeapp.app.models;

import java.util.ArrayList;

/**
 * Created by oda on 03.06.15.
 */
public class SimpleObservable<T> implements EasyObservable<T>{

   private final ArrayList<OnChangeListener<T>> listeners = new ArrayList<OnChangeListener<T>>();

   public void addListener(OnChangeListener<T> listener){
       synchronized (listeners){
           listeners.add(listener);
       }
   }

    public void removeListener (OnChangeListener <T> listener){
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected void notifyObservers(final T model) {
        synchronized (listeners) {
            for (OnChangeListener<T> listener : listeners) {
                listener.onChange(model);
            }
        }
    }

}
