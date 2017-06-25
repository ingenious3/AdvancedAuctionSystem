package com.example.ishant.advancedauctionsystem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.ishant.advancedauctionsystem.fragment.LoginFragment;
import com.example.ishant.advancedauctionsystem.fragment.SignupFragment;
import com.example.ishant.advancedauctionsystem.modelauction.User;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class AuctionApplication extends OrmActivity  implements LoginFragment.LoginCallbacks, SignupFragment.SignupCallbacks {

    private View welcomeArea;
    private SplashTask splashTask;
    private FragmentManager fragmentManager;

	private static final String FRG_LOGIN = "LOGIN";
    private static final String FRG_SIGNUP = "SIGNUP";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        welcomeArea = findViewById(R.id.welcome_area);
        fragmentManager = getSupportFragmentManager();
        boolean skipSplash = getIntent().getBooleanExtra(Constants.EXTRA_NO_SPLASH, false);
        if (skipSplash) {
            navigateToLogin();
        } else {
            splashTask = new SplashTask();
            splashTask.execute();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (splashTask != null && !splashTask.isCancelled()) {
            splashTask.cancel(true);
        }
    }

    private class SplashTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int result = -1;
            
            long userId = Long.valueOf(userPref.readPreference(Constants.PREF_LOGGED_USER_ID, "-1"));
            boolean remember = Boolean.valueOf(userPref.readPreference(Constants.PREF_REMEMBER_ME, "false"));
            if (userId > 0 && remember) {
                result = 0; 
            }
            
            try {
                Thread.sleep(Constants.SPLASH_TIMEOUT);
            }
            catch (InterruptedException ex) {

            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0) {
                navigateToHome();
            } else {
            
                navigateToLogin();
            }
        }
    }

    private void navigateToLogin() {
        welcomeArea.setVisibility(View.GONE);
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, loginFragment, FRG_LOGIN);
        ft.commit();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, ApplicationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void login(String username, String password, boolean remember) {
        try {
            DBManager dbHelper = getDBHelper();
            password = BasicUtility.MD5(password);
            User user = dbHelper.loginUser(username, password);
            if (user != null) {
                userPref.writeIntoPreferences(new String[] { Constants.PREF_LOGGED_USER_ID, Constants.PREF_REMEMBER_ME }, new String[] { String.valueOf(user.getId()), String.valueOf(remember) });
                navigateToHome();
            } else {
                showToast(getString(R.string.invalid_login));
            }
        }
        catch (SQLException ex) {

        }
        catch (UnsupportedEncodingException ex) {

        }
        catch (NoSuchAlgorithmException ex) {

        }
    }

    @Override
    public void showSignup() {
        SignupFragment signupFragment = new SignupFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, signupFragment);
        ft.addToBackStack(FRG_SIGNUP);
        ft.commit();
    }

    @Override
    public void register(User user) {
        DBManager dbHelper = getDBHelper();
        try {
            List<User> users = dbHelper.getUsersByUsername(user.getUsername());
            if (users == null || users.isEmpty()) {
                if (dbHelper.getUserRuntimeDao().create(user) == 1) {
            
                    showToast(getString(R.string.user_created));
                    userPref.writeIntoPreferences(new String[] { Constants.PREF_LOGGED_USER_ID, Constants.PREF_REMEMBER_ME }, new String[] { String.valueOf(user.getId()), String.valueOf(true) });
                    navigateToHome();
                } else {
                    showToast(getString(R.string.db_user_insert_failed));
                }
            } else {
                showToast(getString(R.string.db_username_exists));
            }
        }
        catch (SQLException ex) {

        }
    }

    @Override
    public void cancel() {
        if (fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}