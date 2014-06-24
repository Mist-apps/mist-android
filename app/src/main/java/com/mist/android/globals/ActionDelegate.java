package com.mist.android.globals;

/**
 * Created by gerard on 24/06/14.
 */
public interface ActionDelegate<T> {
    void onSuccess(T result);

    void onError(Exception e);
}
