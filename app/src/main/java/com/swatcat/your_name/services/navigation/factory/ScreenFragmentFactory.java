package com.swatcat.your_name.services.navigation.factory;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.swatcat.your_name.base.App;
import com.swatcat.your_name.services.navigation.Screen;


public class ScreenFragmentFactory {

    protected Context context;

    public Fragment getFragmentByType(Screen type) {
        Class<? extends Fragment> clazz = getFragmentClassByType(type);
        return Fragment.instantiate(App.getInstance().getContext(), clazz.getName());
    }

    public Class<? extends Fragment> getFragmentClassByType(Screen type) {
        /*switch (type) {


        }*/
        return null;
    }
}
