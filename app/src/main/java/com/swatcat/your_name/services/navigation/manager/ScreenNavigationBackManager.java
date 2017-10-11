package com.swatcat.your_name.services.navigation.manager;

import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;

import com.squareup.otto.Subscribe;
import com.swatcat.your_name.base.BaseActivity;
import com.swatcat.your_name.services.navigation.ScreenAnimType;
import com.swatcat.your_name.services.navigation.manager.events.AbBackButtonClickEvent;
import com.swatcat.your_name.services.navigation.manager.events.OnBackPressedEvent;
import com.swatcat.your_name.services.navigation.manager.events.TryNavigateBackEvent;
import com.swatcat.your_name.services.utils.UiPopupHelper;

public class ScreenNavigationBackManager {
    private static final String TAG = ScreenNavigationBackManager.class.getSimpleName();

    private static final int TIME_INTERVAL = 1600;

    private final BaseActivity activity;

    private ScreenAnimType animate;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean couldNavigateBack;

    public ScreenNavigationBackManager(BaseActivity activity) {
        this.activity = activity;
        this.animate = ScreenAnimType.NONE_TYPE;
        setCouldNavigateBack(true);
    }

    public void setScreenAnimType(ScreenAnimType animate) {
        this.animate = animate;
    }

    private void navigateBack() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i(TAG, "popping backstack");

            FragmentManager.BackStackEntry backEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            String fragmentName = backEntry.getName();
            fragmentManager.popBackStackImmediate(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            tryExitActivity();
        }
    }

    private void tryExitActivity() {
        Log.i(TAG, "nothing on backstack, calling finish");

        activity.hideKeyboard();

        if (activity.isTaskRoot()) {
            if (doubleBackToExitPressedOnce) {
                finishActivity();
                return;
            }

            doubleBackToExitPressedOnce = true;
            UiPopupHelper.showCustomToast(activity, activity.getString(R.string.message_exit_from_app), Gravity.BOTTOM);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }

            }, TIME_INTERVAL);
        } else {
            finishActivity();
        }
    }

    private void finishActivity() {
        activity.finish();
        activity.freeMemory();

        switch (animate) {
            case FADE_TYPE:
                activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case RIGHT_TO_LEFT_TYPE:
                activity.overridePendingTransition(R.anim.right_to_left_in, R.anim.right_to_left_out);
                break;
            case LEFT_TO_RIGHT_TYPE:
                activity.overridePendingTransition(R.anim.left_to_right_in, R.anim.left_to_right_out);
                break;
        }
    }

    // Events

    @Subscribe
    public void onEvent(AbBackButtonClickEvent event) {
        activity.hideKeyboard();
        navigateBack();
    }

    @Subscribe
    public void onEvent(TryExitEvent event){
        tryExitActivity();
    }

    @Subscribe
    public void onEvent(OnBackPressedEvent event) {
        if (isCouldNavigateBack()) {
            navigateBack();
        }
        else {
            activity.getBus().post(new TryNavigateBackEvent());
        }
    }

    public boolean isCouldNavigateBack() {
        return couldNavigateBack;
    }

    public void setCouldNavigateBack(boolean couldNavigateBack) {
        this.couldNavigateBack = couldNavigateBack;
    }
}
