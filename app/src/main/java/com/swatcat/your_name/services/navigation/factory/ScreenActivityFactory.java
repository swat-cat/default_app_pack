package com.swatcat.your_name.services.navigation.factory;

import android.app.Activity;
import android.content.Intent;


import com.swatcat.your_name.LoginActivity;
import com.swatcat.your_name.MainActivity;
import com.swatcat.your_name.base.App;
import com.swatcat.your_name.services.navigation.Screen;


public class ScreenActivityFactory {

    public Intent getActivityByType(Screen type) {
        Class<? extends Activity> clazz = getActivityClassByType(type);
        return new Intent(App.getInstance().getContext(), clazz);
    }

    public Class<? extends Activity> getActivityClassByType(Screen type) {
        switch (type) {
            case MAIN:
                return MainActivity.class;
            case LOGIN:
                return LoginActivity.class;


            default:
                return LoginActivity.class;
        }
    }
}
