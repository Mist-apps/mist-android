package com.mist.android;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * Created by gerard on 24/06/14.
 */
public abstract class AbstractActionReceiver extends BroadcastReceiver {

    /**
     * Gets filters of the action receiver.
     */
    public abstract IntentFilter getFilters();
}
