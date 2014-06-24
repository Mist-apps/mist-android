package com.mist.android.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.inject.Inject;
import com.mist.android.R;
import com.mist.android.globals.ActionDelegate;
import com.mist.android.main.MainActivity;
import com.mist.android.managers.users.UserManager;
import com.mist.android.util.LogWrapper;

import roboguice.activity.RoboFragmentActivity;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends RoboFragmentActivity implements ActionDelegate<Token> {
    private static final String TAG = "LoginActivity";
    /**
     * Logger.
     */
    @Inject
    LogWrapper logger;
    /**
     * Manager to handle users.
     */
    @Inject
    UserManager mUserManager;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mValidLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Token currentToken = mUserManager.getCurrentToken();
        if (currentToken != null && currentToken.isValid()) {
            startNextActivity(currentToken);
        }
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mValidLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mValidLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        mValidLoginButton.setEnabled(false);
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Launches the sign in.
            mUserManager.login(this, email, password);
        }
    }

    @Override
    public void onSuccess(Token result) {
        mValidLoginButton.setEnabled(false);
        logger.d(TAG, "Token : " + result);
        startNextActivity(result);
    }

    @Override
    public void onError(Exception e) {
        logger.e(TAG, "Error during the login", e);
        mValidLoginButton.setEnabled(true);
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
    }

    private void startNextActivity(Token currentToken) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}



