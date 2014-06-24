package com.mist.android.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.inject.Inject;
import com.mist.android.BuildConfig;

/**
 * Created by gerard on 12/04/14.
 */
public class LogWrapperImpl implements LogWrapper {

    @Inject
    Context context;

    public void v(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.v(tag, message);
        }
    }

    public void v(final String tag, final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.v(tag, message, e);
        }
    }

    public void d(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.d(tag, message);
        }
    }

    public void d(final String tag, final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.d(tag, message, e);
        }
    }

    public void w(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.w(tag, message);
        }
    }

    public void w(final String tag, final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.w(tag, message, e);
        }
    }

    public void e(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.e(tag, message);
        }
    }

    public void e(final String tag, final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.e(tag, message, e);
        }
    }

    public void wtf(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.wtf(tag, message);
        }
    }

    public void wtf(final String tag, final String message, final Exception e) {
        if (BuildConfig.DEBUG) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            Log.wtf(tag, message, e);
        }
    }

}
