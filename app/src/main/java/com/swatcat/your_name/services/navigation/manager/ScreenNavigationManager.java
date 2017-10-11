package com.swatcat.your_name.services.navigation.manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.swatcat.your_name.base.BaseActivity;
import com.swatcat.your_name.services.navigation.NavigationController;
import com.swatcat.your_name.services.navigation.Screen;
import com.swatcat.your_name.services.navigation.ScreenAnimType;
import com.swatcat.your_name.services.navigation.ScreenType;
import com.swatcat.your_name.services.navigation.factory.ScreenActivityFactory;
import com.swatcat.your_name.services.navigation.factory.ScreenFragmentFactory;


public class ScreenNavigationManager implements NavigationController {
    private static final String TAG = ScreenNavigationManager.class.getSimpleName();

    private final static String EXTRA_ACTIVE_SCREEN = "ScreenNavigationManager.activeScreen";

    public final static String ACTIVITY_REQUEST_CODE = "ScreenNavigationManager.activityRequestCode";

    private final ScreenActivityFactory activityFactory;
    private final ScreenFragmentFactory fragmentFactory;


    private final Bundle savedInstanceState;
    private final BaseActivity activity;


    private Screen activeScreen = Screen.NONE;

    public ScreenNavigationManager(BaseActivity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
        activityFactory = new ScreenActivityFactory();
        fragmentFactory = new ScreenFragmentFactory();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (activeScreen != null) {
            outState.putSerializable(EXTRA_ACTIVE_SCREEN, activeScreen);
        }
    }

    @Override
    public void navigateTo(Screen screen, ScreenType type) {
        navigateTo(screen, type, null);
    }

    @Override
    public void navigateTo(Screen screen, ScreenType type, Bundle bundle) {
        switch (type) {
            case ACTIVITY:
                navigateToActivity(screen, bundle);
                break;
            case FRAGMENT:
                navigateToFragment(screen, bundle);
                break;
            /*case OUTSIDE:
                navigateToOutside(screen, bundle);
                break;*/
        }
    }

    private void navigateToActivity(Screen screen, Bundle bundle) {
        switch (screen) {
            case MAIN:
                navigateToMain(bundle);
                break;

        }
    }



    private void navigateToFragment(Screen screen, Bundle bundle) {
        switch (screen) {

        }
    }


    private void navigateToMain(Bundle bundle) {
        Log.d(TAG, "start HomeActivity");

        switchActivityScreen(Screen.MAIN, bundle, ScreenAnimType.FADE_TYPE);

        activity.hideKeyboard();
        activity.finish();
        activity.freeMemory();
    }


    private void switchActivityScreen(Screen type, Bundle bundle, ScreenAnimType animate) {
        Intent intent = activityFactory.getActivityByType(type);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (bundle != null && !bundle.isEmpty()) {
            intent.putExtras(bundle);
        }

        // logic for enabling handling result in onActivityResult
        int requestCode = 0;
        if (bundle != null) {
            requestCode = bundle.getInt(ACTIVITY_REQUEST_CODE, 0);
        }
        if (requestCode != 0) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }


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
            case BOTTOM_TO_TOP_TYPE:
                activity.overridePendingTransition(R.anim.bottom_to_top, R.anim.top_to_bottom);
                break;
        }
    }

    private void switchFragmentScreen(Screen type, Bundle bundle, boolean animate, boolean addToBackStack) {
        if (isSameFragmentAlreadyPlaced(type)) {
            return;
        }

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction tran = fragmentManager.beginTransaction();
        if (animate) {
            tran.setCustomAnimations(R.anim.popup_in, R.anim.popup_out);
        }

        Fragment fragment = fragmentFactory.getFragmentByType(type);
        if (bundle != null && !bundle.isEmpty()) {
            fragment.setArguments(bundle);
        }
        if (addToBackStack) {
            if (animate) {
                tran.setCustomAnimations(R.anim.popup_in, R.anim.popup_out, R.anim.popup_in, R.anim.popup_out);
            }
            tran.replace(R.id.content_frame, fragment, fragment.getClass().getSimpleName());
            tran.addToBackStack(fragment.getClass().getSimpleName());
        } else {
            if (animate) {
                tran.setCustomAnimations(R.anim.popup_in, R.anim.popup_out);
            }
            tran.replace(R.id.content_frame, fragment);
        }
        tran.commit();
    }

    private boolean isSameFragmentAlreadyPlaced(Screen type) {
        Fragment existing = activity.getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (existing != null) {
            Class<? extends Fragment> requested = fragmentFactory.getFragmentClassByType(type);
            if (existing.getClass().equals(requested)) {
                return true;
            }
        }
        return false;
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Screen savedScreen = (Screen) savedInstanceState.getSerializable(EXTRA_ACTIVE_SCREEN);
            if (savedScreen != null) {
                setInitialScreen(savedScreen);
            }
        } else {
            setInitialScreen(Screen.MAIN);
        }
    }

    private void setInitialScreen(Screen savedScreen) {
        switchFragmentScreen(savedScreen, null, false, true);
        activeScreen = savedScreen;
    }

    public Screen getActiveScreen() {
        return activeScreen;
    }
}
