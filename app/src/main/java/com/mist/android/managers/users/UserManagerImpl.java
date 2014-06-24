package com.mist.android.managers.users;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.login.Token;
import com.mist.android.managers.users.actions.LoginAction;
import com.mist.android.managers.users.delegates.LoginDelegate;

import retrofit.RestAdapter;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by gerard on 24/06/14.
 */
@Singleton
public class UserManagerImpl implements UserManager {
    private static final String API_URL = "http://api.leleux.me";
    /**
     * Used to make user online request.
     */
    private UserInterface mUserInterface;
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
    public void login(ActionDelegate<Token> delegate, String login, String password) {
        mGetLoginAction.get().perform(mUserInterface, new LoginDelegate(delegate), login, password);
    }

    public interface UserInterface {
        @FormUrlEncoded
        @POST("/login")
        Token login(@Field("login") String login, @Field("password") String password);
    }
}
