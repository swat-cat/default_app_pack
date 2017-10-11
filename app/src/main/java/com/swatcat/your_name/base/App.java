package com.swatcat.your_name.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;

import com.orhanobut.hawk.Hawk;
import com.swatcat.your_name.services.rest.RestService;

import timber.log.Timber;

/**
 * Created by max_ermakov on 9/20/17.
 */

public class App extends MultiDexApplication {

    private static App instance;
    private Context context;
    private Handler handler;
    private RestService restService;
    private BaseActivity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        handler = new Handler(Looper.getMainLooper());
        context = getApplicationContext();
        Hawk.init(context).build();
        Timber.plant(new Timber.DebugTree());
    }

    public static App getInstance() {
        if (instance == null) {
            throw new RuntimeException("Application initialization error!");
        }
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }


    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public  void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
