package com.mist.android.managers.users;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.login.User;
import com.mist.android.managers.users.actions.LoginAction;
import com.mist.android.managers.users.daos.TokenDao;
import com.mist.android.managers.users.daos.UserDao;
import com.mist.android.util.LogWrapper;

import retrofit.RestAdapter;
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
    LogWrapper logger;
    /**
     * Dao to save token of the user in the local database.
     */
    @Inject
    TokenDao tokenDao;
    /**
     * Dao to save user in the local database.
     */
    @Inject
    UserDao userDao;
    /**
     * Provider to log in the user.
     */
    @Inject
    Provider<LoginAction> mGetLoginAction;

    public UserManagerImpl() {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        // Create an instance of our GitHub API interface.
        mUserInterface = restAdapter.create(UserInterface.class);
    }

    @Override
    public Token getCurrentToken() {
        return mCurrentToken;
    }

    @Override
    public void login(ActionDelegate<Token> delegate, String login, String password) {
        // Check if we have a session in cache.
        if (mCurrentToken != null && mCurrentToken.isValid()) {
            logger.d(TAG, "Cache hit");
            delegate.onSuccess(mCurrentToken);
            return;
        }
        // Check if we have a session in local database.
        User user = userDao.get(login);
        if (user != null) {
            Token token = tokenDao.get(user._id);
            if (token != null && token.isValid()) {
                logger.d(TAG, "Database hit");
                token.setUser(user);
                mCurrentToken = token;
                delegate.onSuccess(token);
                return;
            }
        }
        // Otherwise, we make a request on the database.
        logger.d(TAG, "Action hit");
        mGetLoginAction.get().perform(mUserInterface, new LoginDelegate(delegate), login, password);
    }

    /**
     * Delegate used for the login.
     */
    class LoginDelegate implements ActionDelegate<Token> {

        private ActionDelegate<Token> mNext;

        public LoginDelegate(ActionDelegate<Token> next) {
            mNext = next;
        }

        @Override
        public void onSuccess(Token result) {
            userDao.save(result.getUser());
            tokenDao.save(result);
            mCurrentToken = result;
            mNext.onSuccess(result);
        }

        @Override
        public void onError(Exception e) {
            mNext.onError(e);
        }
    }

    public interface UserInterface {
        @FormUrlEncoded
        @POST("/login")
        Token login(@Field("login") String login, @Field("password") String password);
    }
}
