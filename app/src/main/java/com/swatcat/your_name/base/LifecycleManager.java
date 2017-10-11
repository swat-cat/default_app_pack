package com.swatcat.your_name.base;

import android.content.res.Configuration;
import android.os.Bundle;


import com.squareup.otto.Bus;
import com.swatcat.your_name.base.lifecycle_events.ActivityInvisibleEvent;
import com.swatcat.your_name.base.lifecycle_events.ActivityVisibleEvent;
import com.swatcat.your_name.base.lifecycle_events.OnConfigurationChangedEvent;
import com.swatcat.your_name.base.lifecycle_events.OnCreateCompletedEvent;
import com.swatcat.your_name.base.lifecycle_events.OnDestroyEvent;
import com.swatcat.your_name.base.lifecycle_events.OnPauseEvent;
import com.swatcat.your_name.base.lifecycle_events.OnResumeEvent;
import com.swatcat.your_name.base.lifecycle_events.OnSaveInstanceStateEvent;
import com.swatcat.your_name.base.lifecycle_events.OnStartEvent;
import com.swatcat.your_name.base.lifecycle_events.OnStopEvent;
import com.swatcat.your_name.base.lifecycle_events.OnWindowFocusChangedEvent;
import com.swatcat.your_name.services.navigation.manager.events.OnBackPressedEvent;

import timber.log.Timber;


public class LifecycleManager {
    private final Bus bus;
    private boolean visible; //one more responsibility for this class...

    public LifecycleManager(Bus bus) {
        this.bus = bus;
    }

    public void onCreateCompleted(Bundle savedInstanceState) {
        bus.post(new OnCreateCompletedEvent(savedInstanceState));
    }

    public void onStart() {
        bus.post(new OnStartEvent());
    }

    public void onStop() {
        //region visible/invisible
        visible = false;
        bus.post(new ActivityInvisibleEvent());
        //endregion
        bus.post(new OnStopEvent());
    }

    public void onResume() {
        bus.post(new OnResumeEvent());
    }

    public void onPause() {
        bus.post(new OnPauseEvent());
    }

    public void onSaveInstanceState(Bundle outState) {
        bus.post(new OnSaveInstanceStateEvent(outState));
    }

    public void onDestroy() {
        bus.post(new OnDestroyEvent());
    }

    public void onBackPressed() {
        bus.post(new OnBackPressedEvent());
    }

    public void onWindowFocusChanged(boolean hasFocus) { //is this possible that Activity doesn't lose focus after onStop()..? There was a bug, ActivityVisibleEvent didn't arrive...
        bus.post(new OnWindowFocusChangedEvent(hasFocus));
        if (hasFocus && !visible) {
            visible = true;
            bus.post(new ActivityVisibleEvent());
        }
        //endregion
    }

    public void onConfigurationChanged(Configuration newConfig) {
        //see ScreenOrientationManager if you want to distinct landscape/portrait events and notify properly
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Timber.i("onConfigurationChanged(landscape)");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Timber.i("onConfigurationChanged(portrait)");
        }
        bus.post(new OnConfigurationChangedEvent(newConfig));
    }

    /**
     * For Activities only
     */
    public boolean isVisible() {
        return visible;
    }
}
