package com.mist.android.managers.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.login.User;
import com.mist.android.managers.users.daos.TokenDao;
import com.mist.android.managers.users.daos.UserDao;
import com.mist.android.util.LogWrapper;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by gerard on 24/06/14.
 */
@Singleton
public class UserManagerImpl implements UserManager {
    private static final String TAG = "UserManagerImpl";
    private static final String API_URL = "http://api.leleux.me";
    /**
     * Used to make user online request.
     */
    private UserInterface mUserInterface;
    /**
     * Current token valid of the user.
     */
    private Token mCurrentToken;
    /**
     * Logger.
     */
    @Inject
    LogWrapper mLogger;
    /**
     * Dao to save token of the user in the local database.
     */
    @Inject
    TokenDao mTokenDao;
    /**
     * Dao to save user in the local database.
     */
    @Inject
    UserDao mUserDao;

    public UserManagerImpl() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        mUserInterface = restAdapter.create(UserInterface.class);
    }

    @Override
    public Token getCurrentToken() {
        return mCurrentToken;
    }

    @Override
    public void login(final ActionDelegate<Token> delegate, String login, String password) {
        // Check if we have a session in cache.
        if (mCurrentToken != null && mCurrentToken.isValid()) {
            mLogger.d(TAG, "Cache hit");
            delegate.onSuccess(mCurrentToken);
            return;
        }
        // Check if we have a session in local database.
        User user = mUserDao.get(login);
        if (user != null) {
            Token token = mTokenDao.get(user._id);
            if (token != null && token.isValid()) {
                mLogger.d(TAG, "Database hit");
                token.setUser(user);
                mCurrentToken = token;
                delegate.onSuccess(token);
                return;
            }
        }
        // Otherwise, we make an online request.
        mLogger.d(TAG, "Action hit");
        mUserInterface.login(login, password, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mUserDao.save(token.getUser());
                mTokenDao.save(token);
                mCurrentToken = token;
                delegate.onSuccess(token);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // There is only one status code possible,
                // it is 401 for an invalid login or password.
                delegate.onError(retrofitError);
            }
        });
    }

    public interface UserInterface {
        @FormUrlEncoded
        @POST("/login")
        void login(@Field("login") String login, @Field("password") String password, Callback<Token> callback);
    }
}
