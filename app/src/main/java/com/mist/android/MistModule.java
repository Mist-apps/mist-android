package com.mist.android;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.mist.android.managers.users.UserManager;
import com.mist.android.managers.users.UserManagerImpl;
import com.mist.android.managers.users.daos.TokenDao;
import com.mist.android.managers.users.daos.TokenDaoImpl;
import com.mist.android.managers.users.daos.UserDao;
import com.mist.android.managers.users.daos.UserDaoImpl;
import com.mist.android.util.LogWrapper;
import com.mist.android.util.LogWrapperImpl;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by gerard on 24/06/14.
 */
public class MistModule implements Module {
    @Override
    public void configure(Binder binder) {
        // Wrapper.
        binder.bind(LogWrapper.class).to(LogWrapperImpl.class);

        // Token.
        binder.bind(TokenDao.class).to(TokenDaoImpl.class);

        // Users.
        binder.bind(UserDao.class).to(UserDaoImpl.class);
        binder.bind(UserManager.class).to(UserManagerImpl.class);
    }

    @Provides
    @Named(MistApplication.TAG)
    public Collection<AbstractActionReceiver> providesActionReceivers() {
        return Arrays.asList();
    }
}