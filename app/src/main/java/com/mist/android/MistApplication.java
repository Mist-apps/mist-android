package com.mist.android;

import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;

import java.util.Collection;

import roboguice.RoboGuice;

/**
 * Created by gerard on 24/06/14.
 */
public class MistApplication extends Application {
    public static final String TAG = "MistApplication";

    @Inject
    @Named(TAG)
    Collection<AbstractActionReceiver> mActionReceivers;

    @Override
    public void onCreate() {
        // Create Guice injector.
        RoboGuice.setBaseApplicationInjector(this,
                RoboGuice.DEFAULT_STAGE,
                RoboGuice.newDefaultRoboModule(this),
                new MistModule());
        Injector injector = RoboGuice.getInjector(this);
        injector.injectMembers(this);

        for (AbstractActionReceiver actionReceiver : mActionReceivers) {
            registerReceiver(actionReceiver, actionReceiver.getFilters());
        }
    }

    @Override
    public void onTerminate() {
        for (AbstractActionReceiver actionReceiver : mActionReceivers) {
            unregisterReceiver(actionReceiver);
        }
    }
}
