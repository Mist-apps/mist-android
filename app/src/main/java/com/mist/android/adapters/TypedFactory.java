package com.mist.android.adapters;

/**
 * Created by gerard on 20/04/14.
 */
public interface TypedFactory<T> extends Factory<TypedHolder> {

    public boolean canCreate(T object);

}