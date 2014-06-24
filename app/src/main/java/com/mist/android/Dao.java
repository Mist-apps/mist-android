package com.mist.android;

/**
 * Created by gerard on 25/06/14.
 */
public interface Dao {
    /**
     * Open the database in write or read mode.
     */
    void open();

    /**
     * Close the database. After that, we can't access at the database.
     */
    void close();
}