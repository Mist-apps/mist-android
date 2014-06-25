package com.mist.android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gerard on 20/04/14.
 */
public interface Holder<T> {
    public View getView(LayoutInflater inflater, ViewGroup parent, T content);

    public void setContent(T content);

}
