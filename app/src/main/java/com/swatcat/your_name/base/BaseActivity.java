package com.swatcat.your_name.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;


import com.squareup.otto.Bus;
import com.swatcat.your_name.R;
import com.swatcat.your_name.base.action_bar.ActionBarContract;
import com.swatcat.your_name.services.navigation.NavigationController;
import com.swatcat.your_name.services.navigation.manager.ScreenNavigationBackManager;
import com.swatcat.your_name.services.navigation.manager.ScreenNavigationManager;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by max_ermakov on 1/13/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = getClass().getSimpleName();
    private Bus bus;
    private LifecycleManager lifecycleManager;
    private NavigationController navigationController;
    private ScreenNavigationBackManager navigationBackManager;

    private boolean checkConnectivity;
    private boolean isNoNetworkDialogShowing;
    private final int INTERVAL_SHOWING_NO_NETWORK_DIALOG = 5000;


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " onStart()");
        lifecycleManager.onStart();
        bus.register(navigationBackManager);
    }


    @Override
    protected void onStop() {
        Log.i(TAG, " onStop()");
        lifecycleManager.onStop();

        bus.unregister(navigationBackManager);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, " onCreate()" + (savedInstanceState != null ? " recreating" : ""));
        super.onCreate(savedInstanceState);
        bus = new Bus();
        lifecycleManager = new LifecycleManager(bus);
        navigationBackManager = new ScreenNavigationBackManager(this);
        navigationController = new ScreenNavigationManager(this, savedInstanceState);

    }




    //public abstract void lostConnectivityAction();

    @Override
    protected void onResume() {
        Log.i(TAG, " onResume()");
        super.onResume();
        lifecycleManager.onResume();
        App.getInstance().setCurrentActivity(this);
        //checkConnectivity();
    }

    @Override
    protected void onPostResume() {
        Log.i(TAG, " onPostResume()");
        super.onPostResume();
    }


    @Override
    protected void onPause() {
        Log.i(TAG, " onPause()");
        super.onPause();
        lifecycleManager.onPause();
        clearReferences();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, " onDestroy()");
        lifecycleManager.onDestroy();
        clearReferences();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifecycleManager.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        lifecycleManager.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent() Intent=" + intent);
    }


    public Bus getBus() {
        return bus;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public void hideKeyboard() {
        try {
            IBinder windowToken = getWindow().getDecorView().getRootView().getWindowToken();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        } catch (Exception e) {
            Timber.e(e.getLocalizedMessage());
        }
    }

    @Override
    public void onBackPressed() {
        lifecycleManager.onBackPressed();
    }

    //public abstract ActionBarContract.View getActionBarView();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void clearReferences(){
        BaseActivity currActivity = App.getInstance().getCurrentActivity();
        if (this.equals(currActivity))
            App.getInstance().setCurrentActivity(null);
    }

    public LifecycleManager getLifecycleManager() {
        return lifecycleManager;
    }

    public abstract ActionBarContract.View getActionBarView();

    public ScreenNavigationBackManager getNavigationBackManager() {
        return navigationBackManager;
    }


    public Locale getCurrentLocale() {
        return new Locale(getString(R.string.locale));
    }
}

