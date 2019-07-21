package com.burakkal.taskplanner;

import android.app.Activity;
import android.app.Application;

import com.burakkal.taskplanner.di.component.AppComponent;
import com.burakkal.taskplanner.di.component.DaggerAppComponent;
import com.burakkal.taskplanner.di.module.AppContextModule;
import com.burakkal.taskplanner.di.module.DatabaseModule;

import timber.log.Timber;

public class App extends Application {

    AppComponent component;

    public static App get(Activity activity) {
        return (App) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        component = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .databaseModule(new DatabaseModule())
                .build();
    }

    public AppComponent getComponent() {
        return component;
    }
}
