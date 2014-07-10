package com.mist.android.managers.users;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.login.User;
import com.mist.android.managers.notes.NoteManager;
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
     * Current user logged.
     */
    private User mCurrentUser;
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
    /**
     * Manager to handle notes of the application in cache, database or online server.
     */
    @Inject
    NoteManager mNoteManager;

    public UserManagerImpl() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
        mUserInterface = restAdapter.create(UserInterface.class);
    }

    @Override
    public User getCurrentUser() {
        if (mCurrentUser == null) {
            mLogger.d(TAG, "Database hit for the current user.");
            mCurrentUser = mUserDao.get();
        } else {
            mLogger.d(TAG, "Cache hit for the current user.");
        }
        return mCurrentUser;
    }

    @Override
    public Token getCurrentToken() {
        if (mCurrentToken == null) {
            mLogger.d(TAG, "Database hit for the current token.");
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                mCurrentToken = mTokenDao.get(currentUser._id);
            }
        } else {
            mLogger.d(TAG, "Database hit for the current token.");
        }
        return mCurrentToken;
    }

    @Override
    public void login(final ActionDelegate<Token> delegate, String login, String password) {
        // Check if we have a session in cache.
        if (mCurrentToken != null && mCurrentToken.isValid()) {
            mLogger.d(TAG, "Cache hit to log in the user.");
            delegate.onSuccess(mCurrentToken);
            return;
        }
        // Check if we have a session in local database.
        User user = mUserDao.get(login);
        if (user != null) {
            Token token = mTokenDao.get(user._id);
            if (token != null && token.isValid()) {
                mLogger.d(TAG, "Database hit to log in the user.");
                token.setUser(user);
                mCurrentToken = token;
                delegate.onSuccess(token);
                return;
            }
        }
        // Otherwise, we make an online request.
        mLogger.d(TAG, "Action hit to log in the user.");
        mUserInterface.login(login, password, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                mUserDao.remove();
                mUserDao.save(token.getUser());
                mTokenDao.save(token);
                mCurrentUser = token.getUser();
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

    @Override
    public void logout() {
        // Remove all notes.
        mNoteManager.removeAll();
        // Remove user information in cache.
        mCurrentUser = null;
        mCurrentToken = null;
        // Remove user information in database.
        mUserDao.remove();
        mTokenDao.remove();
    }

    public interface UserInterface {
        @FormUrlEncoded
        @POST("/login")
        void login(@Field("login") String login, @Field("password") String password, Callback<Token> callback);
    }
}
