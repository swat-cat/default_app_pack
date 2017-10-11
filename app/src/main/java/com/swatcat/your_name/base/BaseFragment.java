package com.swatcat.your_name.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Bus;

import timber.log.Timber;

/**
 * Created by max_ermakov on 1/13/17.
 */

public class BaseFragment extends Fragment {
    protected Bus bus ;
    private LifecycleManager lifecycleManager;

    protected String screenCode;

    public boolean stopped;
    public boolean paused;
    public boolean destroyed;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Timber.i("onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.i("onDetach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        bus = new Bus();
        lifecycleManager = new LifecycleManager(bus);
        Timber.i("onCreate");
    }

    // Don't work as always overridden in subclasses.
    // Call super.onCreateView() if you want this to work.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.i("onCreateView");
        return null;
    }

    @Override
    public void onStart() {
        stopped = false;
        super.onStart();
        Timber.i("onStart");
        lifecycleManager.onStart();
    }

    @Override
    public void onStop() {
        lifecycleManager.onStop();
        Timber.i("onStop");
        super.onStop();
        stopped = true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Timber.i("onConfigurationChanged");
        lifecycleManager.onConfigurationChanged(newConfig);
        tryLogAnalytics();
    }

    @Override
    public void onDestroy() {
        destroyed = true;
        Timber.i("onDestroy");
        lifecycleManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.i("onSaveInstanceState");
        lifecycleManager.onSaveInstanceState(outState);
    }

    protected void tryLogAnalytics() {
//        if (analytics != null && !CommonUtils.isNullOrEmpty(screenCode)) {
//            analytics.logScreen(screenCode);
//        }
    }

    @Override
    public void onResume() {
        paused = false;
        Timber.i("onResume");
        super.onResume();
        tryLogAnalytics();
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.i("onPause");
        paused = true;
    }

    public Bus getBus() {
        return bus;
    }
}
