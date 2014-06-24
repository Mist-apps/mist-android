package com.mist.android.util;

/**
 * Created by gerard on 20/04/14.
 */
public interface LogWrapper {
    void v(final String tag, final String message);

    void v(final String tag, final String message, final Exception e);

    void d(final String tag, final String message);

    void d(final String tag, final String message, final Exception e);

    void w(final String tag, final String message);

    void w(final String tag, final String message, final Exception e);

    void e(final String tag, final String message);

    void e(final String tag, final String message, final Exception e);

    void wtf(final String tag, final String message);

    void wtf(final String tag, final String message, final Exception e);
}
