package com.mist.android.adapters;

/**
 * Created by gerard on 20/04/14.
 */
public interface TypedHolder<T> extends Holder<T>{
    public boolean canHandle(Object object);
}